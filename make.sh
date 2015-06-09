#!/bin/sh

DIR=$PWD
CLP="$DIR/ss-hmm"
cd $CLP
mvn package
cd $DIR
CP="$CLP/target/ss-hmm-1.0-jar-with-dependencies.jar"
cp $CP ss-hmm.jar
