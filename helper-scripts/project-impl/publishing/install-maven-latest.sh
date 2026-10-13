#!/bin/bash

source "./helper-scripts/project-impl/variables.sh"

function downloadMavenLatest() {
    wget "https://archive.apache.org/dist/maven/maven-3/${maven_version}/binaries/apache-maven-${maven_version}-bin.tar.gz"
    return $?
}

function extractMavenLatest() {
    tar xzvf "./apache-maven-${maven_version}-bin.tar.gz"
    return $?
}

downloadMavenLatest
extractMavenLatest
