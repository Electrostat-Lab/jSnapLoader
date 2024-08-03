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
 * Provides executable functions ensuring tight binding the user applications to
 * the extraction lifecycle.
 * <p>
 * Note: All the functions on this interface are dispatched
 * by the {@link FileExtractor#extract()}.
 * <p>
 * Warning: this listener interface is an essential component of
 * developing custom system loaders; as it requires freeing the native stream resources
 * manually through the {@link FileExtractionListener#onExtractionFinalization(FileExtractor, FileLocator)}.
 * If not freeing the resources with this interface was attained, then a try-with resources could be used.
 *
 * @author pavl_g
 */
public interface FileExtractionListener {

    /**
     * Dispatched when the extraction process is completed.
     *
     * @param fileExtractor the extractor in-command.
     */
    void onExtractionCompleted(FileExtractor fileExtractor);

    /**
     * Dispatched when the extraction process has failed with a throwable
     * component.
     *
     * @param fileExtractor the extractor in-command.
     * @param throwable the throwable captured from the FileExtractor API.
     */
    void onExtractionFailure(FileExtractor fileExtractor, Throwable throwable);

    /**
     * Dispatched when the extraction process is finalized, at this point, manually
     * freeing active resources should be attained.
     *
     * @param fileExtractor the extractor in-command.
     * @param fileLocator the file locator used by the extractor to locate files inside compressions.
     */
    void onExtractionFinalization(FileExtractor fileExtractor, FileLocator fileLocator);
}
