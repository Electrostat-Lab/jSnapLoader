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

package electrostatic4j.snaploader.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread-safe implementation of the filesystem extractor API.
 * 
 * @author pavl_g
 */
public class ConcurrentFileExtractor extends FileExtractor {
    
    /**
     * An Object that controls the monitor.
     */
    protected final ReentrantLock lock = new ReentrantLock();

    /**
     * Instantiates a thread-safe filesystem extractor instance.
     * 
     * @param fileLocator locates a filesystem inside a zip compression
     * @param destination an absolute filesystem path representing the extraction destination filesystem
     * @throws FileNotFoundException if the destination filesystem path is not found
     */
    public ConcurrentFileExtractor(FileLocator fileLocator, String destination) throws FileNotFoundException {
        super(fileLocator, destination);
    }

    /**
     * Instantiates an empty filesystem extractor instance.
     */
    protected ConcurrentFileExtractor() { 
        super();
    }

    @Override
    public void extract() throws IOException {
        try {
            /* CRITICAL SECTION STARTS */
            lock.lock();
            super.extract();
        } finally {
            lock.unlock();
            /* CRITICAL SECTION ENDS */
        }
    }
}
