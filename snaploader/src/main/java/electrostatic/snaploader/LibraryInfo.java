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

package electrostatic.snaploader;

import electrostatic.snaploader.platform.NativeDynamicLibrary;

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
     * Instantiates a library info object with a basename,
     * together with the current classpath, the platform directory
     * as the path to locate the native library, and the current
     * working directory as an extraction path.
     *
     * @param baseName the library basename without the extension
     */
    public LibraryInfo(String baseName) {
        this(null, null, baseName, null);
    }

    /**
     * Instantiates a library info object with a basename,
     * together with the current classpath, the platform directory
     * as the path to locate the native library, and a custom
     * user extraction path.
     *
     * @param baseName the library basename without the extension
     * @param extractionDir the path to a user-defined extraction directory
     */
    public LibraryInfo(String baseName, String extractionDir) {
        this(null, null, baseName, extractionDir);
    }

    /**
     * Instantiates a library info object with a basename,
     * together with the current classpath, a user-defined platform-independent
     * directory, and a custom user extraction path.
     *
     * @param directory the directory path to the binary inside the Jar file
     * @param baseName the library basename without the extension
     * @param extractionDir the path to a user-defined extraction directory
     */
    public LibraryInfo(String directory, String baseName, String extractionDir) {
        this(null, directory, baseName, extractionDir);
    }

    /**
     * Instantiates a library info data structure pointing to a library in an external jar with jarPath.
     * 
     * @param jarPath a path to an external jar to locate the library inside, "null" to use the project jar (classpath).
     * @param directory  the directory inside the compression used for locating the native dynamic library, "null" to use 
     *                   the default directory defined by {@link NativeDynamicLibrary}.
     * @param baseName the library basename, for example, 'lib-basename.so'.
     * @param extractionDir the extraction destination in absolute string format, "null" if the current [user.dir] is 
     *                      specified as the extraction directory
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
     * @return the library base-name in string (non-null)
     */
    public String getBaseName() {
        return baseName;
    }

    /**
     * Retrieves the jar filesystem path, the jar is the compression used to locate the native dynamic library to
     * be extracted and loaded by {@link NativeBinaryLoader}.
     * 
     * @return the jar absolute filesystem path in a string format, "null" if the classpath is specified instead of
     *         an external jar compression
     */
    public String getJarPath() {
        return jarPath;
    }

    /**
     * Retrieves the directory inside the compression used for locating the native dynamic library.
     * 
     * @return the path to the dynamic library filesystem inside the compression, "null" if the
     *         default variant-based directories are set to be used
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Retrieves the extraction absolute directory.
     * 
     * @return the extraction destination in absolute string format, "null" if the current [user.dir] is 
     *         specified as the extraction directory
     */
    public String getExtractionDir() {
        return extractionDir;
    }

    /**
     * Sets the absolute path to the jar filesystem to locate the native dynamic library to be
     * extracted and loaded, "null" to use the "classpath (the stock jar)"" to load the library filesystem.
     * 
     * @param jarPath the absolute path to the jar filesystem to locate the library to be extracted, "null" to
     *                use the "classpath" (aka. the stock jar)
     */
    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    /**
     * Sets the directory to the native dynamic library inside the jar compression, "null" to use
     * the default directories specified for each variant by {@link NativeBinaryLoader}.
     * 
     * @param directory the location to the native dynamic library inside the jar compression, "null"
     *                  to use the default variant-based directories
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Adjusts the library base name in a dynamic library name (lib-[basename]).
     * 
     * @param baseName a new base-name to assign (non-null)
     */
    public void setBaseName(final String baseName) {
        this.baseName = baseName;
    }

    /**
     * Sets the extraction directory used for extracting the native dynamic library in the 
     * form of an absolute directory, "null" to use the current [user.dir].
     * 
     * @param extractionDir the absolute extraction directory to which the located library
     *                      will be extracted to, "null" to set the extraction to the current [user.dir]
     */
    public void setExtractionDir(String extractionDir) {
        this.extractionDir = extractionDir;
    }
}
