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

package electrostatic.snaploader.platform.util;

/**
 * Wraps a platform-specific predicate; that if all of its propositions evaluated
 * as true, the respective platform library will be assigned to be extracted
 * and loaded by the loader object in-command.
 *
 * @author pavl_g
 */
public final class PlatformPredicate {

    public static final PlatformPredicate LINUX_X86 = new PlatformPredicate(NativeVariant.isLinux() && NativeVariant.isX86());
    public static final PlatformPredicate LINUX_X86_64 = new PlatformPredicate(NativeVariant.isLinux() && NativeVariant.isAMD() && NativeVariant.is64());
    public static final PlatformPredicate LINUX_ARM_32 = new PlatformPredicate(NativeVariant.isLinux() && NativeVariant.isARM() && NativeVariant.is32());
    public static final PlatformPredicate LINUX_ARM_64 = new PlatformPredicate(NativeVariant.isLinux() && NativeVariant.isARM() && NativeVariant.is64());
    public static final PlatformPredicate MACOS_X86 = new PlatformPredicate(NativeVariant.isMac() && NativeVariant.isX86());
    public static final PlatformPredicate MACOS_X86_64 = new PlatformPredicate(NativeVariant.isMac() && NativeVariant.isAMD() && NativeVariant.is64());
    public static final PlatformPredicate MACOS_ARM_32 = new PlatformPredicate(NativeVariant.isMac() && NativeVariant.isARM() && NativeVariant.is32());
    public static final PlatformPredicate MACOS_ARM_64 = new PlatformPredicate(NativeVariant.isMac() && NativeVariant.isARM() && NativeVariant.is64());
    public static final PlatformPredicate WIN_X86 = new PlatformPredicate(NativeVariant.isWindows() && NativeVariant.isX86());
    public static final PlatformPredicate WIN_X86_64 = new PlatformPredicate(NativeVariant.isWindows() && NativeVariant.isAMD() && NativeVariant.is64());
    public static final PlatformPredicate WIN_ARM_32 = new PlatformPredicate(NativeVariant.isWindows() && NativeVariant.isARM() && NativeVariant.is32());
    public static final PlatformPredicate WIN_ARM_64 = new PlatformPredicate(NativeVariant.isWindows() && NativeVariant.isARM() && NativeVariant.is64());

    private final boolean predicate;

    /**
     * Instantiates a platform-specific predicate object
     * that wraps a predicate composed of multiple
     * propositions appended by logical operations.
     *
     * @param predicate a raw boolean predicate to evaluate against
     */
    public PlatformPredicate(boolean predicate) {
        this.predicate = predicate;
    }

    /**
     * Evaluate the propositions of the predefined platform-predicate.
     *
     * @return true if the
     */
    public boolean evaluatePredicate() {
        return predicate;
    }
}
