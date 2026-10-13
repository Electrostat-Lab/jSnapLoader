#!/bin/bash

function generateGenericPom() {
    local groupId=$1
    local artifactId=$2
    local version=$3
    local output=$4

    config="<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\"> \
    <modelVersion>4.0.0</modelVersion> \
    <groupId>${groupId}</groupId> \
    <artifactId>${artifactId}</artifactId> \
    <version>${version}</version> \
    <name>jSnapLoader API</name> \
    <packaging>jar</packaging> \
    <description>A high-performance cross-platform dynamic library loader API for JVM Applications with file locator and file extractor interfaces.</description> \
    <url>https://github.com/Electrostat-Lab/jSnapLoader</url> \
    <licenses>  \
      <license> \
          <name>The Electrostatic-Sandbox Distributed Simulation Project, jSnapLoader BSD-3 Clause License</name> \
          <url>https://github.com/Electrostat-Lab/jSnapLoader/blob/master/LICENSE</url> \
      </license> \
    </licenses> \
    <developers> \
      <developer> \
        <name>Pavly Gerges aka. pavl_g</name> \
        <email>pepogerges33@gmail.com</email> \
        <organization>Electrostat-Lab</organization> \
        <organizationUrl>https://github.com/Electrostat-Lab</organizationUrl> \
      </developer> \
    </developers> \
    <scm> \
      <connection>scm:git://github.com/Electrostat-Lab/jSnapLoader.git</connection> \
      <developerConnection>scm:git:ssh://github.com/Electrostat-Lab/jSnapLoader.git</developerConnection> \
      <url>https://github.com/Electrostat-Lab/jSnapLoader/tree/master</url> \
    </scm> \
</project> \
"
    echo $config > ${output}
}

function publishBuild() {
    local groupId="$1"
    local artifactId="$2"
    local artifact="$3"
    local version="$4"
    local javadoc_jar="$5"
    local sources_jar="$6"
    local pomFile="$7"

    : "${CENTRAL_TOKEN_USERNAME:?Missing Central token username}"
    : "${CENTRAL_TOKEN_PASSWORD:?Missing Central token password}"

    # Upload and sign the main JAR, sources, Javadoc, and POM.
    "${maven_bin}" gpg:sign-and-deploy-file \
        -s "${settings}" \
        -Durl="${sonatype_url}" \
        -DgroupId="${groupId}" \
        -DartifactId="${artifactId}" \
        -Dversion="${version}" \
        -DrepositoryId="${repository}" \
        -DpomFile="${pomFile}" \
        -Dfile="${artifact}" \
        -Djavadoc="${javadoc_jar}" \
        -Dsources="${sources_jar}" \
        -Dgpg.passphrase="${passphrase}" || return $?

    # Transfer the uploaded deployment to the Central Publisher Portal.
    local token
    token=$(printf '%s:%s' \
        "${CENTRAL_TOKEN_USERNAME}" \
        "${CENTRAL_TOKEN_PASSWORD}" | base64 | tr -d '\n') || return $?

    curl --fail-with-body --silent --show-error \
        --request POST \
        --header "Authorization: Bearer ${token}" \
        "https://ossrh-staging-api.central.sonatype.com/manual/upload/defaultRepository/${groupId}?publishing_type=user_managed"
}