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
package com.avrsandbox.snaploader.platform.util;

/**
 * Wraps objects for native variant constituents (OS + ARCH={CPU + INSTRUCT_SET} + VM).
 * 
 * @author pavl_g
 */
public enum NativeVariant {
    
    /**
     * The Operating system name property for this variant.
     */
    OS_NAME(System.getProperty("os.name")),
    
    /**
     * The Operating system architecture.
     */
    OS_ARCH(System.getProperty("os.arch")),
    
    /**
     * The current java virtual machine.
     */
    JVM(System.getProperty("java.vm.name"));
    
    private static final String Linux = "Linux";
    private static final String Windows = "Windows";
    private static final String Mac = "Mac";
    private static final String Dalvik = "Dalvik";

    private final String property;

    NativeVariant(final String property) {
        this.property = property;
    }

    /**
     * Tests whether the current system is a Linux.
     * 
     * @return true if the current OS is a Linux, false otherwise.
     */
    public static boolean isLinux() {
        return NativeVariant.OS_NAME.getProperty().contains(NativeVariant.Linux);
    }

    /**
     * Tests whether the current system is a Windows.
     * 
     * @return true if the current OS is a Windows, false otherwise.
     */
    public static boolean isWindows() {
        return NativeVariant.OS_NAME.getProperty().contains(NativeVariant.Windows);
    }

    /**
     * Tests whether the current system is a Mac.
     * 
     * @return true if the current OS is a Mac, false otherwise.
     */
    public static boolean isMac() {
        return NativeVariant.OS_NAME.getProperty().contains(NativeVariant.Mac);
    }

    /**
     * Tests whether the current system is an Android.
     * 
     * @return true if the current OS is an Android, false otherwise.
     */
    public static boolean isAndroid() {
        return JVM.getProperty().contains(NativeVariant.Dalvik);
    }

    /**
     * Tests whether the current system architecture is a 64-bit chipset.
     * 
     * @return true if the current OS architecture is a 64-bit chipset, false otherwise.
     */
    public static boolean isX86_64() {
        return OS_ARCH.getProperty().contains("64");
    }

    /**
     * Tests whether the current system architecture is a plain x86 chipset.
     * 
     * @return true if the current OS architecture is a plain x86 chipset, false otherwise.
     */
    public static boolean isX86() {
        return OS_ARCH.getProperty().contains("x86");
    }

    /**
     * Tests whether the current system architecture is a 32-bit chipset.
     *
     * @return true if the current OS architecture is a 32-bit chipset, false otherwise.
     */
    public static boolean is32() {
        return OS_ARCH.getProperty().contains("32");
    }

    /**
     * Tests whether the current CPU vendor is an AMD vendor (e.g., Intel Chipset).
     *
     * @return true if the current CPU vendor is an AMD vendor.
     */
    public static boolean isAMD() {
        return OS_ARCH.getProperty().contains("amd");
    }

    /**
     * Tests whether the current CPU vendor is an ARM vendor (e.g., Broadcom Chipset).
     *
     * @return true if the current CPU vendor is an ARM vendor.
     */
    public static boolean isARM() {
        return OS_ARCH.getProperty().contains("arm");
    }

    /**
     * Retrieves the data of this native variant property.
     * 
     * @return the specified property object in a string format.
     */
    public String getProperty() {
        return property;
    }
}
