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

import electrostatic4j.snaploader.platform.NativeDynamicLibrary;
import electrostatic4j.snaploader.throwable.UnSupportedSystemError;

/**
 * Provides executable functions binding the user applications to
 * the system detection lifecycle.
 * <p>
 * Note: All the functions on this interface are dispatched
 * by the {@link NativeBinaryLoader#loadLibrary(LoadingCriterion)}.
 *
 * @author pavl_g
 */
public interface SystemDetectionListener {

    /**
     * Dispatched when a system predicate is evaluated as true against the
     * system in this runtime.
     *
     * @param nativeBinaryLoader the dispatching loader.
     * @param nativeDynamicLibrary the native library object with an evaluated predicate.
     */
    void onSystemFound(NativeBinaryLoader nativeBinaryLoader, NativeDynamicLibrary nativeDynamicLibrary);

    /**
     * Dispatched when all the registered system predicates are evaluated as false
     * against the current system in this runtime. In this case, a {@link UnSupportedSystemError}
     * is also thrown on the user application environment.
     *
     * @param nativeBinaryLoader the dispatching loader
     */
    void onSystemNotFound(NativeBinaryLoader nativeBinaryLoader);
}
