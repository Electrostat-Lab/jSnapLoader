# <img src="https://github.com/Software-Hardware-Codesign/jSnapLoader/assets/60224159/85ac90d0-7d10-4d7c-a57e-390246ac5dee" width=60 height=60/> jSnapLoader [![Codacy Badge](https://app.codacy.com/project/badge/Grade/50261f7cc09b4b9bacaf9f44ecc28ea9)](https://app.codacy.com/gh/Software-Hardware-Codesign/jSnapLoader/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade) [![](https://img.shields.io/badge/jSnapLoader-latest_version-red)](https://github.com/Software-Hardware-Codesign/jSnapLoader/releases)
[![](https://github.com/Software-Hardware-Codesign/jSnapLoader/actions/workflows/build-test.yml/badge.svg)]() [![](https://github.com/Software-Hardware-Codesign/jSnapLoader/actions/workflows/build-deploy.yml/badge.svg)]()

A high-performance cross-platform dynamic library loader API for JVM Applications,
with highly modifiable system-specific registration using platform predicates.

## Software Specification: 

| Item                            | Description | Predicate Calculus | 
|---------------------------------|-------------|--------------------|
| _Problem Definition_            | The main problem that entailed building this project had been to have something similar to the JME's `NativeLibraryLoader`, but for a better version with more capabilities (such as: loading libraries from external Jars and having a generic file extraction interface). |                    |
| _Generalized Approach_          | The generalized approach was deemed insufficient; as it will not add on the JME's `NativeLibraryLoader`, and thus the project will be insignificant. The generalized approach entails building something that directly encapsulates concrete buffered byte stream calls to _ZipFileSystem_ interfaces for the native libraries only within the same classpath, and that actually is a bad practice to build APIs on concretions, instead as the Ontology entails, abstractions and using levels of indirections should play the most part in developing a new system. | |
| _jSnapLoader-specific Approach_ | The jSnapLoader-specific approach is quite ingenuine, nevertheless it's not unique. The essential design entails the basic use of the System-Entity-Structure (SES) Framework; which essentially decomposes the the bulky actions of the library, first from the perspective of behavior into deterministic finite-states, and then from the structural perspective into the components that execute these states; the net result is having a freely-floating decomposed components. The next step was capturing relations between those components and similar systems (i.e., FileSystem APIs). The result of this structural way of thinking has resulted in beneficial compilation units that could serve other purposes and not entailed specifically to serve native library extraction and loading. |                    |
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
