# <img src="https://github.com/Software-Hardware-Codesign/jSnapLoader/assets/60224159/85ac90d0-7d10-4d7c-a57e-390246ac5dee" width=60 height=60/> jSnapLoader [![Codacy Badge](https://app.codacy.com/project/badge/Grade/50261f7cc09b4b9bacaf9f44ecc28ea9)](https://app.codacy.com/gh/Electrostat-Lab/jSnapLoader/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade) [![](https://img.shields.io/badge/jSnapLoader-latest_version-red)](https://github.com/Software-Hardware-Codesign/jSnapLoader/releases)
[![](https://github.com/Software-Hardware-Codesign/jSnapLoader/actions/workflows/build-test.yml/badge.svg)]() [![](https://github.com/Software-Hardware-Codesign/jSnapLoader/actions/workflows/build-deploy.yml/badge.svg)]()

A high-performance cross-platform dynamic library loader API for JVM Applications,
with highly modifiable system-specific registration using platform predicates.

## Software Specification: 

| Item                            | Description | Predicate Calculus | 
|---------------------------------|-------------|--------------------|
| _Problem Definition_            | The main problem that entailed building this project had been to have something similar to the JME's `NativeLibraryLoader`, but for a better version with more capabilities (such as: loading libraries from external Jars and having a generic file extraction interface). |                    |
| _Generalized Approach_          | The generalized approach was deemed insufficient; as it will not add on the JME's `NativeLibraryLoader`, and thus the project will be insignificant. The generalized approach entails building something that directly encapsulates concrete buffered byte stream calls to _ZipFileSystem_ interfaces for the native libraries only within the same classpath, and that actually is a bad practice to build APIs on concretions, instead as the Ontology entails, abstractions and using levels of indirections should play the most part in developing a new system. | |
| _jSnapLoader-specific Approach_ | The jSnapLoader-specific approach is quite ingenuine, nevertheless it's not unique. The essential design entails the basic use of the System-Entity-Structure (SES) Framework; which essentially decomposes the the bulky actions of the library, first from the perspective of behavior into deterministic finite-states, and then from the structural perspective into the components that execute these states; the net result is having a freely-floating decomposed components. The next step was regrouping those components (using common properties from predicate calculus) and finding abstractions for them, and finally capturing relations between those components and similar systems (i.e., FileSystem APIs). This structural way of thinking has resulted in the production of beneficial compilation units that could serve other purposes and not entailed specifically to serve native library extraction and loading. |                    |
| _Framework Decomposition_ | The decomposition had been successfully inducted into a `FileLocator` interface with a validation strategy, a `FileExtractor` interface with the full ability to control native IO resources, and a `LibraryLoader` interface with the ability to build and register platform predicates to support any new unsupported systems. |                    |
| _Framework Enhancements_ | The `PlatformPredicate` interface wasn't planned from the start; it has been added as an enhancement for the library to accomodate the changes and the addition of new platforms robustly without changing the internal API. In fact, I give credits to [Jolt-jni](https://github.com/stephengold/jolt-jni/tree/master) for implicitly opening the vision to add them. Furthermore, the addition of the `FileLocalizingListener`, the `FileExtractionListener`, and the `NativeLibraryLoadingListener` binds the user API to the framework API giving jSnapLoader a more added robustness value overtime. |                    |
| _Credits for other systems_ | Credits should go for the jSnapLoader's users, [Serial4j](https://github.com/Electrostat-Lab/Serial4j), [Jolt-jni](https://github.com/stephengold/jolt-jni/tree/master), and [Electrostatic4j](https://github.com/Electrostat-Lab/Electrostatic-Sandbox). Those implicitly and continously have pushed the API over to become better. | |

## Software Architectural Paradigm: 
<img src="https://github.com/Electrostat-Lab/jSnapLoader/blob/master/architecture/architectural-paradigm.png" alt="Architectural-Paradigm-Placement"/>

## Quick Building and running examples: 
```bash
┌─[pavl-machine@pavl-machine]─[/home/twisted/GradleProjects/jSnapLoader]
└──╼ $./gradlew clean && \
      ./gradlew build && \
      ./gradlew :snaploader-examples:run

BUILD SUCCESSFUL in 943ms
2 actionable tasks: 2 executed

BUILD SUCCESSFUL in 1s
7 actionable tasks: 7 executed

BUILD SUCCESSFUL in 1s
4 actionable tasks: 1 executed, 3 up-to-date
```

## Run specific example by name: 
```bash
┌─[pavl-machine@pavl-machine]─[/home/twisted/GradleProjects/jSnapLoader]
└──╼ $./gradlew :snaploader-examples:TestZipExtractor \
                :snaploader-examples:run

BUILD SUCCESSFUL in 1s
4 actionable tasks: 2 executed, 2 up-to-date
```

## Plug-and-play usage: 
### Project build files:
[build.gradle]
```groovy
dependencies {
    implementation "io.github.software-hardware-codesign:snaploader:1.0.0-delta"
}
```
[settings.gradle]
```groovy
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
...
// project Gradle modules includes
```
### Library Implementation: 
1) The most straightforward way: 
```java
final LibraryInfo info = new LibraryInfo(null, "lib/independent", "basename", null);
final NativeBinaryLoader loader = new NativeBinaryLoader(info);
final NativeDynamicLibrary[] libraries = new NativeDynamicLibrary[] {
      new NativeDynamicLibrary("lib/linux/x86-64", PlatformPredicate.LINUX_X86_64),
      new NativeDynamicLibrary("lib/macos/arm-64", PlatformPredicate.MACOS_ARM_64),
      new NativeDynamicLibrary("lib/macos/x86-64", PlatformPredicate.MACOS_X86_64),
      new NativeDynamicLibrary("lib/win/x86-64", PlatformPredicate.WIN_X86_64)
      ...
};
loader.registerNativeLibraries(libraries).initPlatformLibrary();
loader.setLoggingEnabled(true);
loader.setRetryWithCleanExtraction(true);
try {
      loader.loadLibrary(LoadingCriterion.INCREMENTAL_LOADING);
} catch (IOException e) {
      Logger.getLogger(NativeBinaryLoader.class.getName()
            .log(Level.SEVERE, "Native loader has failed!", e);
}
```
- This way utilizes the classpath on the stock Jar archive to locate, extract and load the native binaries.
- It first defines a library info object with a pointer to the classpath (first null argument), and a default path that will be
used in case the platform path for the selected platform predicate is invalid, then a `basename` for the library to be operated, and finally the current working directory as an extraction path (third null argument).

2) A Superior control:
```java
import java.nio.file.Path;
import java.nio.file.Paths;
...
// compatible with Java 8, Since 1.7
final Path compression = Paths.get(PropertiesProvider.USER_DIR.getSystemProperty(), "libs", "electrostatic4j.jar");
final Path extractionPath = Paths.get(PropertiesProvider.USER_DIR.getSystemProperty(), "libs", "natives");
final LibraryInfo info = new LibraryInfo(compression.toString(), "lib/independent", "electrostatic4j", extractionPath.toString());
final NativeBinaryLoader loader = new NativeBinaryLoader(info);
final NativeDynamicLibrary[] libraries = new NativeDynamicLibrary[] {
      new NativeDynamicLibrary("lib/linux/x86-64", PlatformPredicate.LINUX_X86_64),
      new NativeDynamicLibrary("lib/macos/arm-64", PlatformPredicate.MACOS_ARM_64),
      new NativeDynamicLibrary("lib/macos/x86-64", PlatformPredicate.MACOS_X86_64),
      new NativeDynamicLibrary("lib/win/x86-64", PlatformPredicate.WIN_X86_64)
      ...
};
loader.registerNativeLibraries(libraries).initPlatformLibrary();
loader.setLoggingEnabled(true);
loader.setRetryWithCleanExtraction(true);
try {
      loader.loadLibrary(LoadingCriterion.INCREMENTAL_LOADING);
} catch (IOException e) {
      Logger.getLogger(NativeBinaryLoader.class.getName()
            .log(Level.SEVERE, "Native loader has failed!", e);
}
```
- This way utilizes the `java.nio.file.Paths` and `java.nio.file.Path` APIs to build platform-independent directory paths, and it's deemed the most superior way, especially for vague systems; thus it's considered the most robust way, and the best cross-platform strategy; because it depends on the Java implementation for this specific runtime.

