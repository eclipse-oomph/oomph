#!/bin/bash

if [[ "$BUILD_TYPE" == "" || "$BUILD_TYPE" == none ]]; then
  BUILD_TYPE=nightly
fi

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
#   $WORKSPACE/products/eclipse-installer-linux32.tar.gz
#   $WORKSPACE/products/eclipse-installer-linux64.tar.gz
#   $WORKSPACE/products/eclipse-installer-mac64.tar.gz
#   $WORKSPACE/products/eclipse-installer-win32.exe
#   $WORKSPACE/products/eclipse-installer-win64.exe
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

BUILDS=$DOWNLOADS/builds
HELP=$DOWNLOADS/help
UPDATES=$DOWNLOADS/updates
PRODUCTS=$DOWNLOADS/products
DROPS=$DOWNLOADS/drops
DROP_TYPE=$DROPS/$BUILD_TYPE
DROP=$DROP_TYPE/$FOLDER

mkdir -p $DOWNLOADS
mkdir -p $BUILDS
mkdir -p $HELP
mkdir -p $UPDATES
mkdir -p $PRODUCTS
mkdir -p $DROPS
mkdir -p $DROP_TYPE

cp -a $PROPERTIES $BUILDS/$BUILD_NUMBER.properties

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
mkdir $PRODUCTS.tmp/latest

cd $WORKSPACE/products
for f in *.exe *.tar.gz; do
  echo "Promoting $f to $PRODUCTS.tmp/latest"
  cp -a $f $PRODUCTS.tmp/latest

  if [[ "$BUILD_TYPE" != nightly ]]; then
    echo "Promoting $f to $PRODUCTS.tmp"
    cp -a $f $PRODUCTS.tmp
  fi
done

cd $WORKSPACE
cp -a $PROPERTIES $PRODUCTS.tmp/latest/product.properties
cp -a $WORKSPACE/products/repository $PRODUCTS.tmp/latest
$BASH $SCRIPTS/adjustArtifactRepository.sh \
  $PRODUCTS.tmp/latest/repository \
  $PRODUCTS/latest/repository \
  "Oomph Product Updates Latest" \
  $BUILD_TYPE

if [[ "$BUILD_TYPE" != nightly ]]; then
  cp -a $PROPERTIES $PRODUCTS.tmp/product.properties
  cp -a $WORKSPACE/products/repository $PRODUCTS.tmp
  $BASH $SCRIPTS/adjustArtifactRepository.sh \
    $PRODUCTS.tmp/repository \
    $PRODUCTS/repository \
    "Oomph Product Updates" \
    $BUILD_TYPE
else
  cp -a $PRODUCTS/*.exe $PRODUCTS.tmp
  cp -a $PRODUCTS/*.tar.gz $PRODUCTS.tmp
  cp -a $PRODUCTS/repository $PRODUCTS.tmp
fi

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

mkdir -p $UPDATES.tmp/$BUILD_TYPE/latest
cp -a $DROP/org.eclipse.oomph.site.zip $UPDATES.tmp/$BUILD_TYPE/latest

mkdir -p $UPDATES.tmp/latest
cp -a $DROP/org.eclipse.oomph.site.zip $UPDATES.tmp/latest

mv $UPDATES $UPDATES.bak; mv $UPDATES.tmp $UPDATES
mv $PRODUCTS $PRODUCTS.bak; mv $PRODUCTS.tmp $PRODUCTS
mv $HELP $HELP.bak; mv $HELP.tmp $HELP

cd $WORKSPACE
rm -rf $UPDATES.bak
rm -rf $PRODUCTS.bak
rm -rf $HELP.bak

cd $WORKSPACE
for t in nightly milestone; do
  for f in $DROPS/$t/*; do
    if [[ -f $f/REMOVE ]]; then
      echo "Deleting $f"
      rm -rf $f
    fi
  done
done

echo ""
