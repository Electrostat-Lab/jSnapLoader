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

package electrostatic.snaploader.filesystem;

/**
 * Provides executable functions by the file locator API
 * to enable the binding of user applications with the file locator interface.
 *
 * @author pavl_g
 * @see FileExtractor#extract()
 */
public interface FileLocalizingListener {

    /**
     * Dispatched whenever the file input stream is valid, hence the specified
     * file can be localized.
     *
     * @param locator the associated file locator object.
     */
    void onFileLocalizationSuccess(FileLocator locator);

    /**
     * Dispatched whenever the file input stream is invalid, hence the specified
     * file cannot be localized for the extraction process.
     *
     * @param locator the associated file locator object.
     * @param throwable the throwable object, typically indicating a failure
     *                  to locate the file inside the compression.
     */
    void onFileLocalizationFailure(FileLocator locator, Throwable throwable);
}
