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
public class FileLocator implements InputStreamProvider {
    
    /**
     * The input stream associated with the located filesystem.
     */
    protected InputStream fileInputStream;

    /**
     * An interface object to provide the file-localizing process with a command-state pattern;
     * binding the user application interface with the file localizing lifecycle.
     */
    protected FileLocalizingListener fileLocalizingListener;

    protected String directory;

    protected String filePath;

    protected ZipCompressionType compressionType;

    /**
     * Locates a filesystem inside an external zip compression, the zip filesystem is defined as a {@link ZipFile} object and
     * the locatable filesystem is defined as a {@link ZipEntry} object.
     * <p>
     * Warning: This object leaks a buffered stream, either use try-with-resources, or handle your
     * memory manually!
     * 
     * @param directory the absolute path for the external jar filesystem
     * @param filePath the path to the filesystem to be extracted
     * @param compressionType the type of the zip compression, ZIP or JAR
     * 
     * @throws IOException if the jar to be located is not found or an interrupted I/O exception has occured
     */
    public FileLocator(String directory, String filePath, ZipCompressionType compressionType) throws IOException {
        this.directory = directory;
        this.filePath = filePath;
        this.compressionType = compressionType;
    }

    /**
     * Instantiates an empty filesystem locator object.
     */
    protected FileLocator() {   
    }


    @Override
    public void initialize(int size) throws IOException {
        // 1) sanity-check for double initializing
        // 2) sanity-check for pre-initialization using other routines
        // (e.g., classpath resources stream).
        if (this.fileInputStream != null) {
            return;
        }
        try {
            final ZipFile compression = compressionType.createNewCompressionObject(directory);
            final ZipEntry zipEntry = compression.getEntry(filePath);
            validateFileLocalization(zipEntry);
            if (size > 0) {
                this.fileInputStream = new BufferedInputStream(compression.getInputStream(zipEntry), size);
                SnapLoaderLogger.log(Level.INFO, getClass().getName(), "initialize(int)",
                        "File locator initialized with hash key #" + getHashKey());
                return;
            }
            this.fileInputStream = compression.getInputStream(zipEntry);
            SnapLoaderLogger.log(Level.INFO, getClass().getName(), "initialize(int)",
                    "File locator initialized with hash key #" + getHashKey());
        } catch (Exception e) {
            close();
            throw new FilesystemResourceInitializationException(
                    "Failed to initialize the file locator handler #" + getHashKey(), e);
        }
    }

    /**
     * Validates the file localization process inside the compression.
     *
     * @throws FileNotFoundException if the localization of the file inside
     *                               the specified compression has failed.
     */
    protected void validateFileLocalization(final ZipEntry zipEntry) throws FileNotFoundException {
        if (zipEntry != null) {
            if (fileLocalizingListener != null) {
                fileLocalizingListener.onFileLocalizationSuccess(this);
            }
        } else {
            final FileNotFoundException fileNotFoundException =
                    new FileNotFoundException("File locator has failed to locate the file inside the compression!");

            if (fileLocalizingListener != null) {
                fileLocalizingListener.onFileLocalizationFailure(this, fileNotFoundException);
            }
            throw fileNotFoundException;
        }
    }

    @Override
    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    @Override
    public void close() throws IOException {
        if (fileInputStream != null) {
            fileInputStream.close();
            fileInputStream = null;
        }
        SnapLoaderLogger.log(Level.INFO, getClass().getName(),
                "close", "File locator #" + getHashKey() + " resources closed!");
    }

    @Override
    public void setFileLocalizingListener(FileLocalizingListener fileLocalizingListener) {
        this.fileLocalizingListener = fileLocalizingListener;
    }
}
