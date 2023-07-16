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
 * A cross-platform utility for extracting and loading native binaries based on 
 * the variant properties (OS + ARCH + VM).
 * 
 * @author pavl_g.
 */
public class NativeBinaryLoader {

    /**
     * NativeBinaryLoader logger object.
     */
    protected static final Logger logger = Logger.getLogger(NativeBinaryLoader.class.getName());
    
    /**
     * A data structure that wraps the general basic dynamic library info.
     */
    protected LibraryInfo libraryInfo;

    /**
     * An Output stream concrete provider for library extraction.
     */
    protected FileExtractor libraryExtractor;

    /**
     * The native dynamic library object representing the library to extract and load.
     */
    protected NativeDynamicLibrary nativeDynamicLibrary;

    /**
     * Instantiates a native dynamic library loader to extract and load a system specific native dynamic library.
     * 
     * @param libraryInfo a data structure object representing the basic dynamic library info
     */
    public NativeBinaryLoader(LibraryInfo libraryInfo) {
        this.libraryInfo = libraryInfo;
        NativeDynamicLibrary.initWithLibraryInfo(libraryInfo);
    }

    /**
     * Extracts and loads the system and the architecture specific library from the output jar to the [user.dir].
     * 
     * @param criterion the loading criterion, either {@link LoadingCriterion#INCREMENTAL_LOADING} or {@link LoadingCriterion#CLEAN_EXTRACTION}
     * @throws IOException if the library to extract is not present in the jar file
     * @throws UnSupportedSystemError if the OS is not supported by jSnapLoader
     */
    public void loadLibrary(LoadingCriterion criterion) throws IOException, UnSupportedSystemError {
        /* extracts and loads the system specific library */
        if (NativeVariant.isLinux()) {
            loadLinux(criterion);
        } else if (NativeVariant.isWindows()) {
            loadWindows(criterion);
        } else if (NativeVariant.isMac()) {
            loadMac(criterion);
        } else {
            throw new UnSupportedSystemError(NativeVariant.NAME.getProperty(), NativeVariant.ARCH.getProperty());
        }
    }
    
    /**
     * Retrieves the native dynamic library object representing the library to extract and load.
     * 
     * @return an object representing the native dynamic library
     */
    public NativeDynamicLibrary getNativeDynamicLibrary() {
        return nativeDynamicLibrary;
    }
    
    /**
     * Loads the android binary automatically by its variant.
     */
    protected void loadAndroid() {
        System.loadLibrary(libraryInfo.getBaseName());
    }

    /**
     * Extracts and loads the architecture specific library from [libs/linux] from the specified jar to the extraction directory.
     * If the system is an android system then load the library directly.
     * 
     * @param criterion the loading criterion, either {@link LoadingCriterion#INCREMENTAL_LOADING} or {@link LoadingCriterion#CLEAN_EXTRACTION}
     * @throws IOException if the binary to extract is not present in the jar file
     * @see NativeDynamicLibrary#LINUX_x86
     * @see NativeDynamicLibrary#LINUX_x86_64
     */
    protected void loadLinux(LoadingCriterion criterion) throws IOException {
        /* sanity check for android java vm (the dalvik) */
        if (NativeVariant.isAndroid()) {
            loadAndroid();
            return;
        }

        if (!NativeVariant.isX86()) {
            incrementalExtractBinary(NativeDynamicLibrary.LINUX_x86_64, criterion);
        } else {
            incrementalExtractBinary(NativeDynamicLibrary.LINUX_x86, criterion);
        }
    }

    /**
     * Extracts and loads the architecture specific library from [libs/windows] from the specified jar to the extraction directory.
     * 
     * @param criterion the loading criterion, either {@link LoadingCriterion#INCREMENTAL_LOADING} or {@link LoadingCriterion#CLEAN_EXTRACTION}
     * @throws IOException if the binary to extract is not present in the jar file
     * @see NativeDynamicLibrary#WIN_x86
     * @see NativeDynamicLibrary#WIN_x86_64
     */
    protected void loadWindows(LoadingCriterion criterion) throws IOException {
        if (!NativeVariant.isX86()) {
            incrementalExtractBinary(NativeDynamicLibrary.WIN_x86_64, criterion);
        } else {
            incrementalExtractBinary(NativeDynamicLibrary.WIN_x86, criterion);
        }
    }

