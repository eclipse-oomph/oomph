#!/bin/bash

REPO = "$1"
REPO_NAME = "$2"

DOWNLOADS = "/home/data/httpd/download.eclipse.org"
FILE = `echo $REPO | sed "s!$DOWNLOADS\(.*?\)!\1!g"`

cd $REPO
unzip -qq artifacts.jar

java -cp $WORKSPACE/git/releng/org.eclipse.oomph.releng/bin-tools/ ArtifactRepositoryAdjuster $REPO/artifacts.xml $REPO/artifacts.tmp $REPO_NAME $REPO
mv artifacts.tmp artifacts.xml

zip -r -9 -q artifacts.jar artifacts.xml
rm artifacts.xml
