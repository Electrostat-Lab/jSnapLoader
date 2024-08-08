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

package electrostatic4j.snaploader;

import electrostatic4j.snaploader.filesystem.DirectoryPath;
import electrostatic4j.snaploader.platform.NativeDynamicLibrary;

/**
 * Provides a platform-independent library placeholder with an adjustable baseName {@link LibraryInfo#baseName}
 * grouping the common components among all platforms.
 * <p>
 * <b> Understand the API vision: </b>
 * The native dynamic API is composed of {@link NativeDynamicLibrary} which represents the
 * platform-dependent entity and the {@link LibraryInfo} which represents the abstract
 * platform-independent entity; the common components among them represent a backup
 * on the LibraryInfo side that the API will fall back to in case the platform directory
 * path is invalid.
 * <p>
 * <ul>
 * <li> ------------------------------
 * <li> (2).Abstract: LibraryInfo (Grouping common components among all platforms; thus platform-independent).
 * <li> ------------------------------
 * <li> (1).Concrete: NativeDynamicLibrary (Grouping platform-dependent components).
 * <li> ------------------------------
 * </ul>
 * <p>
 * where (1).(2) designates the runtime order.
 *
 * @author pavl_g
 */
public final class LibraryInfo {

    private DirectoryPath jarPath;
    private DirectoryPath directory;
    private String baseName;
    private DirectoryPath directoryPath;

    /**
     * Instantiates a library info data structure pointing to a library in the classpath.
     *
     * @param directory the platform-independent directory inside the compression used for locating the native dynamic library,
     *                  this is used as a backup directory path
     *                  in case the {@link NativeDynamicLibrary#getPlatformDirectory()} is not valid.
     * @param baseName the library basename, for example, 'lib-basename.so' (not null).
     * @param directoryPath the extraction destination path, {@link DirectoryPath#USER_DIR} for
     *                      a user working directory extraction path,
     *                      and {@link DirectoryPath#USER_HOME} for the user home (not null).
     */
    public LibraryInfo(DirectoryPath directory, String baseName, DirectoryPath directoryPath) {
        this(DirectoryPath.CLASS_PATH, directory, baseName, directoryPath);
    }

    /**
     * Instantiates a library info data structure pointing to a library in an external jar with jarPath.
     * 
     * @param jarPath a path to an external jar to locate the library inside, {@link DirectoryPath#CLASS_PATH} to assign the classpath routine
     *                to the {@link electrostatic4j.snaploader.filesystem.FileLocator} API (not null).
     * @param directory the platform-independent directory inside the compression used for locating the native dynamic library,
     *                  this is used as a backup directory path
     *                  in case the {@link NativeDynamicLibrary#getPlatformDirectory()} is not valid.
     * @param baseName the library basename, for example, 'lib-basename.so' (not null).
     * @param directoryPath the extraction destination path, {@link DirectoryPath#USER_DIR} for
     *                      a user working directory extraction path,
     *                      and {@link DirectoryPath#USER_HOME} for the user home (not null).
     */
    public LibraryInfo(DirectoryPath jarPath, DirectoryPath directory, String baseName, DirectoryPath directoryPath) {
        this.jarPath = jarPath;
        this.directory = directory;
        this.baseName = baseName;
        this.directoryPath = directoryPath;
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
     * @return the jar absolute filesystem path object.
     */
    public DirectoryPath getJarPath() {
        return jarPath;
    }

    /**
     * Retrieves the directory inside the compression used for locating the native dynamic library.
     * 
     * @return the path to the dynamic library filesystem inside the compression.
     */
    public DirectoryPath getDirectory() {
        return directory;
    }

    /**
     * Retrieves the extraction absolute directory.
     * 
     * @return the extraction destination path object.
     */
    public DirectoryPath getExtractionDirectory() {
        return directoryPath;
    }

    /**
     * Sets the absolute path to the jar filesystem to locate the native dynamic library to be
     * extracted and loaded, "null" to use the "classpath (the stock jar)"" to load the library filesystem.
     * 
     * @param jarPath the external jar path object to localize the compression, use {@link DirectoryPath#CLASS_PATH}
     *                to switch the file locator to the classpath routine.
     */
    public void setJarPath(DirectoryPath jarPath) {
        this.jarPath = jarPath;
    }

    /**
     * Sets the directory to the native dynamic library inside the jar compression, "null" to use
     * the default directories specified for each variant by {@link NativeBinaryLoader}.
     * 
     * @param directory the location to the native dynamic library inside the jar compression.
     */
    public void setDirectory(DirectoryPath directory) {
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
     * Sets the extraction directory used for extracting the native dynamic library.
     * 
     * @param directoryPath the extraction directory path to which the native-located library
     *                      will be extracted to.
     */
    public void setExtractionDirectory(DirectoryPath directoryPath) {
        this.directoryPath = directoryPath;
    }
}
