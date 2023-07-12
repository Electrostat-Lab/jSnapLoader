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
package com.avrsandbox.snaploader.library;

import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import com.avrsandbox.snaploader.file.ZipCompressionType;
import com.avrsandbox.snaploader.file.FileLocator;

/**
 * Locates a library inside a jar file, the probable source for the native dynamic libraries to extract and load.
 * 
 * @author pavl_g
 */
public class LibraryLocator extends FileLocator {
    
    /**
     * Locates the library inside the stock jar file. 
     * This object leaks an input stream.
     * 
     * @param libraryPath the path to the dynamic native library inside that jar file
     */
    public LibraryLocator(String libraryPath) {
        this.fileInputStream = LibraryLocator.class.getClassLoader().getResourceAsStream(libraryPath);
    } 

    /**
     * Locates a library inside an external jar, the external jar is defined by the means of a {@link JarFile} and 
     * the native library is defined as a {@link ZipEntry}. 
     * This object leaks an input stream.
     * 
     * @param directory the absolute path for the external jar file
     * @param libraryPath the path to the dynamic native library inside that jar file
     * @throws IOException if the jar to be located is not found or an interrupt I/O operation has occured
     */
    public LibraryLocator(String directory, String libraryPath) throws IOException {
        super(directory, libraryPath, ZipCompressionType.JAR);
    }
}
