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

package electrostatic4j.snaploader.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class SnapLoaderLogger {

    /**
     * NativeBinaryLoader logger object.
     */
    private static final Logger logger = Logger.getLogger("jSnapLoader");

    /**
     * Flag for enable/disable logging.
     */
    private static boolean loggingEnabled;

    /**
     * Log data with a level and a throwable (optional).
     *
     * @param level the logger level
     * @param sourceMethod the source of this call
     * @param msg a string formatted message to display
     */
    public static void log(Level level, String sourceClass, String sourceMethod, String msg) {
        if (!SnapLoaderLogger.isLoggingEnabled()) {
            return;
        }
        logger.logp(level, sourceClass, sourceMethod, msg);
    }

    /**
     * Log data with a level and a throwable (optional).
     *
     * @param level the logger level
     * @param sourceMethod the source of this call
     * @param msg a string formatted message to display
     * @param throwable optional param for error messages
     */
    public static void log(Level level, String sourceClass, String sourceMethod, String msg, Throwable throwable) {
        if (!SnapLoaderLogger.isLoggingEnabled()) {
            return;
        }
        logger.logp(level, sourceClass, sourceMethod, msg, throwable);
    }

    /**
     * Enables the logging for this object, default value is false.
     *
     * @param loggingEnabled true to enable logging, false otherwise
     */
    public static void setLoggingEnabled(boolean loggingEnabled) {
        SnapLoaderLogger.loggingEnabled = loggingEnabled;
    }

    /**
     * Tests the logging flag, default value is false.
     *
     * @return true if the logging flag is enabled, false otherwise
     */
    public static boolean isLoggingEnabled() {
        return SnapLoaderLogger.loggingEnabled;
    }
}