    /**
     * Extracts and loads the architecture specific library from [libs/macos] from the specified jar to the extraction directory.
     * 
     * @param criterion the loading criterion, either {@link LoadingCriterion#INCREMENTAL_LOADING} or {@link LoadingCriterion#CLEAN_EXTRACTION}
     * @throws IOException if the binary to extract is not present in the jar file
     * @see NativeDynamicLibrary#MAC_x86
     * @see NativeDynamicLibrary#MAC_x86_64
     */
    protected void loadMac(LoadingCriterion criterion) throws IOException {
        if (!NativeVariant.isX86()) {
            incrementalExtractBinary(NativeDynamicLibrary.MAC_x86_64, criterion);
        } else {
            incrementalExtractBinary(NativeDynamicLibrary.MAC_x86, criterion);
        }
    }

    /**
     * Loads a binary with a criterion.
     * 
     * @param library the native library to load
     * @throws IOException in case the binary to be extracted is not found on the output jar
     */
    protected void loadBinary(final NativeDynamicLibrary library) throws IOException {
        try {
            System.load(library.getExtractedLibrary());
        } catch (final UnsatisfiedLinkError error) {
            logger.log(Level.SEVERE, "Cannot load the dynamic library: " + library.getExtractedLibrary(), error);
        }
    }
    

    /**
     * Incrementally extracts and loads the native binary to the current [user.dir] with a retry binary load criteria.
     * The retry criteria utilizes the {@link java.lang.UnsatisfiedLinkError} to cleanly re-extract and re-load the binary in case of 
     * broken binaries.
     * 
     * @param library the native library to extract and load
     * @param criterion the dynamic loading criterion, either {@link LoadingCriterion#INCREMENTAL_LOADING} or {@link LoadingCriterion#CLEAN_EXTRACTION}
     * @throws IOException in case the binary to be extracted is not found on the output jar
     */
    protected void incrementalExtractBinary(NativeDynamicLibrary library, LoadingCriterion criterion) throws IOException {  
        this.nativeDynamicLibrary = library;
        if (criterion == LoadingCriterion.INCREMENTAL_LOADING && library.isExtracted()) {
            loadBinary(library);
            return;
        }
        cleanExtractBinary(library);
    }

    /**
     * Cleanly extracts and loads the native binary to the current [user.dir].
     * 
     * @param library the library to extract and load
     * @throws IOException in case the binary to be extracted is not found on the specified jar or an 
     *                     interrupted I/O operation has occured
     */
    protected void cleanExtractBinary(NativeDynamicLibrary library) throws IOException {
        libraryExtractor = initializeLibraryExtractor(library);
        libraryExtractor.extract();
        /* CLEAR RESOURCES AND RESET OBJECTS */
        libraryExtractor.setExtractionListener(() -> {
            try{
                loadBinary(library);
                libraryExtractor.getFileLocator().close();
                libraryExtractor.close();
                libraryExtractor = null;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error while closing the resources!", e);
            }
        });
    }

    /**
     * Initializes a file extrator object if the file extractor object associated with this loader isnot defined.
     * 
     * @param library the native dynamic library to load 
     * @return a new FileExtractor object that represents an output stream provider
     * @throws IOException if the jar file to be located is not found, or if the extraction destination is not found
     */
    protected FileExtractor initializeLibraryExtractor(NativeDynamicLibrary library) throws IOException {
        if (library.getJarPath() != null) {
            return new LibraryExtractor(library.getJarPath(), library.getCompressedLibrary(), library.getExtractedLibrary());
        }
        return new LibraryExtractor(library.getCompressedLibrary(), library.getExtractedLibrary());
    }
}
