#!/bin/bash

function getMd5Checksum() {
    local input=$1
    local output=$2

    md5sum "$input" > "$output"

    return $?
}

function getSha1Checksum() {
    local input=$1
    local output=$2

    sha1sum "$input" > "$output"

    return $?
}