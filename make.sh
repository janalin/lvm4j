#!/bin/sh

DIR=$PWD

CLP="$DIR/ss-hmm"
cd $CLP
mvn package
cd $DIR
CP="$CLP/target/ss-hmm.jar"
cp $CP ss-hmm.jar


