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

package electrostatic.snaploader.examples;

import electrostatic.snaploader.filesystem.FileExtractionListener;
import electrostatic.snaploader.filesystem.FileExtractor;
import electrostatic.snaploader.filesystem.FileLocator;
import electrostatic.snaploader.filesystem.ZipCompressionType;
import electrostatic.snaploader.platform.util.PropertiesProvider;
import electrostatic.snaploader.util.SnapLoaderLogger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Testing impacts of memory leaks, test this using jconsole.
 *
 * @author pavl_g
 */
public class TestFilesystemMemoryLeak {
    public static void main(String[] args) throws IOException {
        /* Locates the image inside the Zip Compression */
        SnapLoaderLogger.setLoggingEnabled(true);
        final FileLocator fileLocator = new FileLocator(getZipAbsolutePath(), getFilePath(), ZipCompressionType.ZIP);
        /* Extracts the image filesystem from the Zip Compression */
        final FileExtractor fileExtractor = new FileExtractor(fileLocator, getExtractionPath());
        fileLocator.initialize(0);
        fileExtractor.initialize(0);
        /* CLOSE/CLEAR I/O Resources */
        fileExtractor.setExtractionListener(new FileExtractionListener() {
            @Override
            public void onExtractionCompleted(FileExtractor fileExtractor) {

            }

            @Override
            public void onExtractionFailure(FileExtractor fileExtractor, Throwable throwable) {

            }

            @Override
            public void onExtractionFinalization(FileExtractor fileExtractor, FileLocator fileLocator) {
                Logger.getLogger(TestZipExtractor.class.getName())
                        .log(Level.INFO, "Resources Leak!");
            }
        });
        fileExtractor.extract();
    }

    protected static String getZipAbsolutePath() {
        return TestBasicFeatures.getLibrariesAbsolutePath() +
                PropertiesProvider.FILE_SEPARATOR.getSystemProperty() + "jmelogo700.zip";
    }

    protected static String getExtractionPath() {
        return TestBasicFeatures.getLibrariesAbsolutePath() +
                PropertiesProvider.FILE_SEPARATOR.getSystemProperty() + getFilePath();
    }

    protected static String getFilePath() {
        return "jmelogo700.png";
    }
}
