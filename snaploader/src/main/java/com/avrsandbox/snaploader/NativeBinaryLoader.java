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
package com.avrsandbox.snaploader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.UnsatisfiedLinkError;
import com.avrsandbox.snaploader.file.FileExtractor;
import com.avrsandbox.snaploader.library.LibraryExtractor;

/**
 * Helper utility for loading native binaries.
 * 
 * @author pavl_g.
 */
public final class NativeBinaryLoader {
    
    private static final Logger logger = Logger.getLogger(NativeBinaryLoader.class.getName());
    private final LibraryInfo libraryInfo;
    private FileExtractor libraryExtractor;
    private boolean incrementalLoadEnabled;
    private NativeDynamicLibrary nativeDynamicLibrary;

    public NativeBinaryLoader(LibraryInfo libraryInfo) {
        this.libraryInfo = libraryInfo;
    }

    /**
     * Extracts and loads the variant specific binary from the output jar, handling the error messages.
     */
    public void loadLibraryIfEnabled() {
        try {
            NativeDynamicLibrary.initWithLibraryInfo(libraryInfo);
            /* extracts and loads the system specific library */
            loadLibrary();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Binary Not Found!", e);
        }
    }

    /**
     * Extracts and loads the system and the architecture specific library from the output jar to the [user.dir].
     * 
     * @throws IOException if the library to extract is not present in the jar file
     */
    public void loadLibrary() throws IOException {
        if (NativeVariant.isLinux()) {
            loadLinux();
        } else if (NativeVariant.isWindows()) {
            loadWindows();
        } else if (NativeVariant.isMac()) {
            loadMac();
        } else {
            throw new UnSupportedSystemError(NativeVariant.NAME.getProperty(), NativeVariant.ARCH.getProperty());
        }
    }

    public void setIncrementalLoadEnabled(boolean incrementalLoadEnabled) {
        this.incrementalLoadEnabled = incrementalLoadEnabled;
    }

    public boolean isIncrementalLoadEnabled() {
        return incrementalLoadEnabled;
    }

    public NativeDynamicLibrary getNativeDynamicLibrary() {
        if (nativeDynamicLibrary == null) {
            throw new IllegalStateException("Native Dynamic library hasn't been initialized, ensure running the loader first!");
        }
        return nativeDynamicLibrary;
    }
    
    /**
     * Loads the android binary automatically by its variant.
     */
    private void loadAndroid() {
        System.loadLibrary(libraryInfo.getBaseName());
    }

    /**
     * Extracts and loads the architecture specific library from [libs/linux] from the output jar to the [user.dir].
     * If the system is an android system then load the library directly.
     * 
     * @throws IOException if the binary to extract is not present in the jar file
     * @see NativeDynamicLibrary#LINUX_x86
     * @see NativeDynamicLibrary#LINUX_x86_64
     */
    private void loadLinux() throws IOException {
        /* sanity check for android java vm (the dalvik) */
        if (NativeVariant.isAndroid()) {
            loadAndroid();
            return;
        }

        if (!NativeVariant.isX86()) {
            incrementalExtractBinary(NativeDynamicLibrary.LINUX_x86_64);
        } else {
            incrementalExtractBinary(NativeDynamicLibrary.LINUX_x86);
        }
    }

    /**
     * Extracts and loads the architecture specific library from [libs/windows] from the output jar to the [user.dir].
     * 
     * @throws IOException if the binary to extract is not present in the jar file
     * @see NativeDynamicLibrary#WIN_x86
     * @see NativeDynamicLibrary#WIN_x86_64
     */
    private void loadWindows() throws IOException {
        if (!NativeVariant.isX86()) {
            incrementalExtractBinary(NativeDynamicLibrary.WIN_x86_64);
        } else {
            incrementalExtractBinary(NativeDynamicLibrary.WIN_x86);
        }
    }

    /**
     * Extracts and loads the architecture specific library from [libs/macos] from the output jar to the [user.dir].
     * 
     * @throws IOException if the binary to extract is not present in the jar file
     * @see NativeDynamicLibrary#MAC_x86
     * @see NativeDynamicLibrary#MAC_x86_64
     */
    private void loadMac() throws IOException {
        if (!NativeVariant.isX86()) {
            incrementalExtractBinary(NativeDynamicLibrary.MAC_x86_64);
        } else {
            incrementalExtractBinary(NativeDynamicLibrary.MAC_x86);
        }
    }

    /**
     * Loads a binary with a retry criteria.
     * 
     * @params library the native library to load
     * @params criteria a retry criteria, default is {@link RetryCriteria#RETRY_WITH_CLEAN_EXTRACTION}
     * @throws IOException in case the binary to be extracted is not found on the output jar
     */
    private void loadBinary(final NativeDynamicLibrary library, final RetryCriteria criteria) throws IOException {
        try {
            System.load(library.getExtractedLibrary());
        } catch (final UnsatisfiedLinkError error) {
            switch (criteria) {
                case RETRY_WITH_INCREMENTAL_EXTRACTION:
                    incrementalExtractBinary(library);
                    break;
                
                default:
                    cleanExtractBinary(library);
                    break;
            }
        }
    }
    

    /**
     * Incrementally extracts and loads the native binary to the current [user.dir] with a retry binary load criteria.
     * The retry criteria utilizes the {@link java.lang.UnsatisfiedLinkError} to cleanly re-extract and re-load the binary in case of 
     * broken binaries.
     * 
     * @params library the native library to extract and load
     * @throws IOException in case the binary to be extracted is not found on the output jar
     */
    private void incrementalExtractBinary(NativeDynamicLibrary library) throws IOException {  
        this.nativeDynamicLibrary = library;
        if (isIncrementalLoadEnabled() && library.isExtracted()) {
            loadBinary(library, RetryCriteria.RETRY_WITH_CLEAN_EXTRACTION);
            return;
        }
        cleanExtractBinary(library);
    }

    /**
     * Cleanly extracts and loads the native binary to the current [user.dir].
     * 
     * @params library the library to extract and load
     * @throws IOException in case the binary to be extracted is not found on the specified jar or an 
     *                     interrupted I/O operation has occured
     */
    private void cleanExtractBinary(NativeDynamicLibrary library) throws IOException {
        try {
            libraryExtractor = initializeLibraryExtractor(library);
            libraryExtractor.extract();
            loadBinary(library, RetryCriteria.RETRY_WITH_CLEAN_EXTRACTION);
        } finally {
            /* CLEAR RESOURCES AND RESET OBJECTS */
            libraryExtractor.getFileLocator().close();
            libraryExtractor.close();
            libraryExtractor = null;
        }
    }

    /**
     * Initializes a file extrator object if the file extractor object associated with this loader isnot defined.
     * 
     * @param library the native dynamic library to load 
     * @return
     * @throws IOException
     */
    private FileExtractor initializeLibraryExtractor(NativeDynamicLibrary library) throws IOException {
        if (library.getJarPath() != null) {
            return new LibraryExtractor(library.getJarPath(), library.getCompressedLibrary(), library.getExtractedLibrary());
        }
        return new LibraryExtractor(library.getCompressedLibrary(), library.getExtractedLibrary());
    }
}