/*
 * Copyright (c) 2023-2025, The Electrostatic-Sandbox Distributed Simulation Framework, jSnapLoader
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

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Wraps objects for native variant constituents (OS + ARCH={CPU + INSTRUCT_SET} + VM).
 *
 * <p>
 * Use the following list to build your platform predicates:
 * <ul>
 * <li> x86: 32-bit x86 architecture </li>
 * <li> x86_64: 64-bit x86 architecture, often referred to as amd64 </li>
 * <li> amd64: Another name for x86_64 </li>
 * <li> i386: Another designation for 32-bit x86 architecture </li>
 * <li> arm: ARM architecture </li>
 * <li> aarch64: 64-bit ARM architecture </li>
 * <li> sparc: SPARC architecture </li>
 * <li> sparcv9: 64-bit SPARC architecture </li>
 * <li> ppc: PowerPC architecture </li>
 * <li> ppc64: 64-bit PowerPC architecture </li>
 * <li> ppc64le: 64-bit PowerPC architecture, little-endian </li>
 * <li> s390: IBM System/390 architecture </li>
 * <li> s390x: 64-bit IBM System/390 architecture </li>
 * <li> riscv32: 32-bit RISC-V architecture </li>
 * <li> riscv64: 64-bit RISC-V architecture </li>
 * </ul>
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
    
    private static final String Linux = "linux";
    private static final String Windows = "windows";
    private static final String Mac = "mac";
    private static final String WebOs = "webos";
    private static final String Web0s = "web0s";
    private static final String Ios = "ios";
    private static final String Iphone = "iphone";
    private static final String Dalvik = "dalvik";

    private final String property;

    NativeVariant(final String property) {
        this.property = property;
    }

    /**
     * A namespace class exposing the Operating System Propositions.
     */
    public static final class Os {
        private Os() {
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
         * Tests whether the current system runtime is Desktop. A
         * system runtime is said to be desktop if it's not an Android,
         * and not Web-OS, and not iOS.
         *
         * @return true if the current runtime is a desktop binary, false otherwise.
         */
        public static boolean isDesktop() {
            return !isAndroid() && !isWebOs() && !isIos();
        }

        /**
         * Contemplated to test whether the current system runtime is
         * a web os.
         *
         * @return true if web-os; false otherwise. Current value is constantly false.
         */
        public static boolean isWebOs() {
            return NativeVariant.OS_NAME.getProperty()
                    .contains(NativeVariant.WebOs) || NativeVariant.OS_NAME.getProperty()
                    .contains(NativeVariant.Web0s);
        }

        /**
         * Contemplated to test whether the current system runtime is
         * an iOS os.
         *
         * @return true if iOS; false otherwise. Current value is constantly false.
         */
        public static boolean isIos() {
            return NativeVariant.OS_NAME.getProperty()
                    .contains(NativeVariant.Ios) || NativeVariant.OS_NAME.getProperty().contains(NativeVariant.Iphone);
        }
    }

    /**
     * A namespace class exposing the CPU propositions.
     */
    public static final class Cpu {
        /**
         * named CPU features that were detected by the OSHI library
         */
        private static Collection<String> presentFeatures;
        /**
         * serialize access to presentFeatures
         */
        private static Object synchronizeFeatures = new Object();

        private Cpu() {
        }

        /**
         * Tests whether the current environment is running
         * on a RISC-V (reduced instruction-set) CPU, typically plain RISC-V means 32-bit, and
         * with the added predicate {@link Cpu#is64()} adds the 64-bit
         * predicate.
         *
         * @return true if the current runtime is operating on a RISC-V
         */
        public static boolean isRiscV() {
            return JVM.getProperty().contains("riscv");
        }

        /**
         * Tests whether the current runtime environment is operating
         * on a Sparc CPU, typically plain Sparc means 32-bit.
         *
         * @return true if the current runtime is operating on a Sparc.
         * @see Cpu#isSparcV9()
         */
        public static boolean isSparc() {
            return JVM.getProperty().contains("sparc");
        }

        /**
         * Tests whether the current runtime environment is operating
         * on a SparcV9 CPU, the typical 64-bit version of the Sparc CPU.
         *
         * @return true if the current runtime environment is running on a SparcV9.
         * @see Cpu#isSparc()
         */
        public static boolean isSparcV9() {
            return JVM.getProperty().contains("sparcv9");
        }

        /**
         * Tests whether the current runtime environment is operating
         * on a PowerPc CPU, typically Ppc only means 32-bit.
         *
         * @return true if the current runtime environment is operating on a PPC.
         * @see Cpu#isPpc64le()
         */
        public static boolean isPpc() {
            return JVM.getProperty().contains("ppc");
        }

        /**
         * Tests whether the current runtime environment is operating
         * on a PowerPc CPU, typically the 64-bit version with the little
         * endian byte-order (i.e., le architecture).
         *
         * @return true if the current runtime environment is operating on a PPC-64-bit-le.
         * @see Cpu#isPpc()
         */
        public static boolean isPpc64le() {
            return JVM.getProperty().contains("ppc64le");
        }

        /**
         * Tests whether the current runtime environment is operating on an
         * IBM System/390 CPU, typically plain s390 means the 32-bit version.
         *
         * @return true if the current runtime environment is operating on an IBM System/390.
         * @see Cpu#isS390x()
         */
        public static boolean isS390() {
            return JVM.getProperty().contains("s390");
        }

        /**
         * Tests whether the current runtime environment is operating on an
         * IBM System/390 CPU, typically the 64-bit version.
         *
         * @return true if the current runtime environment is operating on an IBM System/390-64-bit.
         * @see Cpu#isS390()
         */
        public static boolean isS390x() {
            return JVM.getProperty().contains("s390x");
        }

        /**
         * Tests whether the current system architecture is a 64-bit chipset
         * (NOT APPLICABLE TO ALL CPUs).
         *
         * @return true if the current OS architecture is a 64-bit chipset, false otherwise.
         */
        public static boolean is64() {
            return OS_ARCH.getProperty().contains("64");
        }

        /**
         * Tests whether the current system architecture is of an x86 chipset, typically 32-bit intel chipsets.
         *
         * @return true if the current OS architecture is of an x86 chipset, false otherwise.
         */
        public static boolean isX86() {
            return OS_ARCH.getProperty().contains("x86") || OS_ARCH.getProperty().contains("i386");
        }

        /**
         * Tests whether the current CPU vendor is an AMD vendor (e.g., x86_64 Intel Chipset).
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
            return OS_ARCH.getProperty().contains("arm") || OS_ARCH.getProperty().contains("aarch");
        }

        /**
         * Tests whether the current CPU vendor is a Loongson vendor (i.e., an independent Chinese vendor).
         *
         * @return true if the current CPU vendor is a Loongson vendor.
         */
        public static boolean isLoongArch() {
            return OS_ARCH.getProperty().contains("loong");
        }

        /**
         * Reads named CPU features from the OSHI library and parses them into
         * words. If system commands are executed, this might be an expensive
         * operation.
         */
        private static Collection<String> readFeatureFlags() {
            // Obtain the list of CPU feature strings from OSHI:
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            CentralProcessor cpu = hal.getProcessor();
            List<String> oshiList = cpu.getFeatureFlags();

            Pattern pattern = Pattern.compile("[a-z][a-z0-9_]*");

            // Convert the list to a collection of feature names:
            Collection<String> result = new TreeSet<>();
            for (String oshiString : oshiList) {
                /*
                 * On macOS, strings ending with ": 0" indicate
                 * disabled features, so ignore all such lines.
                 */
                if (oshiString.endsWith(": 0")) {
                    continue;
                }
                String lcString = oshiString.toLowerCase(Locale.ROOT);
                Matcher matcher = pattern.matcher(lcString);
                while (matcher.find()) {
                    String featureName = matcher.group();
                    result.add(featureName);
                }
            }

            return result;
        }

        /**
         * Tests whether the named ISA extensions are all present.
         * <p>
         * Extension names are case-insensitive and might be reported
         * differently by different operating systems or even by different
         * versions of the same operating system.
         * <p>
         * Examples of extension names:<ul>
         * <li>"3dnow" for AMD 3D-Now</li>
         * <li>"avx" for x86 AVX</li>
         * <li>"avx2" for x86 AVX2</li>
         * <li>"avx512f" for x86 AVX512F</li>
         * <li>"bmi1" for x86 bit-manipulation instruction set 1</li>
         * <li>"f16c" for x86 half-precision floating-point</li>
         * <li>"fma" for x86 fused multiply-add</li>
         * <li>"fmac" for Arm floating-point multiply-accumulate</li>
         * <li>"mmx" for x86 MMX</li>
         * <li>"neon" for Arm NEON</li>
         * <li>"sse3" for x86 SSE3</li>
         * <li>"sse4_1" for x86 SSE4.1</li>
         * <li>"sse4_2" for x86 SSE4.2</li>
         * <li>"ssse3" for x86 SSSE3</li>
         * <li>"v8" for Arm V8</li>
         * <li>"v8_crc32" for Arm V8 extra CRC32</li>
         * <li>"v8_crypto" for Arm V8 extra cryptographic</li>
         * <li>"v81_atomic" for Arm V8.1 atomic</li>
         * <li>"v82_dp" for Arm V8.2 DP</li>
         * <li>"v83_jscvt" for Arm v8.3 JSCVT</li>
         * <li>"v83_lrcpc" for Arm v8.3 LRCPC</li>
         * </ul>
         * <p>
         * Wikipedia provides informal descriptions of many ISA extensions.
         * https://en.wikipedia.org/wiki/Template:Multimedia_extensions offers a
         * good starting point.
         *
         * @param requiredNames the names of the extensions to test for
         * @return {@code true} if the current platform supports all of the
         * specified extensions, otherwise {@code false}
         */
        public static boolean hasExtensions(String... requiredNames) {
            synchronized (synchronizeFeatures) {
                if (presentFeatures == null) {
                    presentFeatures = readFeatureFlags();
                }

                // Test for each required extension:
                for (String extensionName : requiredNames) {
                    String lcName = extensionName.toLowerCase(Locale.ROOT);
                    /*
                     * On Windows, ISA extensions are coded as features
                     * with names like "PF_xxx_INSTRUCTIONS_AVAILABLE" and
                     * "PF_ARM_xxx_INSTRUCTIONS_AVAILABLE".
                     *
                     * For details see
                     * https://learn.microsoft.com/en-us/windows/win32/api/processthreadsapi/nf-processthreadsapi-isprocessorfeaturepresent
                     */
                    String pfNameArm = "pf_arm_" + lcName + "_instructions_available";
                    String pfNameX86 = "pf_" + lcName + "_instructions_available";
                    boolean isPresent = presentFeatures.contains(lcName)
                            || presentFeatures.contains(pfNameX86)
                            || presentFeatures.contains(pfNameArm);

                    // conjunctive test: fails if any required extension is missing
                    if (!isPresent) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    /**
     * Retrieves the data of this native variant property in a lowercase style.
     * 
     * @return the specified property object in a string format.
     */
    public String getProperty() {
        return property.toLowerCase();
    }
}
