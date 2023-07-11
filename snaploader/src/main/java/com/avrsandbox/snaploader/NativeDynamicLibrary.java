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

import java.io.File;

/**
 * Represents a native binary domain with a {@link NativeDynamicLibrary#libraryDirectory} and a {@link NativeDynamicLibrary#library}.
 * 
 * @apiNote Internal use only.
 * 
 * @author pavl_g
 */
public enum NativeDynamicLibrary {
    /**
     * Represents a linux x86 binary with 64-bit instruction set.
     */
    LINUX_x86_64(null, "lib/linux/x86-64", null, null),
    
    /**
     * Represents a linux x86 binary with 32-bit instruction set.
     */
    LINUX_x86(null, "lib/linux/x86", null, null),

    /**
     * Represents a mac x86 binary with 64-bit instruction set.
     */
    MAC_x86_64(null, "lib/macos/x86-64", null, null),

    /**
     * Represents a mac x86 binary with 32-bit instruction set.
     */
    MAC_x86(null, "lib/macos/x86", null, null),

    /**
     * Represents a windows x86 binary with 64-bit instruction set.
     */
    WIN_x86_64(null, "lib/windows/x86-64", null, null),

    /**
     * Represents a windows x86 binary with 32-bit instruction set.
     */
    WIN_x86(null, "lib/windows/x86", null, null);

    private String jarPath;
    private String libraryDirectory;
    private String library;
    private String extractionDir;
    protected static final String fileSeparator = System.getProperty("file.separator");
    protected static final String userdir = System.getProperty("user.dir");

    /**
     * Creates a Native dynamic library from a relative directory and a library file.
     * 
     * @param jarPath the absolute path to the jar compression
     * @param libraryDirectory the library directory inside the jar compression, "null" for the default library directory.
     * @param library the library filename
     * @param extractionDir the absolute path to the extraction directory
     */
    NativeDynamicLibrary(String jarPath, String libraryDirectory, String library, String extractionDir) {
        this.jarPath = jarPath;
        this.libraryDirectory = libraryDirectory;
        this.library = library;
        this.extractionDir = extractionDir;
    }

    /**
     * Initializes the native dynamic library with the library info.
     * 
     * @param libraryInfo wraps abstract data representing the native library
     */
    static void initWithLibraryInfo(LibraryInfo libraryInfo) {
        /* Initializes the library basename */
        if (libraryInfo.getBaseName() != null) {
            NativeDynamicLibrary.LINUX_x86.library = "lib" + libraryInfo.getBaseName() + ".so";
            NativeDynamicLibrary.LINUX_x86_64.library = "lib" + libraryInfo.getBaseName() + ".so";
            NativeDynamicLibrary.MAC_x86.library = "lib" + libraryInfo.getBaseName() + ".dylib";
            NativeDynamicLibrary.MAC_x86_64.library = "lib" + libraryInfo.getBaseName() + ".dylib";
            NativeDynamicLibrary.WIN_x86.library = "lib" + libraryInfo.getBaseName() + ".dll";
            NativeDynamicLibrary.WIN_x86_64.library = "lib" + libraryInfo.getBaseName() + ".dll";
        }
        
        /* Initializes the library jar path to locate before extracting, "null" to use the classpath */
        NativeDynamicLibrary.LINUX_x86.jarPath = libraryInfo.getJarPath();
        NativeDynamicLibrary.LINUX_x86_64.jarPath = libraryInfo.getJarPath();
        NativeDynamicLibrary.MAC_x86.jarPath = libraryInfo.getJarPath();
        NativeDynamicLibrary.MAC_x86_64.jarPath = libraryInfo.getJarPath();
        NativeDynamicLibrary.WIN_x86.jarPath = libraryInfo.getJarPath();
        NativeDynamicLibrary.WIN_x86_64.jarPath = libraryInfo.getJarPath();

        /* Initializes the library with an extraction path, "null" to extract to the current user directory */
        NativeDynamicLibrary.LINUX_x86.extractionDir = libraryInfo.getExtractionDir();
        NativeDynamicLibrary.LINUX_x86_64.extractionDir = libraryInfo.getExtractionDir();
        NativeDynamicLibrary.MAC_x86.extractionDir = libraryInfo.getExtractionDir();
        NativeDynamicLibrary.MAC_x86_64.extractionDir = libraryInfo.getExtractionDir();
        NativeDynamicLibrary.WIN_x86.extractionDir = libraryInfo.getExtractionDir();
        NativeDynamicLibrary.WIN_x86_64.extractionDir = libraryInfo.getExtractionDir();

        /* Initializes the library directory within the jar, "null" for the default library directory */
        if (libraryInfo.getDirectory() != null) {
            NativeDynamicLibrary.LINUX_x86.libraryDirectory = libraryInfo.getDirectory();
            NativeDynamicLibrary.LINUX_x86_64.libraryDirectory = libraryInfo.getDirectory();
            NativeDynamicLibrary.MAC_x86.libraryDirectory = libraryInfo.getDirectory();
            NativeDynamicLibrary.MAC_x86_64.libraryDirectory = libraryInfo.getDirectory();
            NativeDynamicLibrary.WIN_x86.libraryDirectory = libraryInfo.getDirectory();
            NativeDynamicLibrary.WIN_x86_64.libraryDirectory = libraryInfo.getDirectory();
        }
    } 

    /**
     * Retrieves the absolute path for the jar compression as specified by the {@link LibraryInfo} API.
     * 
     * @return a string representing the absolute path to the jar compression containing the dynamic libraries
     */
    public String getJarPath() {
        return jarPath;
    }

    /**
     * Retrieves the native library directory inside the jar compression.
     * 
     * @return a string representing the location of the native dynamic library to be loaded
     */
    public String getLibraryDirectory() {
        return libraryDirectory;
    }
    
    /**
     * Retrieves the library path within the jar compression.
     * 
     * @return a string representing the library path within the jar compression
     */
    public String getCompressedLibrary() {
        return libraryDirectory + fileSeparator + library;
    }

    /**
     * Retrieves the absolute path for the native library as supposed to be on the extraction directory.
     * 
     * @return the absolute path composed of the extraction directory and the library name and system specific extension
     */
    public String getExtractedLibrary() {
        if (extractionDir != null) {
            return extractionDir + fileSeparator + library;
        }
        return userdir + fileSeparator + library;
    }

    /**
     * Tests whether the native library is extracted to the specified extraction directory.
     * 
     * @return true if the library has been extracted before, false otherwise
     */
    public boolean isExtracted() {
        return new File(getExtractedLibrary()).exists();
    }
}
