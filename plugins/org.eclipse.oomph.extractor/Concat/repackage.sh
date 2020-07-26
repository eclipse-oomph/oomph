#!/bin/bash

mkdir -p ../Libdata
rm -rf ../Concat/product
mkdir -p ../Concat/product
cd ../../org.eclipse.oomph.extractor.lib/bin
zip -r -9 -qq  ../../org.eclipse.oomph.extractor/Libdata/libdata.jar *
cd -

unzip -p ../../../products/org.eclipse.oomph.setup.installer.product/target/products/*64.zip eclipse-inst.ini | grep '^-vm[^a-z]*$'

if unzip -p ../../../products/org.eclipse.oomph.setup.installer.product/target/products/*64.zip eclipse-inst.ini | grep -q '^-vm[^a-z]*$'; then
echo "Preparing with built-in JRE"
mkdir -p ../Concat/product/unzipped
echo "Unzipping"
unzip -q ../../../products/org.eclipse.oomph.setup.installer.product/target/products/*64.zip -d ../Concat/product/unzipped
cd ../Concat/product/unzipped
echo "Tarring"
tar -cf ../product-64.tar *
cd -
rm -rf ../Concat/product/unzipped
else
echo "Preparing without built-in JRE"
cp ../../../products/org.eclipse.oomph.setup.installer.product/target/products/*64.zip ../Concat/product/product-64.zip
fi
