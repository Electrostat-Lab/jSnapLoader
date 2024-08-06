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
    protected FileExtractionListener fileExtractionListener;

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
     */
    public FileExtractor(FileLocator fileLocator, String destination) {
        this.fileLocator = fileLocator;
        this.destination = destination;
    }

    /**
     * Instantiates an empty filesystem extractor.
     */
    protected FileExtractor() {
    }

    @Override
    public void initialize(int size) throws Exception {
        // 1) sanity-check for double initializing
        // 2) sanity-check for pre-initialization using other routines
        if (this.fileOutputStream != null) {
            SnapLoaderLogger.log(Level.INFO, getClass().getName(), "initialize(int)",
                    "File extractor already initialized using external routines with hash key #" + getHashKey());
            return;
        }
        try {
            if (size > 0) {
                this.fileOutputStream = new BufferedOutputStream(
                        new FileOutputStream(destination), size);
                SnapLoaderLogger.log(Level.INFO, getClass().getName(), "initialize(int)",
                        "File extractor initialized with hash key #" + getHashKey());
                return;
            }
            this.fileOutputStream = new FileOutputStream(destination);
            SnapLoaderLogger.log(Level.INFO, getClass().getName(), "initialize(int)",
                    "File extractor initialized with hash key #" + getHashKey());
        } catch (Exception e) {
            close();
            throw new FilesystemResourceInitializationException(
                    "Failed to initialize the file extractor handler #" + getHashKey(), e);
        }
    }

    /**
     * Commands and Extract the specified filesystem to the specified destination filesystem.
     * <p>
     * Warning: this function leaks buffered streams; this vision was attained for freedom of use,
     * but the user application must keep in mind that stream closure and resources release must be
     * attained either through the extraction completed and failure listeners, or through a try-with
     * resources.
     *
     * @throws IOException if the input/output streams has failed or an interrupted I/O operation has occurred.
     * @throws FileNotFoundException if the file locator has failed to locate the file inside the compression
     *                               for the extraction process.
     */
    public void extract() throws IOException, FileNotFoundException {
        try {
            /* uses buffered streams */
            /* buffered byte streams provide a constant memory allocation
             * according to the file size in bytes,
             * this constant memory allocation is then treated
             * as a pipe; either a unidirectional (simplex) or a bidirectional (duplex)
             * unlike the unbuffered streams, which polls byte streams from an online
             * pipe, and allocate memory according to the active bytes manipulated
             * by the pipeline. */
            InputStream fileStream = fileLocator.getFileInputStream();

            /* Extracts the shipped native files */
            /* Allocate a byte buffer for the buffered streams */
            final byte[] buffer = new byte[fileStream.available()];

            for (int bytes = 0; bytes != EOF; bytes = fileStream.read(buffer)) {
                /* use the bytes as the buffer length to write valid data */
                fileOutputStream.write(buffer, 0, bytes);
            }
            if (fileExtractionListener != null) {
                fileExtractionListener.onExtractionCompleted(this);
            }
        } catch (Exception e) {
            if (fileExtractionListener != null) {
                fileExtractionListener.onExtractionFailure(this, e);
            }
        // release the native resources anyway!
        } finally {
            if (fileExtractionListener != null) {
                fileExtractionListener.onExtractionFinalization(this, fileLocator);
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (fileOutputStream != null) {
            fileOutputStream.close();
            fileOutputStream = null;
        }

        // close the associated file locator resources
        if (getFileLocator() != null && getFileLocator().getFileInputStream() != null) {
            getFileLocator().close();
            SnapLoaderLogger.log(Level.INFO, getClass().getName(), "close",
                    "Delegation for file locator resources closure has succeeded!");
        }

        SnapLoaderLogger.log(Level.INFO, getClass().getName(), "close",
                "File extractor #" + getHashKey() + " resources closed!");
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
     * Sets the extraction listener action to dispatch the {@link FileExtractionListener#onExtractionCompleted(FileExtractor)}
     * when the extraction task is completed.
     * 
     * @param fileExtractionListener an implementation object of the extraction listener dispatched when the
     *                           extraction is completed
     */
    public void setExtractionListener(FileExtractionListener fileExtractionListener) {
        this.fileExtractionListener = fileExtractionListener;
    }
}
