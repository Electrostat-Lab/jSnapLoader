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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.UnsatisfiedLinkError;

import com.avrsandbox.snaploader.filesystem.ExtractionListener;
import com.avrsandbox.snaploader.filesystem.FileExtractor;
import com.avrsandbox.snaploader.filesystem.FileLocator;
import com.avrsandbox.snaploader.library.LibraryExtractor;
import com.avrsandbox.snaploader.platform.NativeDynamicLibrary;
import com.avrsandbox.snaploader.platform.util.NativeVariant;

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

    protected final LibraryInfo libraryInfo;

    protected List<NativeDynamicLibrary> registeredLibraries;

    protected NativeBinaryLoadingListener nativeBinaryLoadingListener;

    protected SystemFoundListener systemFoundListener;

    /**
     * An Output stream concrete provider for library extraction.
     */
    protected FileExtractor libraryExtractor;

    /**
     * The native dynamic library object representing the library to extract and load.
     */
    protected NativeDynamicLibrary nativeDynamicLibrary;

    /**
     * Flag for enable/disable logging.
     */
    protected boolean loggingEnabled;

    /**
     * Flag for retry loading with clean extract if UnSatisfiedLinkError is thrown.
     */
    protected boolean retryWithCleanExtraction;

    /**
     * Instantiates a native dynamic library loader to extract and load a system-specific native dynamic library.
     */
    public NativeBinaryLoader(final LibraryInfo libraryInfo) {
        this.libraryInfo = libraryInfo;
    }

    /**
     * Instantiates a native dynamic library loader to extract and load a system-specific native dynamic library.
     */
    public NativeBinaryLoader(final List<NativeDynamicLibrary> registeredLibraries, final LibraryInfo libraryInfo) {
        this(libraryInfo);
        this.registeredLibraries = registeredLibraries;
    }

    public NativeBinaryLoader registerNativeLibraries(NativeDynamicLibrary[] nativeDynamicLibraries) {
        this.registeredLibraries = List.of(nativeDynamicLibraries);
        return this;
    }

    public List<NativeDynamicLibrary> getRegisteredLibraries() {
        return registeredLibraries;
    }

    public void setNativeBinaryLoadingListener(NativeBinaryLoadingListener nativeBinaryLoadingListener) {
        this.nativeBinaryLoadingListener = nativeBinaryLoadingListener;
    }

    public NativeBinaryLoadingListener getNativeBinaryLoadingListener() {
        return nativeBinaryLoadingListener;
    }

    public void setSystemFoundListener(SystemFoundListener systemFoundListener) {
        this.systemFoundListener = systemFoundListener;
    }

    public SystemFoundListener getSystemFoundListener() {
        return systemFoundListener;
    }

    /**
     * Initializes the platform-dependent native dynamic library.
     * 
     * @return this instance for chained invocations
     * @throws UnSupportedSystemError if the OS is not supported by jSnapLoader
     */
    public NativeBinaryLoader initPlatformLibrary() throws UnSupportedSystemError {
        final boolean[] isSystemFound = new boolean[] {false};
        // search for the compatible library using the predefined predicate
        // a predicate is a conditional statement composed of multiple propositions
        // representing the complete system variant (OS + ARCH + VM).
        registeredLibraries.forEach(nativeDynamicLibrary -> {
            if (isSystemFound[0]) {
                return;
            }
            // re-evaluate the library info part
            if (libraryInfo != null) {
                nativeDynamicLibrary.initWithLibraryInfo(libraryInfo);
            }
            if (nativeDynamicLibrary.getPredicate()) {
                this.nativeDynamicLibrary = nativeDynamicLibrary;
                isSystemFound[0] = true;
            }
        });

        // execute system found listeners
        if (systemFoundListener != null) {
            if (isSystemFound[0]) {
                systemFoundListener.onSystemFound(this, nativeDynamicLibrary);
            } else {
                systemFoundListener.onSystemNotFound(this);
                throw new UnSupportedSystemError(NativeVariant.OS_NAME.getProperty(),
                        NativeVariant.OS_ARCH.getProperty());
            }
        }
        return this;
    }

    /**
     * Extracts and loads the system and the architecture-specific library from the output jar to the [user.dir]
     * according to a loading criterion (incremental-load or clean-extract).
     * 
     * @param criterion the initial loading criterion, either {@link LoadingCriterion#INCREMENTAL_LOADING} or {@link LoadingCriterion#CLEAN_EXTRACTION}
     * @return this instance for chained invocations
     * @throws IOException if the library to extract is not present in the jar filesystem
     */
    public NativeBinaryLoader loadLibrary(LoadingCriterion criterion) throws IOException {
        if (criterion == LoadingCriterion.INCREMENTAL_LOADING && nativeDynamicLibrary.isExtracted()) {
            loadBinary(nativeDynamicLibrary);
            return this;
        }
        cleanExtractBinary(nativeDynamicLibrary);
        return this;
    }
    
    /**
     * Retrieves the native dynamic library object representing the library to extract and load.
     * 
     * @return an object representing the platform dependent native dynamic library
     */
    public NativeDynamicLibrary getNativeDynamicLibrary() {
        return nativeDynamicLibrary;
    }

    /**
     * Enables the logging for this object, default value is false.
     * 
     * @param loggingEnabled true to enable logging, false otherwise
     */
    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    /**
     * Tests the logging flag, default value is false.
     * 
     * @return true if the logging flag is enabled, false otherwise
     */
    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    /**
     * Enables the retry with clean extraction after a load failure, default value is false.
     * 
     * @param retryWithCleanExtraction true to enable the flag, false otherwise
     */
    public void setRetryWithCleanExtraction(boolean retryWithCleanExtraction) {
        this.retryWithCleanExtraction = retryWithCleanExtraction;
    }

    /**
     * Tests the retry with clean extraction flag, default value is false.
     * 
     * @return true if enabled, false otherwise
     */
    public boolean isRetryWithCleanExtraction() {
        return retryWithCleanExtraction;
    }

    /**
     * Loads a native binary using the platform dependent object, for android, 
     * the library is loaded by its basename (variant is managed internally by the android sdk).
     * 
     * @param library the platform-specific library to load
     * @throws IOException in case the binary to be extracted is not found on the specified jar
     */
    protected void loadBinary(NativeDynamicLibrary library) throws IOException {
        try {
            /* sanity check for android java vm (the dalvik) */
            if (NativeVariant.isAndroid()) {
                System.loadLibrary(libraryInfo.getBaseName());
                return;
            }
            System.load(library.getExtractedLibrary());
            log(Level.INFO, "loadBinary", "Successfully loaded library: " + library.getExtractedLibrary(), null);
            if (nativeBinaryLoadingListener != null) {
                nativeBinaryLoadingListener.onLoadingSuccess(this);
            }
        } catch (final UnsatisfiedLinkError error) {
            log(Level.SEVERE, "loadBinary", "Cannot load the dynamic library: " + library.getExtractedLibrary(), error);
            if (nativeBinaryLoadingListener != null) {
                nativeBinaryLoadingListener.onLoadingFailure(this);
            }
            /* Retry with clean extract */
            if (isRetryWithCleanExtraction()) {
                cleanExtractBinary(library);
                if (nativeBinaryLoadingListener != null) {
                    nativeBinaryLoadingListener.onRetryCriterionExecution(this);
                }
            }
        }
    }

    /**
     * Cleanly extracts and loads the native binary to the current [user.dir].
     * 
     * @param library the platform-specific library to extract and load
     * @throws IOException in case the binary to be extracted is not found on the specified jar or an 
     *                     interrupted I/O operation has occured
     */
    protected void cleanExtractBinary(NativeDynamicLibrary library) throws IOException {
        libraryExtractor = initializeLibraryExtractor(library);
        /* CLEAR RESOURCES AND RESET OBJECTS ON-EXTRACTION */
        libraryExtractor.setExtractionListener(new ExtractionListener() {
            @Override
            public void onExtractionCompleted(FileExtractor fileExtractor) {
                log(Level.INFO, "cleanExtractBinary", "Extracted successfully to " + library.getExtractedLibrary(), null);
                try {
                    loadBinary(library);
                } catch (IOException e) {
                    log(Level.SEVERE, "cleanExtractBinary", "Error while loading the binary!", e);
                }
            }

            @Override
            public void onExtractionFailure(FileExtractor fileExtractor, Throwable throwable) {
                log(Level.SEVERE, "cleanExtractBinary", "Extraction has failed!", throwable);
            }

            @Override
            public void onExtractionFinalization(FileExtractor fileExtractor, FileLocator fileLocator) {
                try {
                    fileLocator.close();
                    fileExtractor.close();
                } catch (IOException e) {
                    log(Level.SEVERE, "cleanExtractBinary", "Error while closing the resources!", e);
                }
            }
        });
        libraryExtractor.extract();
        libraryExtractor = null;
    }

    /**
     * Initializes a filesystem extrator object if the filesystem extractor object associated with this loader isnot defined.
     * 
     * @param library the native dynamic library to load 
     * @return a new FileExtractor object that represents an output stream provider
     * @throws IOException if the jar filesystem to be located is not found, or if the extraction destination is not found
     */
    protected FileExtractor initializeLibraryExtractor(NativeDynamicLibrary library) throws IOException {
        if (library.getJarPath() != null) {
            return new LibraryExtractor(library.getJarPath(), library.getCompressedLibrary(), library.getExtractedLibrary());
        }
        return new LibraryExtractor(library.getCompressedLibrary(), library.getExtractedLibrary());
    }

    /**
     * Log data with a level and a throwable (optional).
     * 
     * @param level the logger level
     * @param sourceMethod the source of this call
     * @param msg a string formatted message to display 
     * @param throwable optional param for error messages
     */
    protected void log(Level level, String sourceMethod, String msg, Throwable throwable) {
        if (!isLoggingEnabled()) {
            return;
        }
        if (throwable == null) {
            logger.logp(level, this.getClass().getName(), sourceMethod, msg);
        } else {
            logger.logp(level, this.getClass().getName(), sourceMethod, msg, throwable);
        }
    }
}