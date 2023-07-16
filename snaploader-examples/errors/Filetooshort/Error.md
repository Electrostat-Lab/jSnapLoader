```java
Jul 16, 2023 12:02:22 PM com.avrsandbox.snaploader.NativeBinaryLoader loadBinary
SEVERE: Cannot load the dynamic library: /home/twisted/GradleProjects/jSnapLoader/snaploader-examples/libs/libjmealloc.so
java.lang.UnsatisfiedLinkError: /home/twisted/GradleProjects/jSnapLoader/snaploader-examples/libs/libjmealloc.so: /home/twisted/GradleProjects/jSnapLoader/snaploader-examples/libs/libjmealloc.so: file too short
        at java.base/jdk.internal.loader.NativeLibraries.load(Native Method)
        at java.base/jdk.internal.loader.NativeLibraries$NativeLibraryImpl.open(NativeLibraries.java:388)
        at java.base/jdk.internal.loader.NativeLibraries.loadLibrary(NativeLibraries.java:232)
        at java.base/jdk.internal.loader.NativeLibraries.loadLibrary(NativeLibraries.java:174)
        at java.base/java.lang.ClassLoader.loadLibrary(ClassLoader.java:2389)
        at java.base/java.lang.Runtime.load0(Runtime.java:755)
        at java.base/java.lang.System.load(System.java:1953)
        at com.avrsandbox.snaploader.NativeBinaryLoader.loadBinary(NativeBinaryLoader.java:178)
        at com.avrsandbox.snaploader.NativeBinaryLoader.lambda$cleanExtractBinary$0(NativeBinaryLoader.java:216)
        at com.avrsandbox.snaploader.file.FileExtractor.extract(FileExtractor.java:102)
        at com.avrsandbox.snaploader.file.ConcurrentFileExtractor.extract(ConcurrentFileExtractor.java:73)
        at com.avrsandbox.snaploader.NativeBinaryLoader.cleanExtractBinary(NativeBinaryLoader.java:212)
        at com.avrsandbox.snaploader.NativeBinaryLoader.incrementalExtractBinary(NativeBinaryLoader.java:200)
        at com.avrsandbox.snaploader.NativeBinaryLoader.loadLinux(NativeBinaryLoader.java:132)
        at com.avrsandbox.snaploader.NativeBinaryLoader.loadLibrary(NativeBinaryLoader.java:89)
        at com.avrsandbox.snaploader.examples.TestMultiThreading$2.run(TestMultiThreading.java:67)
        at java.base/java.lang.Thread.run(Thread.java:833)
```