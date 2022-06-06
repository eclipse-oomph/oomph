#!/bin/bash

if [[ "$GIT" == "" ]]; then
  GIT=$WORKSPACE/git
fi

set -o nounset
set -o errexit

REPO=$1
REPO_FINAL=$2
REPO_NAME=$3
BUILD_TYPE=$4
CURDIR=`pwd -P`

cd "$REPO"
unzip -qq artifacts.jar
cd "$CURDIR"

java -cp "$GIT/releng/org.eclipse.oomph.releng/target/classes/" ArtifactRepositoryAdjuster \
  "$REPO" \
  "$REPO_FINAL" \
  "$REPO_NAME"\
  "$BUILD_TYPE"

cd "$REPO"
mv artifacts.out artifacts.xml
zip -r -9 -qq artifacts.jar artifacts.xml
xz -f artifacts.xml
cd "$CURDIR"
