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

import java.io.File;

/**
 * Represents a native binary domain with a {@link NativeDynamicLibrary#directory} and a {@link NativeDynamicLibrary#library}.
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
    private String directory;
    private String library;
    private String extractionDir;

    /**
     * Creates a Native dynamic library from a relative directory and a library file.
     * 
     * @param jarPath 
     * @param directory the relative path inside the jar file.
     * @param library the library filename.
     */
    NativeDynamicLibrary(String jarPath, String directory, String library, String extractionDir) {
        this.jarPath = jarPath;
        this.directory = directory;
        this.library = library;
        this.extractionDir = extractionDir;
    }

    public static void initWithLibraryInfo(LibraryInfo libraryInfo) {
        /* Initializes the library basename */
        if (libraryInfo.getBaseName() != null) {
            NativeDynamicLibrary.LINUX_x86.setLibrary("lib" + libraryInfo.getBaseName() + ".so");
            NativeDynamicLibrary.LINUX_x86_64.setLibrary("lib" + libraryInfo.getBaseName() + ".so");
            NativeDynamicLibrary.MAC_x86.setLibrary("lib" + libraryInfo.getBaseName() + ".dylib");
            NativeDynamicLibrary.MAC_x86_64.setLibrary("lib" + libraryInfo.getBaseName() + ".dylib");
            NativeDynamicLibrary.WIN_x86.setLibrary("lib" + libraryInfo.getBaseName() + ".dll");
            NativeDynamicLibrary.WIN_x86_64.setLibrary("lib" + libraryInfo.getBaseName() + ".dll");
        }
        
        /* Initializes the library jar path to locate before extracting, "null" to use the classpath */
        NativeDynamicLibrary.LINUX_x86.setJarPath(libraryInfo.getJarPath());
        NativeDynamicLibrary.LINUX_x86_64.setJarPath(libraryInfo.getJarPath());
        NativeDynamicLibrary.MAC_x86.setJarPath(libraryInfo.getJarPath());
        NativeDynamicLibrary.MAC_x86_64.setJarPath(libraryInfo.getJarPath());
        NativeDynamicLibrary.WIN_x86.setJarPath(libraryInfo.getJarPath());
        NativeDynamicLibrary.WIN_x86_64.setJarPath(libraryInfo.getJarPath());

        /* Initializes the library with an extraction path, "null" to extract to the current user directory */
        NativeDynamicLibrary.LINUX_x86.setExtractionDir(libraryInfo.getExtractionDir());
        NativeDynamicLibrary.LINUX_x86_64.setExtractionDir(libraryInfo.getExtractionDir());
        NativeDynamicLibrary.MAC_x86.setExtractionDir(libraryInfo.getExtractionDir());
        NativeDynamicLibrary.MAC_x86_64.setExtractionDir(libraryInfo.getExtractionDir());
        NativeDynamicLibrary.WIN_x86.setExtractionDir(libraryInfo.getExtractionDir());
        NativeDynamicLibrary.WIN_x86_64.setExtractionDir(libraryInfo.getExtractionDir());

        /* Initializes the library directory within the jar, "null" for the default library directory */
        if (libraryInfo.getDirectory() != null) {
            NativeDynamicLibrary.LINUX_x86.setDirectory(libraryInfo.getDirectory());
            NativeDynamicLibrary.LINUX_x86_64.setDirectory(libraryInfo.getDirectory());
            NativeDynamicLibrary.MAC_x86.setDirectory(libraryInfo.getDirectory());
            NativeDynamicLibrary.MAC_x86_64.setDirectory(libraryInfo.getDirectory());
            NativeDynamicLibrary.WIN_x86.setDirectory(libraryInfo.getDirectory());
            NativeDynamicLibrary.WIN_x86_64.setDirectory(libraryInfo.getDirectory());
        }
    } 

    public void setExtractionDir(String extractionDir) {
        this.extractionDir = extractionDir;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getJarPath() {
        return jarPath;
    }

    public String getExtractionDir() {
        return extractionDir;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getLibrary() {
        return library;
    }

    public String getAbsoluteLibraryLocation() {
        return directory + "/" + library;
    }

    /**
     * Retrieves the absolute path for the native library as supposed to be on the current user dir.
     * 
     * @param library the native library
     * @return the absolute path composed of the current user directory and the library name and system specific extension
     */
    public String getAbsoluteLibraryDirectory() {
        if (getExtractionDir() != null) {
            return getExtractionDir() + System.getProperty("file.separator") + this.getLibrary();
        }
        return System.getProperty("user.dir") + System.getProperty("file.separator") + this.getLibrary();
    }

    /**
     * Tests whether the native library is extracted to the current user dir.
     * 
     * @param library the native library
     * @return true if the library has been extracted before, false otherwise
     */
    public boolean isExtracted() {
        return new File(getAbsoluteLibraryDirectory()).exists();
    }
}
