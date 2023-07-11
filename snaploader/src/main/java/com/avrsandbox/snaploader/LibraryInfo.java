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

/**
 * Provides a library placeholder with an adjustable baseName {@link LibraryInfo#baseName}.
 * 
 * @author pavl_g
 */
public final class LibraryInfo {

    private String jarPath;
    private String directory;
    private String baseName;
    private String extractionDir;

    /**
     * Instantiates a library info data structure pointing to a library in an external jar with jarPath.
     * 
     * @param jarPath a path to an external jar to locate the library inside, "null" to use the project jar (classpath).
     * @param baseName the library basename, for example: 'lib-basename.so'.
     */
    public LibraryInfo(String jarPath, String directory, String baseName, String extractionDir) {
        this.jarPath = jarPath;
        this.directory = directory;
        this.baseName = baseName;
        this.extractionDir = extractionDir;
    }

    /**
     * Retrieves the dynamic library basename.
     * 
     * @return the library base-name in string
     */
    public String getBaseName() {
        return baseName;
    }

    public String getJarPath() {
        return jarPath;
    }

    public String getDirectory() {
        return directory;
    }

    public String getExtractionDir() {
        return extractionDir;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Adjusts the library base name in a dynamic library name (lib-[basename]).
     * 
     * @param baseName a new base-name to assign
     */
    public void setBaseName(final String baseName) {
        this.baseName = baseName;
    }

    public void setExtractionDir(String extractionDir) {
        this.extractionDir = extractionDir;
    }
}
