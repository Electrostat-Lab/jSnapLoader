/*
 * Copyright (c) 2023, AvrSandbox, jSnapLoader
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
 * * Neither the name of 'AvrSandbox' nor the names of its contributors
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
package com.avrsandbox.snaploader.filesystem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Extracts a filesystem from a zip compression to a destination filesystem.
 * 
 * @author pavl_g
 */
public class FileExtractor implements OutputStreamProvider {
    
    /**
     * Locates a filesystem inside a zip compression.
     */
    protected FileLocator fileLocator;
    
    /**
     * Provides an output byte stream for this FileExtractor object.
     */
    protected OutputStream fileOutputStream;

    /**
     * An interface object to provide the extraction process with a command-state pattern.
     */
    protected ExtractionListener extractionListener;

    /**
     * An absolute path for the destination filesystem of the extraction process.
     */
    protected String destination;
    private static final int EOF = -1; /* End-of-filesystem */

    /**
     * Instantiates a filesystem extractor object with a filesystem locator and a destination filesystem.
     * 
     * @param fileLocator locates a filesystem inside a zip compression
     * @param destination an absolute filesystem path representing the extraction destination filesystem
     * @throws FileNotFoundException if the destination filesystem path is not found
     */
    public FileExtractor(FileLocator fileLocator, String destination) throws FileNotFoundException {
        this.fileLocator = fileLocator;
        this.fileOutputStream = new FileOutputStream(destination);
    }

    /**
     * Instantiates an empty filesystem extractor.
     */
    protected FileExtractor() {
    }

    /**
     * Commands and Extract the specified filesystem to the specified destination filesystem.
     * 
     * @throws IOException if the input/output streams has failed or an interrupted I/O operation has occured
     */
    public void extract() throws IOException {
        try {
            InputStream libraryStream = fileLocator.getFileInputStream();
            /* Extracts the shipped native files */
            final byte[] buffer = new byte[libraryStream.available()];
            for (int bytes = 0; bytes != EOF; bytes = libraryStream.read(buffer)) {
                /* use the bytes as the buffer length to write valid data */
                fileOutputStream.write(buffer, 0, bytes);
            }
            if (extractionListener != null) {
                extractionListener.onExtractionCompleted(this);
            }
        } catch (Exception e) {
            if (extractionListener != null) {
                extractionListener.onExtractionFailure(this, e);
            }
        // release the native resources anyway!
        } finally {
            if (extractionListener != null) {
                extractionListener.onExtractionFinalization(this, fileLocator);
            }
        }
    }

    @Override
    public void close() throws IOException {
        fileOutputStream.close();
        fileOutputStream = null;
    }

    @Override
    public OutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    @Override
    public InputStreamProvider getFileLocator() {
        return fileLocator;
    }

    /**
     * Sets the extraction listener action to dispatch the {@link ExtractionListener#onExtractionCompleted(FileExtractor)}
     * when the extraction task is completed.
     * 
     * @param extractionListener an implementation object of the extraction listener dispatched when the 
     *                           extraction is completed
     */
    public void setExtractionListener(ExtractionListener extractionListener) {
        this.extractionListener = extractionListener;
    }
}
