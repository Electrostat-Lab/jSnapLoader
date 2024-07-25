# <img src="https://github.com/Software-Hardware-Codesign/jSnapLoader/assets/60224159/85ac90d0-7d10-4d7c-a57e-390246ac5dee" width=60 height=60/> jSnapLoader [![Codacy Badge](https://app.codacy.com/project/badge/Grade/50261f7cc09b4b9bacaf9f44ecc28ea9)](https://app.codacy.com/gh/Software-Hardware-Codesign/jSnapLoader/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade) [![](https://img.shields.io/badge/jSnapLoader-latest_version-red)](https://github.com/Software-Hardware-Codesign/jSnapLoader/releases)
[![](https://github.com/Software-Hardware-Codesign/jSnapLoader/actions/workflows/build-test.yml/badge.svg)]() [![](https://github.com/Software-Hardware-Codesign/jSnapLoader/actions/workflows/build-deploy.yml/badge.svg)]()

A high-performance cross-platform dynamic library loader API for JVM Applications,
with highly modifiable system-specific registration using platform predicates.

## Software Specification: 

| Item                            | Description | Predicate Calculus | 
|---------------------------------|-------------|--------------------|
| _Problem Definition_            |             |                    |
| _Generalized Approach_          |             |                    |
| _jSnapLoader-specific Approach_ |             |                    |
| _Framework Decomposition_       |             |                    |
| _Framework Enhancements_        |             |                    |
| _Credits for other systems_     |             |                    |

## Software Architectural Paradigm: 
<img src="https://github.com/Electrostat-Lab/jSnapLoader/architecture/architectural-paradigm.png" alt="Architectural-Paradigm-Placement"/>

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
