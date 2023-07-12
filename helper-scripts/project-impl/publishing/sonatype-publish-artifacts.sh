#!/bin/bash

source "./helper-scripts/abstract/abstract-sonatype-publish.sh"
source "./helper-scripts/project-impl/variables.sh"

# obtain dependencies in the form 'groupId:artifact:version'
version=${1}
desktop_artifact="${build_dir}/${desktop_artifactId_release}-${version}.jar"

sources_jar="${build_dir}/${desktop_artifactId_release}-${version}-sources.jar"
javadoc_jar="${build_dir}/${desktop_artifactId_release}-${version}-javadoc.jar"

generateGenericPom ${groupId} ${desktop_artifactId_release} ${version} "${desktop_pomFile}"

# publish 'android' and 'desktop' builds to maven sonatype
publishBuild "${desktop_artifactId_release}" "${desktop_artifact}" "${version}" "${javadoc_jar}" "${sources_jar}" "${desktop_pomFile}"