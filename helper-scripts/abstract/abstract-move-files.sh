#!/bin/bash

function move() {
    local src=$1
    local dest=$2

    mv -uv $src $dest 

    return $?
}