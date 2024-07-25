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
package com.avrsandbox.snaploader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import com.avrsandbox.snaploader.platform.NativeDynamicLibrary;

/**
 * A thread-safe implementation for the NativeBinaryLoader.
 * 
 * @author pavl_g
 */
public class ConcurrentNativeBinaryLoader extends NativeBinaryLoader {

    /**
     * The monitor object.
     */
    protected final ReentrantLock lock = new ReentrantLock();

    /**
     * Instantiates a thread-safe {@link NativeBinaryLoader} object.
     * 
     * @param libraryInfo a data structure object holding the platform independent data for the library to load
     */
    public ConcurrentNativeBinaryLoader(final List<NativeDynamicLibrary> registeredLibraries, final LibraryInfo libraryInfo) {
        super(registeredLibraries, libraryInfo);
    }
    
    @Override
    protected void cleanExtractBinary(NativeDynamicLibrary library) throws IOException {
        try {
            /* CRITICAL SECTION STARTS */
            lock.lock();
            super.cleanExtractBinary(library);
        } finally {
            lock.unlock();
            /* CRITICAL SECTION ENDS */
        }
    }
}