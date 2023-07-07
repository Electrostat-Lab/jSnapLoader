/*
 * Copyright (c) 2009-2023 jMonkeyEngine
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

/**
 * Represents a native binary domain with a {@link NativeDynamicLibrary#directory} and a {@link NativeDynamicLibrary#library}.
 * 
 * @author pavl_g
 */
public enum NativeDynamicLibrary {
    /**
     * Represents a linux x86 binary with 64-bit instruction set.
     */
    LINUX_x86_64("lib/linux/x86-64", "lib" + LibraryInfo.LIBRARY.getBaseName() + ".so"),
    
    /**
     * Represents a linux x86 binary with 32-bit instruction set.
     */
    LINUX_x86("lib/linux/x86", "lib" + LibraryInfo.LIBRARY.getBaseName() + ".so"),

    /**
     * Represents a mac x86 binary with 64-bit instruction set.
     */
    MAC_x86_64("lib/macos/x86-64", "lib" + LibraryInfo.LIBRARY.getBaseName() + ".dylib"),

    /**
     * Represents a mac x86 binary with 32-bit instruction set.
     */
    MAC_x86("lib/macos/x86", "lib" + LibraryInfo.LIBRARY.getBaseName() + ".dylib"),

    /**
     * Represents a windows x86 binary with 64-bit instruction set.
     */
    WIN_x86_64("lib/windows/x86-64", "lib" + LibraryInfo.LIBRARY.getBaseName() + ".dll"),

    /**
     * Represents a windows x86 binary with 32-bit instruction set.
     */
    WIN_x86("lib/windows/x86", "lib" + LibraryInfo.LIBRARY.getBaseName() + ".dll");

    private final String library;
    private final String directory;

    /**
     * Creates a Native dynamic library from a relative directory and a library file.
     * 
     * @param directory the relative path inside the jar file.
     * @param library the library filename.
     */
    NativeDynamicLibrary(final String directory, final String library) {
        this.directory = directory;
        this.library = library;
    }

    /**
     * Retrieves the directory to which the native binary is located.
     * 
     * @return the directory in a string format
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Retrieves the binary name.
     * 
     * @return the binary file-name in a string format
     */
    public String getLibrary() {
        return library;
    }

    /**
     * Retrieves the full path of the binary inside the jar file.
     * 
     * @return the absolute (full path) starting from the root directory represented by the jar file
     */
    public String getAbsoluteLibraryLocation() {
        return directory + "/" + library;
    }
}
