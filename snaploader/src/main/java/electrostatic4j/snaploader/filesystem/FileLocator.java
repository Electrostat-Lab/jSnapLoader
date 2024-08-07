/*
 * Copyright (c) 2023-2024, The Electrostatic-Sandbox Distributed Simulation Framework, jSnapLoader
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'Electrostatic-Sandbox' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package electrostatic4j.snaploader.filesystem;

import electrostatic4j.snaploader.throwable.FilesystemResourceInitializationException;
import electrostatic4j.snaploader.util.SnapLoaderLogger;
import java.io.*;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * An Input Stream Provider that locates a filesystem inside a zip compression and provides an
 * input stream object for the located filesystem entry.
 * 
 * @author pavl_g
 */
public class FileLocator implements ZipStreamProvider {
    
    /**
     * The input stream associated with the located filesystem.
     */
    protected InputStream fileInputStream;

    /**
     * An interface object to provide the file-localizing process with a command-state pattern;
     * binding the user application interface with the file localizing lifecycle.
     */
    protected FileLocalizingListener fileLocalizingListener;

    /**
     * Resembles the compression stream provider object, used in the case of
     * external compression routines.
     */
    protected ZipFile compression;

    /**
     * Resembles the file path inside the compression.
     */
    protected String filePath;

    /**
     * Locates the library inside the stock jar filesystem.
     *
     * @param filePath the path to the dynamic native library inside that jar filesystem
     */
    public FileLocator(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Locates a filesystem inside an external zip compression, the zip filesystem is defined as a {@link ZipFile} object and
     * the locatable filesystem is defined as a {@link ZipEntry} object.
     * 
     * @param compression the compression, either {@link ZipFile} or {@link java.util.jar.JarFile}
     * @param filePath the path to the filesystem inside the compression to be extracted
     * 
     * @throws IOException if the jar to be located is not found or an interrupted I/O exception has occurred
     */
    public FileLocator(ZipFile compression, String filePath) throws IOException {
        this.filePath = filePath;
        this.compression = compression;
    }

    /**
     * Instantiates an empty filesystem locator object.
     */
    protected FileLocator() {   
    }


    /**
     * Initializes the input stream provider through a file locator routine, either
     * classpath routine or external archive routine.
     * <p>
     * Warning: this stack leaks an input stream provider object for the
     * file to be extracted, and the external archive stream provider in case
     * of using an external archive routine to locate the file.
     *
     * @param size the size of the buffered IO in bytes or zero
     *             for auto filesystem size
     * @throws IOException if an I/O error has occurred.
     */
    @Override
    public void initialize(int size) throws IOException {
        // 1) sanity-check for double initializing
        if (this.fileInputStream != null) {
            SnapLoaderLogger.log(Level.INFO, getClass().getName(), "initialize(int)",
                    "File locator already initialized using external routines with hash key #" + getHashKey());
            return;
        }
        try {

            // 2) sanity-check for initialization routines
            // (e.g., classpath resources stream v.s. external compression).
            if (compression == null) {
                classPathRoutine();
            } else {
                externalCompressionRoutine(size);
            }

            // fire the success listener if the file localization has passed!
            if (fileLocalizingListener != null) {
                fileLocalizingListener.onFileLocalizationSuccess(this);
            }
        } catch (Exception e) {
            close();
            final FilesystemResourceInitializationException exception = new FilesystemResourceInitializationException(
                    "Failed to initialize the file locator handler #" + getHashKey(), e);
            // fire the failure listener when file localization fails and pass
            // the causative exception
            if (fileLocalizingListener != null) {
                fileLocalizingListener.onFileLocalizationFailure(this, exception);
            }
            throw exception;
        }
    }

    /**
     * Commands for the classpath routines.
     */
    protected void classPathRoutine() {
        SnapLoaderLogger.log(Level.INFO, getClass().getName(), "initialize(int)",
                "File locator initialized using classpath routine with hash key #" + getHashKey());
        // use the AppClassLoader, a BuiltinClassLoader to get the resources from the classpath
        // notice that the JVM Classloaders are arranged in a tree-like structure
        // 1) The BootStrap ClassLoader is the most ancestor
        // 2) The Java Platform ClassLoader is the next in the tree.
        // 3) The AppClassLoader is the last in the tree.
        this.fileInputStream = getClass().getClassLoader().getResourceAsStream(filePath);
    }

    /**
     * Commands for the external compression routines.
     *
     * @param size custom buffer size, zero for auto filesystem size
     *             (warning: file expansion and truncation rules are applied).
     * @throws IOException if an I/O error has occurred.
     */
    protected void externalCompressionRoutine(int size) throws IOException {
        final ZipEntry zipEntry = compression.getEntry(filePath);
        if (size > 0) {
            this.fileInputStream = new BufferedInputStream(compression.getInputStream(zipEntry), size);
        } else {
            this.fileInputStream = compression.getInputStream(zipEntry);
        }
        SnapLoaderLogger.log(Level.INFO, getClass().getName(), "initialize(int)",
                "File locator initialized using external compression routine with hash key #" + getHashKey());
    }

    @Override
    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    @Override
    public void close() throws IOException {
        // this is executed in all routines
        // it updates the CleanableResource object
        // for the Compression Routines
        if (fileInputStream != null) {
            fileInputStream.close();
            fileInputStream = null;
        }

        // this will be bypassed in the case of using other non-compression routines to
        // initialize the file streams (e.g., the classpath routines).
        if (compression != null) {
            // this closes all the streams associated with it (if they are not closed)
            // aka.
            // the file entries stream
            // and the native resources for this object
            compression.close();
            compression = null;
        }
        SnapLoaderLogger.log(Level.INFO, getClass().getName(),
                "close", "File locator #" + getHashKey() + " resources closed!");
    }

    @Override
    public void setFileLocalizingListener(FileLocalizingListener fileLocalizingListener) {
        this.fileLocalizingListener = fileLocalizingListener;
    }

    @Override
    public ZipFile getCompression() {
        return compression;
    }
}
