#!/bin/bash

if [[ "$GIT" == "" ]]; then
  GIT=$WORKSPACE/git
fi

set -o nounset
set -o errexit

REPO=$1
REPO_NAME=$2
CURDIR=`pwd -P`

cd "$REPO"
unzip -qq content.jar

sed -e "s/<repository name='[^']*'/<repository name='$REPO_NAME'/" content.xml > content.out
mv content.out content.xml

zip -r -9 -qq content.jar content.xml
rm content.xml
cd "$CURDIR"
