#!/bin/bash
echo "Repackage all the products"
pwd

exec 2>&1
set -e
set -x

cd ../..
GIT=$PWD
cd -

mkdir -p target/repackaged-products
cd target/products

for i in $(ls); do
  echo "Processing $i"

  if [[ $i == *restricted* ]]; then
    restricted="-restricted"
  else
    restricted=""
  fi

  if [[ $i == *.zip ]]; then
    if [[ $i == *with-jre* ]]; then
      cp $i ../repackaged-products/eclipse-inst-jre$restricted-win64.zip
      extractor=eclipse-inst-jre$restricted-win64.exe
      descriptor=descriptor-tar-64.txt
      mkdir -p tmp
      cd tmp
      unzip -qq ../$i
      tar -cf ../${i%.zip}.tar *
      cd ..
      rm -rf tmp
      product=${i%.zip}.tar
    else
      cp $i ../repackaged-products/eclipse-inst$restricted-win64.zip
      extractor=eclipse-inst$restricted-win64.exe
      descriptor=descriptor-64.txt
      product=$i
    fi

    marker=$GIT/plugins/org.eclipse.oomph.extractor/marker.txt

    echo "Creating $extractor"
    cat $GIT/plugins/org.eclipse.oomph.extractor/extractor-64.exe \
      $marker \
      $GIT/plugins/org.eclipse.oomph.extractor.lib/target/org.eclipse.oomph.extractor.lib-*-SNAPSHOT.jar \
      $marker \
      $GIT/plugins/org.eclipse.oomph.extractor/Concat/$descriptor \
      $marker \
      $product \
      $marker > ../repackaged-products/$extractor

  elif [[ $i == *mac*.dmg ]]; then
    if [[ $i == *with-jre* ]]; then
      cp $i ../repackaged-products/eclipse-inst-jre$restricted-mac64.dmg
    else
      cp $i ../repackaged-products/eclipse-inst$restricted-mac64.dmg
    fi
  elif [[ $i == *mac* ]]; then
    if [[ $i == *with-jre* ]]; then
      cp $i ../repackaged-products/eclipse-inst-jre$restricted-mac64.tar.gz
    else
      cp $i ../repackaged-products/eclipse-inst$restricted-mac64.tar.gz
    fi
  elif [[ $i == *linux* ]]; then
    if [[ $i == *with-jre* ]]; then
      cp $i ../repackaged-products/eclipse-inst-jre$restricted-linux64.tar.gz
    else
      cp $i ../repackaged-products/eclipse-inst$restricted-linux64.tar.gz
    fi
  fi
done
