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
package com.avrsandbox.snaploader.examples;

import java.io.IOException;
import com.avrsandbox.snaploader.LibraryInfo;
import com.avrsandbox.snaploader.NativeBinaryLoader;
import com.avrsandbox.snaploader.platform.NativeVariant;
import com.avrsandbox.snaploader.LoadingCriterion;

/**
 * Tests basic features of the {@link NativeBinaryLoader} API.
 * 
 * @author pavl_g
 */
public final class TestBasicFeatures {

    protected static final String userdir = System.getProperty("user.dir");
    protected static final String fileSeparator = System.getProperty("file.separator");
    protected static final String jar = "jme3-alloc-desktop-1.0.0-pre-gamma-2.jar";
    protected static final String libraryBasename = "jmealloc";

    protected static final String libraryAbsolutePath = userdir + fileSeparator + "libs";
    protected static final String jarFile = libraryAbsolutePath + fileSeparator + jar;
    protected static final LibraryInfo libraryInfo = new LibraryInfo(jarFile, null, libraryBasename, libraryAbsolutePath);

    protected static final String finalAbsolutePath = libraryAbsolutePath + fileSeparator + "libjmealloc.so";

    protected static NativeBinaryLoader loader;

    public static void main(String[] args) throws IOException {
        if (loader == null) {
            loader = new NativeBinaryLoader(libraryInfo).initPlatformLibrary();
        }
        loader.setLoggingEnabled(true);
        loader.setRetryWithCleanExtraction(true);
        /* Native dynamic library properties */
        printDetails(loader);
        loader.loadLibrary(LoadingCriterion.CLEAN_EXTRACTION);
    }

    protected static void printDetails(NativeBinaryLoader loader) {
        System.out.println("--------------------------------------------------------------");
        System.out.println("OS: " + NativeVariant.NAME.getProperty());
        System.out.println("ARCH: " + NativeVariant.ARCH.getProperty());
        System.out.println("VM: " + NativeVariant.VM.getProperty());
        System.out.println("--------------------------------------------------------------");
        System.out.println("Jar Path: " + loader.getNativeDynamicLibrary().getJarPath());
        System.out.println("Library Directory: " + loader.getNativeDynamicLibrary().getLibraryDirectory());
        System.out.println("Compressed library path: " + loader.getNativeDynamicLibrary().getCompressedLibrary());
        System.out.println("Extracted library absolute path: " + loader.getNativeDynamicLibrary().getExtractedLibrary());
        System.out.println("Is Extracted: " + loader.getNativeDynamicLibrary().isExtracted()); 
        System.out.println("--------------------------------------------------------------");
    }
}