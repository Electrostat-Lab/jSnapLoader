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
import com.avrsandbox.snaploader.file.FileExtractor;
import com.avrsandbox.snaploader.file.FileLocator;
import com.avrsandbox.snaploader.file.ZipCompressionType;

/**
 * Tests extracting an image compression from a Zip compression type file using {@link FileExtractor} API.
 * 
 * @author pavl_g
 */
public class TestZipExtractor {
    
    protected static final String directory = TestBasicFeatures.libraryAbsolutePath + TestBasicFeatures.fileSeparator + "jmelogo700.zip";
    protected static final String destination = TestBasicFeatures.libraryAbsolutePath + TestBasicFeatures.fileSeparator + "jmelogo700.png";

    public static void main(String[] args) throws IOException {
        /* Locates the image inside the Zip Compression */
        final FileLocator fileLocator = new FileLocator(directory, "jmelogo700.png", ZipCompressionType.ZIP);
        /* Extracts the image file from the Zip Compression */
        final FileExtractor fileExtractor = new FileExtractor(fileLocator, destination);
        fileExtractor.extract();
    }
}
