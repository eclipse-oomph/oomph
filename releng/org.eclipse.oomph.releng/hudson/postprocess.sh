#!/bin/bash

f=$1
if [[ $f == "" ]]; then
    echo "No input file specified!"
    exit 1
fi
if [[ ! -f $f ]]; then
    echo "Input file $f not found!"
    exit 2
fi

shift
script=$1

if [[ $script == "" ]]; then
    echo "No script specified!"
    exit 3
fi

if [[ ! -f $script ]]; then
    echo "Script $script not found!"
    exit 4
fi

script=`readlink -f $script`

shift
args=$*

set -o nounset
set -o errexit

###########################################################################

file=`readlink -f $f`
echo "Post-processing $file"

tmp=$file-tmp
echo "Product dir: $tmp"

rm -rf $tmp
mkdir $tmp
cd $tmp

if [[ $file == *.tar.gz ]]; then

  tar -xzf $file
  bash $script $file $args

  if [[ $file == *mac64* ]]; then
    rm -rf "Eclipse Installer.app/Contents/_CodeSignature"
    zip -r -q unsigned.zip "Eclipse Installer.app"
    rm -rf "Eclipse Installer.app"
    curl -O https://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/releng/org.eclipse.oomph.releng/hudson/installer.entitlements
    curl -o signed.zip -F file=@unsigned.zip -F entitlements=installer.entitlements http://172.30.206.146:8282/macosx-signing-service/1.0.1-SNAPSHOT
    unzip -qq signed.zip
    rm -f unsigned.zip signed.zip
    chmod -R a-st "Eclipse Installer.app"
    chmod a+x "Eclipse Installer.app/Contents/MacOS/eclipse-inst"
    tar -czf $file-new "Eclipse Installer.app"
  else
    tar -czf $file-new *
  fi

  mv $file-new $file

elif [[ $file == *.exe ]]; then

  concatdir=`mktemp -d`
  echo "Concat dir: $concatdir"

  extractorlib=/home/data/httpd/download.eclipse.org/oomph/products/latest/repository/plugins/org.eclipse.oomph.extractor.lib_*.jar
  java -cp $extractorlib org.eclipse.oomph.extractor.lib.BINExtractor \
    $file \
    $concatdir/product.zip \
    -export \
    $concatdir/marker.txt \
    $concatdir/extractor.exe \
    $concatdir/libdata.jar \
    $concatdir/descriptor.txt

  unzip -qq $concatdir/product.zip
  bash $script $file $args
  zip -r -9 -qq --symlinks $file.zip *

  # Don't use the already signed extractor.exe but rather the one directly from git.
  # Otherwise signing will fail.
  curl -O https://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.extractor/extractor-64.exe

  cat extractor-64.exe \
    $concatdir/marker.txt \
    $concatdir/libdata.jar \
    $concatdir/marker.txt \
    $concatdir/descriptor.txt \
    $concatdir/marker.txt \
    $file.zip \
    $concatdir/marker.txt > $file-unsigned

  echo ""
  curl -o $file-new -F filedata=@$file-unsigned http://build.eclipse.org:31338/winsign.php

  actualSize=$(wc -c "$file-new" | cut -f 1 -d ' ')
  if [ $actualSize -lt 40000000 ]; then
    echo "$file-new is just $actualSize bytes large!"
    echo ""
    cat $file-new
    echo ""
    exit 5
  fi

  mv $file-new $file
  rm -rf $concatdir
  rm -rf $file.zip
  rm -rf $file-unsigned

elif [[ $file == *.zip ]]; then

  unzip -qq $file
  bash $script $file $args
  zip -r -9 -qq --symlinks $file-new *
  mv $file-new $file

fi

rm -rf $tmp
cd ..
echo ""
