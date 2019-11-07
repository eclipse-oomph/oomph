#!/bin/bash

f=$1
if [[ $f == "" ]]; then
    echo "No file specified!"
    exit 1
fi
if [[ ! -f $f ]]; then
    echo "File $f not found!"
    exit 2
fi

f=`readlink -f $f`

configini=$2
if [[ $configini == "" ]]; then
    echo "No config.ini specified!"
    exit 3
fi
if [[ ! -f $configini ]]; then
    echo "File $configini not found!"
    exit 4
fi

set -o nounset
set -o errexit

###########################################################################

if [[ $f == *win??.exe ]]; then
  inifile=configuration/config.ini
elif [[ $f == *linux??.tar.gz ]]; then
  inifile=eclipse-installer/configuration/config.ini
elif [[ $f == *mac??.tar.gz ]]; then
  inifile="Eclipse Installer.app/Contents/Eclipse/configuration/config.ini"
fi

echo "Config ini: $inifile"

extractorlib=/home/data/httpd/download.eclipse.org/oomph/products/latest/repository/plugins/org.eclipse.oomph.extractor.lib_*.jar
java -cp $extractorlib org.eclipse.oomph.extractor.lib.PropertiesUpdater \
  "$configini" \
  "$inifile"

echo ""
cat "$inifile"
echo ""