3) Full control (external Jar localizing, platform predicates, and platform-independent extraction paths):
- [Serial4j's Implementation - `NativeImageLoader`](https://github.com/Electrostat-Lab/Serial4j/blob/master/serial4j/src/main/java/com/serial4j/util/loader/NativeImageLoader.java)
- [Electrostatic4j's Implementation - essentially the same](https://github.com/Electrostat-Lab/Electrostatic-Sandbox/blob/master/electrostatic-sandbox-framework/electrostatic4j/electrostatic4j-core/src/main/java/electrostatic4j/util/loader/NativeImageLoader.java)

## Appendix:
### Features:
- [x] Platform-specific dynamic libraries' registration.
- [x] Platform-specific dynamic libraries building using platform-specific predicates (NEW).
- [x] Locate and load external dynamic libraries directly.
- [x] Extract native libraries from the stock jar library (classpath).
- [x] Locate external jars and extract native libraries from them.
- [x] Define an extract directory.
- [x] Retry Criterion with clean extraction (NEW).
- [x] Exposed the platform-dependent library `NativeDynamicLibrary` (NEW).
- [x] Exposed the `NativeVariant` providing system properties.
- [x] EventDispatchers: Extraction Listeners, Loading Listeners, and System Detection Listeners.
- [ ] Extract automatically based on the application name and version.

### Documentation-list:
- [x] Architecture paradigm.
- [x] API documentation. 
- [ ] Java-Doc page.

### Credits:
#### Projects:
Those projects had contributed implicitly and/or explicitly and must be recognized:
- [Serial4j](https://github.com/Electrostat-Lab/Serial4j): was the initial trigger for this project.
- [Jolt-jni](https://github.com/stephengold/jolt-jni): was and still the drive to continue this project.
- [Electrostatic4j](https://github.com/Electrostat-Lab/Electrostatic-Sandbox): will be the future supporter for this project.

#### People:
I owe these people a cup of coffee for their gracious contributions, when we eventually meet:
- [Stephen Gold aka. sgold](https://github.com/stephengold)

#### Pure Science: 
- Discrete mathematics and Ontology.
- The System-Entity-Structure (SES) Framework for System-design.
- The Tricotyledeon theory of system design by W.A.Wymore (T3SD).
- The Finite-automata theory.
