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

package electrostatic.snaploader.library;

import java.io.IOException;
import electrostatic.snaploader.filesystem.ConcurrentFileExtractor;
import electrostatic.snaploader.filesystem.FileExtractor;

/**
 * Represents a thread-safe dynamic library (.so, .dll, .dylib) extractor based on the {@link FileExtractor}.
 * 
 * @author pavl_g
 */
public class LibraryExtractor extends ConcurrentFileExtractor {

    /**
     * Instantiates a native dynamic library extractor with a jar path, library path and extract destination filesystem path.
     * 
     * @param jarPath an absolute path to the jar filesystem containing the library
     * @param libraryPath the path of the library inside the jar filesystem
     * @param destination the extraction destination filesystem path
     * @throws IOException if the jar filesystem to be located is not found, or if the extraction destination is not found
     */
    public LibraryExtractor(String jarPath, String libraryPath, String destination) throws IOException {
        super(new LibraryLocator(jarPath, libraryPath), destination);
    }

    /**
     * Instantiates a native dynamic library extractor with a library path and an extract destination filesystem path. This
     * object locates a dynamic native library inside the stock jar filesystem based on a classpath input stream.
     * 
     * @param libraryPath the path of the library inside the jar filesystem
     * @param destination the extraction destination filesystem path
     * @throws IOException if the jar filesystem to be located is not found, or if the extraction destination is not found
     */
    public LibraryExtractor(String libraryPath, String destination) throws IOException {
        super(new LibraryLocator(libraryPath), destination);
    }
}
