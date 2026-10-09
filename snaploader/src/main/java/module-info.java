module com.electrostat.snaploader {
    requires java.logging;
    requires java.base;
    requires com.github.oshi;

    exports electrostatic4j.snaploader;
    exports electrostatic4j.snaploader.filesystem;
    exports electrostatic4j.snaploader.library;
    exports electrostatic4j.snaploader.platform;
    exports electrostatic4j.snaploader.platform.util;
    exports electrostatic4j.snaploader.throwable;
    exports electrostatic4j.snaploader.util;
}