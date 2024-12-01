#!/bin/bash
echo "Generate additional products" 
pwd

exec 2>&1
set -e
set -x

cp Installer.p2.inf Installer-With-JRE.p2.inf
sed -e 's/uid="\([^"]*\)"/uid="\1.with-jre"/;s/<!--//;s/-->//' < Installer.product > Installer-With-JRE.product

for i in Installer-With-JRE; do 
  echo $i;
  cp $i.p2.inf $i-Restricted.p2.inf
  sed -e 's/uid="\([^"]*\)"/uid="\1.restricted"/' < $i.product > $i-Restricted.product
done