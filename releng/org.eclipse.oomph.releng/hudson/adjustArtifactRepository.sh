#!/bin/bash

REPO=$1
REPO_NAME=$2
CURDIR=`pwd -P`

cd "$REPO"
unzip -qq artifacts.jar
cd "$CURDIR"

java -cp "$WORKSPACE/git/releng/org.eclipse.oomph.releng/buildTools.jar" ArtifactRepositoryAdjuster "$REPO" "$REPO_NAME"

cd "$REPO"
mv artifacts.out artifacts.xml
zip -r -9 -qq artifacts.jar artifacts.xml
rm artifacts.xml
cd "$CURDIR"
