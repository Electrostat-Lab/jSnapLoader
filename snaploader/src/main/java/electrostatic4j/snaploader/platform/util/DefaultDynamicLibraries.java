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

package electrostatic4j.snaploader.platform.util;

import electrostatic4j.snaploader.platform.NativeDynamicLibrary;

/**
 * Defines default helper objects for plug-and-play usage.
 * <p>
 * In order to add more predicated variants, extend
 * this namespace class and add more static objects.
 *
 * @author pavl_g
 */
public class DefaultDynamicLibraries {

    /**
     * Represents a linux x86 binary with 64-bit instruction set.
     */
    public static NativeDynamicLibrary LINUX_X86_64 =
            new NativeDynamicLibrary("lib/linux/x86-64", PlatformPredicate.LINUX_X86_64);

    /**
     * Represents a linux x86 binary with 32-bit instruction set.
     */
    public static NativeDynamicLibrary LINUX_X86 =
            new NativeDynamicLibrary("lib/linux/x86", PlatformPredicate.LINUX_X86);

    /**
     * Represents a mac x86 binary with 64-bit instruction set.
     */
    public static NativeDynamicLibrary MAC_X86_64 =
            new NativeDynamicLibrary("lib/macos/x86-64", PlatformPredicate.MACOS_X86_64);

    /**
     * Represents a mac x86 binary with 32-bit instruction set.
     */
    public static NativeDynamicLibrary MAC_X86 =
            new NativeDynamicLibrary("lib/macos/x86", PlatformPredicate.MACOS_X86);

    /**
     * Represents a windows x86 binary with 64-bit instruction set.
     */
    public static NativeDynamicLibrary WIN_X86_64 =
            new NativeDynamicLibrary("lib/windows/x86-64", PlatformPredicate.WIN_X86_64);

    /**
     * Represents a windows x86 binary with 32-bit instruction set.
     */
    public static NativeDynamicLibrary WIN_X86 =
            new NativeDynamicLibrary("lib/windows/x86", PlatformPredicate.WIN_X86);

    private DefaultDynamicLibraries() {
    }
}
