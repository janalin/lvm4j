#!/bin/sh

dir=$(greadlink -f  $0)
dir=$(dirname ${dir})
mvn package
cp "${dir}/target/lvm4j.jar" "${dir}/lvm4j.jar"
