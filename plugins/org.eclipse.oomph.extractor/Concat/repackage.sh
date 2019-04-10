#!/bin/bash

mkdir -p ../Libdata
mkdir -p ../Concat/product
cd ../../org.eclipse.oomph.extractor.lib/bin
zip -r -9 -qq  ../../org.eclipse.oomph.extractor/Libdata/libdata.jar *
cd -
cp ../../../products/org.eclipse.oomph.setup.installer.product/target/products/*64.zip ../Concat/product/product-64.zip