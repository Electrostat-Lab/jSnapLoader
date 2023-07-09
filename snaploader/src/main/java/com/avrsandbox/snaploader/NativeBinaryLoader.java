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
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.UnsatisfiedLinkError;
import com.avrsandbox.snaploader.library.LibraryLocator;

/**
 * Helper utility for loading native binaries.
 * 
 * @author pavl_g.
 */
public final class NativeBinaryLoader {
    
    private static final Logger logger = Logger.getLogger(NativeBinaryLoader.class.getName());
    private final ReentrantLock lock = new ReentrantLock();
    private final LibraryInfo libraryInfo;
    private final int EOF = -1;
    private boolean enabled = true;

    public NativeBinaryLoader(LibraryInfo libraryInfo) {
        this.libraryInfo = libraryInfo;
    }

    /**
     * Extracts and loads the variant specific binary from the output jar, handling the error messages, 
     * guarded by the {@link NativeBinaryLoader#isEnabled()}.
     */
    public void loadLibraryIfEnabled() {
        if (!isEnabled()) {
            logger.log(Level.WARNING, "Stock NativeBinaryLoader is not enabled!");
            return;
        }
        try {
            /* extracts and loads the system specific library */
            NativeDynamicLibrary.initWithLibraryInfo(libraryInfo);
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

    /**
     * Adjusts the {@link NativeBinaryLoader#enabled} flag to enable/disable the {@link NativeBinaryLoader#loadLibraryIfEnabled()}.
     * Default value is [true].
     * 
     * @param enabled true to enable the {@link NativeBinaryLoader#loadLibraryIfEnabled()}, false otherwise.
     * @see NativeBinaryLoader#loadLibraryIfEnabled()
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Tests whether the method {@link NativeBinaryLoader#loadLibraryIfEnabled()} can load the native binary. 
     * Default value is [true].
     * 
     * @return true if the method {@link NativeBinaryLoader#loadLibraryIfEnabled()} is enabled to load the native binary.
     * @see NativeBinaryLoader#loadLibraryIfEnabled()
     */
    public boolean isEnabled() {
        return enabled;
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
            System.load(library.getAbsoluteLibraryDirectory());
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
        if (library.isExtracted()) {
            loadBinary(library, RetryCriteria.RETRY_WITH_CLEAN_EXTRACTION);
            return;
        }
        cleanExtractBinary(library);
    }

    /**
     * Cleanly extracts and loads the native binary to the current [user.dir].
     * 
     * @params library the library to extract and load
     * @throws IOException in case the binary to be extracted is not found on the specified jar
     */
    private void cleanExtractBinary(NativeDynamicLibrary library) throws IOException {
        /* CRITICAL SECTION STARTS */
        lock.lock();
        InputStream libraryStream = getLibraryInputStream(library);
        FileOutputStream fos = getLibraryOutputStream(library);
        try {
            /* Extracts the shipped native files */
            final byte[] buffer = new byte[libraryStream.available()];
            for (int bytes = 0; bytes != EOF; bytes = libraryStream.read(buffer)) {
                /* use the bytes as the buffer length to write valid data */
                fos.write(buffer, 0, bytes);
            }
        } finally {
            /* Releases resources */
            libraryStream.close();
            fos.close();
            libraryStream = null;
            fos = null;
            /* Loads native binaries */
            loadBinary(library, RetryCriteria.RETRY_WITH_CLEAN_EXTRACTION);
            lock.unlock();
            /* CRITICAL SECTION ENDS */
        }
    }

    private InputStream getLibraryInputStream(NativeDynamicLibrary library) throws IOException {
        /* Locates the library in an external jar file */
        if (library.getJarPath() != null) {
            return new LibraryLocator(library.getJarPath(), library.getAbsoluteLibraryLocation()).getLibraryInputStream();
        } 
        /* Defaulted to extract from stock jar file */
        return new LibraryLocator(library.getAbsoluteLibraryLocation()).getLibraryInputStream();
    }

    private FileOutputStream getLibraryOutputStream(NativeDynamicLibrary library) throws IOException {
        return new FileOutputStream(library.getAbsoluteLibraryDirectory());  
    }
}