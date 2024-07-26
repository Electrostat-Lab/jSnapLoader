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

package electrostatic.snaploader.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        ZipFile compression = compressionType.createNewCompressionObject(directory);
        ZipEntry nativeLibraryEntry = compression.getEntry(filePath);
        this.fileInputStream = compression.getInputStream(nativeLibraryEntry);
    }

    /**
     * Instantiates an empty filesystem locator object.
     */
    protected FileLocator() {   
    }

    /**
     * Validates the file localization process inside the compression.
     *
     * @throws FileNotFoundException if the localization of the file inside
     *                               the specified compression has failed.
     */
    public void validateFileLocalization() throws FileNotFoundException {
        if (fileInputStream != null) {
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
        fileInputStream.close();
        fileInputStream = null;
    }

    @Override
    public void setFileLocalizingListener(FileLocalizingListener fileLocalizingListener) {
        this.fileLocalizingListener = fileLocalizingListener;
    }
}
