#!/bin/bash

##################################################################################################
#
# At this point $WORKSPACE points to the following build folder structure:
#
#   $WORKSPACE/
#   $WORKSPACE/help/
#
#   $WORKSPACE/updates/
#   $WORKSPACE/updates/features/
#   $WORKSPACE/updates/plugins/
#   $WORKSPACE/updates/artifacts.jar
#   $WORKSPACE/updates/content.jar
#   $WORKSPACE/updates/org.eclipse.oomph.site.zip
#
#   $WORKSPACE/products/
#   $WORKSPACE/products/index.txt
#   $WORKSPACE/products/org.eclipse.oomph.setup.installer.product-linux.gtk.x86.zip
#   $WORKSPACE/products/org.eclipse.oomph.setup.installer.product-linux.gtk.x86_64.zip
#   $WORKSPACE/products/org.eclipse.oomph.setup.installer.product-macosx.cocoa.x86_64.tar.gz
#   $WORKSPACE/products/org.eclipse.oomph.setup.installer.product-win32.win32.x86.zip
#   $WORKSPACE/products/org.eclipse.oomph.setup.installer.product-win32.win32.x86_64.zip
#   $WORKSPACE/products/repository/
#   $WORKSPACE/products/repository/binary/
#   $WORKSPACE/products/repository/features/
#   $WORKSPACE/products/repository/plugins/
#   $WORKSPACE/products/repository/artifacts.jar
#   $WORKSPACE/products/repository/content.jar
#
##################################################################################################


if [[ "$BUILD_LABEL" == "" ]]; then
  BUILD_LABEL=`echo $BUILD_ID | sed 's/\([0-9]*\)-\([0-9]*\)-\([0-9]*\)_\([0-9]*\)-\([0-9]*\)-[0-9]*/N\1\2\3-\4\5/g'`
fi

DOWNLOADS=/home/data/httpd/download.eclipse.org/oomph/promotest
HELP=$DOWNLOADS/help
UPDATES=$DOWNLOADS/updates
PRODUCTS=$DOWNLOADS/products
DROPS=$DOWNLOADS/drops
DROP=$DROPS/$BUILD_TYPE/$BUILD_LABEL

echo "Promoting $WORKSPACE/updates"
rm -rf $DROP
mkdir $DROP
cp -a $WORKSPACE/updates $DROP
/bin/bash $WORKSPACE/git/org.eclipse.oomph/releng/org.eclipse.oomph.releng/hudson/adjustArtifactRepository.sh $DROP/updates "Oomph Updates $BUILD_LABEL"

rm -rf $PRODUCTS.tmp
mkdir $PRODUCTS.tmp

for f in $WORKSPACE/products/*; do
  echo "Promoting $f"
  rm -rf $WORKSPACE/tmp
  mkdir $WORKSPACE/tmp
  cd $WORKSPACE/tmp

  if [[ $f == *.zip ]]; then
    unzip -qq $f
  else
    tar -xzf $f
  fi

  inifile=oomph.ini
  if [[ $f == *macosx* ]]; then
    inifile=oomph.app/Contents/MacOS/$inifile
  fi

  head -n -2 $inifile > $inifile.tmp
  mv $inifile.tmp $inifile
  echo "-Doomph.installer.update.url=http://download.eclipse.org/oomph/products/repository" >> $inifile
  echo "-Doomph.update.url=http://download.eclipse.org/oomph/updates" >> $inifile

  if [[ $f == *.zip ]]; then
    zip -r -9 -q $PRODUCTS.tmp/$f *
  else
    tar -czf $PRODUCTS.tmp/$f *
  fi
done

rm -rf $WORKSPACE/tmp

echo "Promoting $WORKSPACE/help"
cp -a $WORKSPACE/help $HELP.tmp

if [[ "$BUILD_TYPE" == "release" ]]; then
  echo "Releasing $DROP/products"
  mkdir $DROP/products
  cp -a $PRODUCTS.tmp/* $DROP/products

  echo "Releasing $DROP/help"
  mkdir $DROP/help
  cp -a $HELP.tmp/* $DROP/help
fi

rm -rf $UPDATES.tmp
mkdir $UPDATES.tmp






mv $UPDATES.tmp $UPDATES
mv $PRODUCTS.tmp $PRODUCTS
mv $HELP.tmp $HELP
