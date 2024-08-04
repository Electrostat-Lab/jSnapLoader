package electrostatic.snaploader.util;

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
