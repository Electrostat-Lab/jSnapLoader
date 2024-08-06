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

package electrostatic4j.snaploader.examples;

import electrostatic4j.snaploader.LibraryInfo;
import electrostatic4j.snaploader.NativeBinaryLoader;
import electrostatic4j.snaploader.filesystem.DirectoryPath;
import electrostatic4j.snaploader.platform.util.DefaultDynamicLibraries;
import electrostatic4j.snaploader.platform.NativeDynamicLibrary;
import electrostatic4j.snaploader.platform.util.NativeVariant;
import electrostatic4j.snaploader.platform.util.PlatformPredicate;
import electrostatic4j.snaploader.platform.util.PropertiesProvider;
import electrostatic4j.snaploader.LoadingCriterion;

/**
 * Tests basic features of the {@link NativeBinaryLoader} API.
 * 
 * @author pavl_g
 */
public final class TestBasicFeatures {
                                                     
    protected static final LibraryInfo libraryInfo = new LibraryInfo(getJarFilePath(),
                                                                     "lib/placeholder",
                                                                     getLibraryBaseName(), 
                                                                     getLibrariesAbsolutePath());

    protected static NativeBinaryLoader loader;

    public static final NativeDynamicLibrary[] libraries = new NativeDynamicLibrary[] {
            DefaultDynamicLibraries.LINUX_X86,
            DefaultDynamicLibraries.LINUX_X86_64,
            new NativeDynamicLibrary("lib/windows/x86", "libjmealloc.dll", PlatformPredicate.WIN_X86),
            new NativeDynamicLibrary("lib/windows/x86-64", "libjmealloc.dll", PlatformPredicate.WIN_X86_64),
            DefaultDynamicLibraries.MAC_X86,
            DefaultDynamicLibraries.MAC_X86_64,
    };

    public static void main(String[] args) throws Exception {
        if (loader == null) {
            loader = new NativeBinaryLoader(libraryInfo);
        }
        loader.registerNativeLibraries(libraries).initPlatformLibrary();
        loader.setLoggingEnabled(true);
        loader.setRetryWithCleanExtraction(true);
        /* Native dynamic library properties */
        printDetails(loader);
        loader.loadLibrary(LoadingCriterion.INCREMENTAL_LOADING);
    }

    protected static void printDetails(NativeBinaryLoader loader) {
        System.out.println("--------------------------------------------------------------");
        System.out.println("OS: " + NativeVariant.OS_NAME.getProperty());
        System.out.println("ARCH: " + NativeVariant.OS_ARCH.getProperty());
        System.out.println("VM: " + NativeVariant.JVM.getProperty());
        System.out.println("--------------------------------------------------------------");
        System.out.println("Jar Path: " + loader.getNativeDynamicLibrary().getJarPath());
        System.out.println("Library Directory: " + loader.getNativeDynamicLibrary().getPlatformDirectory());
        System.out.println("Compressed library path: " + loader.getNativeDynamicLibrary().getCompressedLibrary());
        System.out.println("Extracted library absolute path: " + loader.getNativeDynamicLibrary().getExtractedLibrary());
        System.out.println("Is Extracted: " + loader.getNativeDynamicLibrary().isExtracted()); 
        System.out.println("--------------------------------------------------------------");
    }

    protected static DirectoryPath getLibrariesAbsolutePath() {
        return new DirectoryPath(PropertiesProvider.USER_DIR.getSystemProperty(), "libs");
    }

    protected static String getJarFilePath() {
        return getLibrariesAbsolutePath().getPath() +
                    PropertiesProvider.FILE_SEPARATOR.getSystemProperty() + getJarFile();
    }

    protected static String getNativeDynamicLibraryPath() {
        return getLibrariesAbsolutePath() + 
                    PropertiesProvider.FILE_SEPARATOR.getSystemProperty() + 
                    "lib" + getLibraryBaseName() + ".so";
    }

    protected static String getJarFile() {
        return "jme3-alloc-desktop-1.0.0-pre-gamma-2.jar";
    }

    protected static String getLibraryBaseName() {
        return "jmealloc";
    }
}