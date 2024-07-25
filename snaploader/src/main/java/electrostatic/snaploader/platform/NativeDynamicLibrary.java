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

package electrostatic.snaploader.platform;

import java.io.File;
import electrostatic.snaploader.LibraryInfo;
import electrostatic.snaploader.NativeBinaryLoader;
import electrostatic.snaploader.platform.util.NativeVariant;
import electrostatic.snaploader.platform.util.PlatformPredicate;
import electrostatic.snaploader.platform.util.PropertiesProvider;

/**
 * Represents a filesystem to a platform-specific binary inside
 * a compression with a predicate; if the predicate evaluates, the
 * library is loaded by the {@link NativeBinaryLoader}.
 * 
 * @author pavl_g
 */
public class NativeDynamicLibrary {

    /**
     * The absolute path for the Jar file to
     * extract and load the library from.
     */
    protected String jarPath;

    /**
     * The library path inside the compression (i.e., Jar file).
     */
    protected String libraryDirectory;

    /**
     * A designator for the library name with the platform extension
     * (basename + extension).
     */
    protected String library;

    /**
     * A designator for the extraction directory of the
     * native library.
     */
    protected String extractionDir;

    /**
     * The platform-specific predicate; that if evaluated as
     * true, the respective native library object will be
     * assigned by the {@link NativeBinaryLoader} to be extracted
     * and loaded.
     */
    protected PlatformPredicate platformPredicate;

    /**
     * Creates a Native dynamic library from a relative directory and a library filesystem.
     * 
     * @param platformDirectory the library directory inside the jar compression, "null" for the default library directory.
     * @param platformPredicate the predicate to test against; that if evaluated as true, the native library will be selected
     *                  to be loaded by the native loader
     */
    public NativeDynamicLibrary(String platformDirectory,
                                PlatformPredicate platformPredicate) {
        this.libraryDirectory = platformDirectory;
        this.platformPredicate = platformPredicate;
    }

    /**
     * Initializes the native dynamic library with the library info.
     *
     * @param libraryInfo wraps abstract data representing the native library
     */
    public void initWithLibraryInfo(LibraryInfo libraryInfo) {
        String ext = ".so";

        if (NativeVariant.Os.isMac()) {
            ext = ".dylib";
        } else if (NativeVariant.Os.isWindows()) {
            ext = ".dll";
        }

        /* Initializes the library basename */
        if (libraryInfo.getBaseName() != null) {
            library = "lib" + libraryInfo.getBaseName() + ext;
        }

        /* Initializes the library jar path to locate before extracting, "null" to use the classpath */
        jarPath = libraryInfo.getJarPath();

        /* Initializes the library with an extraction path, "null" to extract to the current user directory */
        extractionDir = libraryInfo.getExtractionDir();

        /* Initializes the library directory within the jar, "null" for the default library directory */
        if (libraryInfo.getDirectory() != null) {
            libraryDirectory = libraryInfo.getDirectory();
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
        return libraryDirectory + PropertiesProvider.ZIP_FILE_SEPARATOR.getSystemProperty() + library;
    }

    /**
     * Retrieves the absolute path for the native library as supposed to be on the extraction directory.
     * 
     * @return the absolute path composed of the extraction directory and the library name and system-specific extension
     */
    public String getExtractedLibrary() {
        if (extractionDir != null) {
            return extractionDir + PropertiesProvider.FILE_SEPARATOR.getSystemProperty() + library;
        }
        return PropertiesProvider.USER_DIR.getSystemProperty() + 
                    PropertiesProvider.FILE_SEPARATOR.getSystemProperty() + library;
    }

    /**
     * Tests whether the native library is extracted to the specified extraction directory.
     * 
     * @return true if the library has been extracted before, false otherwise
     */
    public boolean isExtracted() {
        return new File(getExtractedLibrary()).exists();
    }

    /**
     * Retrieves the platform-specific predicate to test against.
     *
     * @return the predefined platform-specific predicate object.
     */
    public PlatformPredicate getPlatformPredicate() {
        return platformPredicate;
    }
}
