#!/bin/bash

if [[ "$BUILD_KEY" == "" ]]; then
  if [[ "$BUILD_TYPE" == milestone ]]; then
    TYPE="S"
  elif [[ "$BUILD_TYPE" == nightly ]]; then
    TYPE="N"
  fi

  if [[ "$TYPE" != "" ]]; then
    BUILD_KEY=$TYPE`echo $BUILD_ID | sed 's/\([0-9]*\)-\([0-9]*\)-\([0-9]*\)_\([0-9]*\)-\([0-9]*\)-\([0-9]*\)/\1\2\3-\4\5\6/g'`
  fi
fi

if [[ "$BUILD_LABEL" == "" ]]; then
  BUILD_LABEL=""
fi

FOLDER=$BUILD_KEY
if [[ "$BUILD_LABEL" != "" ]]; then
  FOLDER=$FOLDER-$BUILD_LABEL
fi

if [[ "$GIT" == "" ]]; then
  GIT=$WORKSPACE/git
fi

if [[ "$SCRIPTS" == "" ]]; then
  SCRIPTS=$GIT/releng/org.eclipse.oomph.releng/hudson
fi

if [[ "$DOWNLOADS" == "" ]]; then
  DOWNLOADS=/home/data/httpd/download.eclipse.org/oomph
fi

set -o nounset
set -o errexit

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

echo ""

PROPERTIES=$WORKSPACE/updates/repository.properties
echo "branch = $GIT_BRANCH" >> $PROPERTIES
echo "commit = $GIT_COMMIT" >> $PROPERTIES
echo "number = $BUILD_NUMBER" >> $PROPERTIES
echo "key = $BUILD_KEY" >> $PROPERTIES
echo "label = $BUILD_LABEL" >> $PROPERTIES

HELP=$DOWNLOADS/help
UPDATES=$DOWNLOADS/updates
PRODUCTS=$DOWNLOADS/products
DROPS=$DOWNLOADS/drops
DROP_TYPE=$DROPS/$BUILD_TYPE
DROP=$DROP_TYPE/$FOLDER

mkdir -p $DOWNLOADS
mkdir -p $HELP
mkdir -p $UPDATES
mkdir -p $PRODUCTS
mkdir -p $DROPS
mkdir -p $DROP_TYPE

###################
# DOWNLOADS/DROPS #
###################

echo "Promoting $WORKSPACE/updates"
rm -rf $DROP
mkdir $DROP
cp -a $WORKSPACE/updates/* $DROP
$BASH $SCRIPTS/adjustArtifactRepository.sh \
  $DROP \
  $DROP \
  "Oomph Updates $FOLDER" \
  $BUILD_TYPE

######################
# DOWNLOADS/PRODUCTS #
######################

cd $WORKSPACE
rm -rf $PRODUCTS.tmp
mkdir $PRODUCTS.tmp

cd $WORKSPACE/products
for f in *.zip *.tar.gz; do
  echo "Promoting $f"

  cd $WORKSPACE
  rm -rf $WORKSPACE/tmp
  mkdir $WORKSPACE/tmp
  cd $WORKSPACE/tmp

  if [[ $f == *.zip ]]; then
    unzip -qq $WORKSPACE/products/$f
  else
    tar -xzf $WORKSPACE/products/$f
  fi

  inifile=oomph.ini
  if [[ $f == *macosx* ]]; then
    inifile=oomph.app/Contents/MacOS/$inifile

    echo "  Signing oomph.app"
    zip -r -9 -q unsigned.zip oomph.app
    rm -rf oomph.app
    curl -o signed.zip -F filedata=@unsigned.zip http://build.eclipse.org:31338/macsign.php
    unzip -qq signed.zip
    rm -f signed.zip
  elif [[ $f == *win32* ]]; then
    rm -f eclipsec.exe

    echo "  Signing oomph.exe"
    curl -o signed.exe -F filedata=@oomph.exe http://build.eclipse.org:31338/winsign.php
    mv signed.exe oomph.exe
  fi

  head -n -2 $inifile > $inifile.tmp
  mv $inifile.tmp $inifile
  echo "-Doomph.installer.update.url=http://download.eclipse.org/oomph/products/repository" >> $inifile
  echo "-Doomph.update.url=http://download.eclipse.org/oomph/updates/latest" >> $inifile

  if [[ $f == *.zip ]]; then
    zip -r -9 -q $PRODUCTS.tmp/$f *
  else
    tar -czf $PRODUCTS.tmp/$f *
  fi
done

cd $WORKSPACE
rm -rf $WORKSPACE/tmp

cp -a $WORKSPACE/products/repository $PRODUCTS.tmp
$BASH $SCRIPTS/adjustArtifactRepository.sh \
  $PRODUCTS.tmp/repository \
  $PRODUCTS/repository \
  "Oomph Product Updates" \
  $BUILD_TYPE


##################
# DOWNLOADS/HELP #
##################

echo "Promoting $WORKSPACE/help"
cd $WORKSPACE
rm -rf $HELP.tmp
mkdir $HELP.tmp
cp -a $WORKSPACE/help/* $HELP.tmp

#####################
# DOWNLOADS/UPDATES #
#####################

cd $WORKSPACE
rm -rf $UPDATES.tmp
cp -a $UPDATES $UPDATES.tmp

if [[ "$BUILD_TYPE" == release ]]; then
  echo "Releasing $DROP/products"
  mkdir $DROP/products
  cp -a $PRODUCTS.tmp/* $DROP/products
  $BASH $SCRIPTS/adjustArtifactRepository.sh \
    $DROP/products/repository \
    $DROP/products/repository \
    "Oomph $FOLDER Product Updates" \
    $BUILD_TYPE

  echo "Releasing $DROP/help"
  mkdir $DROP/help
  cp -a $HELP.tmp/* $DROP/help
fi

$BASH $SCRIPTS/composeRepositories.sh \
  "$DOWNLOADS" \
  "$BUILD_TYPE" \
  "$BUILD_KEY" \
  "$BUILD_LABEL"

mv $UPDATES $UPDATES.bak; mv $UPDATES.tmp $UPDATES
mv $PRODUCTS $PRODUCTS.bak; mv $PRODUCTS.tmp $PRODUCTS
mv $HELP $HELP.bak; mv $HELP.tmp $HELP

cd $WORKSPACE
rm -rf $UPDATES.bak
rm -rf $PRODUCTS.bak
rm -rf $HELP.bak

for t in nightly milestone; do
  for f in $DROPS/$t/*; do
    if [[ -f $f/REMOVE ]]; then
      echo "Deleting $f"
      rm -rf $f
    fi
  done
done

echo ""
