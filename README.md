# <img src="https://github.com/Software-Hardware-Codesign/jSnapLoader/assets/60224159/85ac90d0-7d10-4d7c-a57e-390246ac5dee" width=60 height=60/> jSnapLoader
[![](https://github.com/Software-Hardware-Codesign/jSnapLoader/actions/workflows/build-test.yml/badge.svg)]() [![](https://github.com/Software-Hardware-Codesign/jSnapLoader/actions/workflows/build-deploy.yml/badge.svg)]()

A high-performance cross-platform dynamic library loader API for JVM Applications.

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

## Appendix 
Features:
- [x] Locate and load external dynamic libraries directly.
- [x] Extract native libraries from the stock jar library (classpath).
- [x] Locate external jars and extract native libraries from them.
- [x] Define an extract directory.
- [ ] Extract automatically based on the application name and version.

Wish-list:
- [x] External jar locator: a utility to locate jar files to be able to extract native libraries from.
- [x] External library locator: a utility to locate dynamic libraries directly.

Documentation-list:
- [ ] Architecture paradigm.
- [x] API documentation. 
