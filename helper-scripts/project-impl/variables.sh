#!/bin/sh

# Maven sonatype stuff
# ---------------------
sonatype_url="https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
repository="central"
groupId="io.github.electrostat-lab"
maven_version="3.9.4"
maven_bin="./apache-maven-$maven_version/bin/mvn"
desktop_pomFile="./helper-scripts/project-impl/publishing/snaploader.pom"
passphrase="avrsandbox"

desktop_artifactId_release="snaploader"

desktop_artifactId_debug="snaploader-debug"

settings="./helper-scripts/project-impl/publishing/maven-settings.xml"
build_dir="./snaploader/build/libs"
# ---------------------
