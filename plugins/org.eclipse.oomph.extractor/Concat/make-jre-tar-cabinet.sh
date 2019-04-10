#!/bin/bash

echo hello

TARGET=$(pwd)
echo $TARGET

cd /c/Program\ Files/Java/jdk1.8.0_121
tar -cf $TARGET/jre.tar jre
cd -
makecab jre.tar jre.tar.cab
