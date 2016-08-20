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
#   $WORKSPACE/products/eclipse-inst-linux32.tar.gz
#   $WORKSPACE/products/eclipse-inst-linux64.tar.gz
#   $WORKSPACE/products/eclipse-inst-mac64.tar.gz
#   $WORKSPACE/products/eclipse-inst-win32.exe
#   $WORKSPACE/products/eclipse-inst-win64.exe
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

PRODUCTS_TMP=$PRODUCTS.tmp
HELP_TMP=$HELP.tmp
UPDATES_TMP=$UPDATES.tmp

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
  "Oomph $FOLDER" \
  $BUILD_TYPE
$BASH $SCRIPTS/adjustContentRepository.sh \
  $DROP \
  "Oomph $FOLDER"

######################
# DOWNLOADS/PRODUCTS #
######################

cd $WORKSPACE
rm -rf $PRODUCTS_TMP
mkdir $PRODUCTS_TMP
mkdir $PRODUCTS_TMP/latest

cd $WORKSPACE/products
for f in *.exe *.tar.gz; do
  echo "Promoting $f to $PRODUCTS_TMP/latest"
  cp -a $f $PRODUCTS_TMP/latest

  if [[ "$BUILD_TYPE" != nightly ]]; then
    echo "Promoting $f to $PRODUCTS_TMP"
    cp -a $f $PRODUCTS_TMP
  fi
done

cd $WORKSPACE
cp -a $PROPERTIES $PRODUCTS_TMP/latest/product.properties
cp -a $WORKSPACE/products/repository $PRODUCTS_TMP/latest
$BASH $SCRIPTS/adjustArtifactRepository.sh \
  $PRODUCTS_TMP/latest/repository \
  $PRODUCTS/latest/repository \
  "Eclipse Installer $FOLDER" \
  $BUILD_TYPE
$BASH $SCRIPTS/adjustContentRepository.sh \
  $PRODUCTS_TMP/latest/repository \
  "Eclipse Installer $FOLDER"

if [[ "$BUILD_TYPE" != nightly ]]; then
  cp -a $PROPERTIES $PRODUCTS_TMP/product.properties
  cp -a $WORKSPACE/products/repository $PRODUCTS_TMP
  $BASH $SCRIPTS/adjustArtifactRepository.sh \
    $PRODUCTS_TMP/repository \
    $PRODUCTS/repository \
    "Eclipse Installer $FOLDER" \
    $BUILD_TYPE
  $BASH $SCRIPTS/adjustContentRepository.sh \
    $PRODUCTS_TMP/repository \
    "Eclipse Installer $FOLDER"
else
  cp -a $PRODUCTS/product.properties $PRODUCTS_TMP
  cp -a $PRODUCTS/*.exe $PRODUCTS_TMP
  cp -a $PRODUCTS/*.tar.gz $PRODUCTS_TMP
  cp -a $PRODUCTS/repository $PRODUCTS_TMP
fi

##################
# DOWNLOADS/HELP #
##################

echo "Promoting $WORKSPACE/help"
cd $WORKSPACE
rm -rf $HELP_TMP
mkdir $HELP_TMP
cp -a $WORKSPACE/help/* $HELP_TMP

#####################
# DOWNLOADS/UPDATES #
#####################

cd $WORKSPACE
rm -rf $UPDATES_TMP
cp -a $UPDATES $UPDATES_TMP

if [[ "$BUILD_TYPE" == release ]]; then
  echo "Releasing $DROP/products"
  mkdir $DROP/products
  cp -a $PRODUCTS_TMP/* $DROP/products
  $BASH $SCRIPTS/adjustArtifactRepository.sh \
    $DROP/products/repository \
    $DROP/products/repository \
    "Eclipse Installer $FOLDER" \
    $BUILD_TYPE
  $BASH $SCRIPTS/adjustContentRepository.sh \
    $DROP/products/repository \
    "Eclipse Installer $FOLDER"

  echo "Releasing $DROP/help"
  mkdir $DROP/help
  cp -a $HELP_TMP/* $DROP/help
fi

$BASH $SCRIPTS/composeRepositories.sh \
  "$DOWNLOADS" \
  "$BUILD_TYPE" \
  "$BUILD_KEY" \
  "$BUILD_LABEL"

mkdir -p $UPDATES_TMP/$BUILD_TYPE/latest
cp -a $DROP/org.eclipse.oomph.site.zip $UPDATES_TMP/$BUILD_TYPE/latest

mkdir -p $UPDATES_TMP/latest
cp -a $DROP/org.eclipse.oomph.site.zip $UPDATES_TMP/latest

rm -rf $UPDATES.bak
mv $UPDATES $UPDATES.bak; mv $UPDATES_TMP $UPDATES
rm -rf $UPDATES.bak

rm -rf $PRODUCTS.bak
mv $PRODUCTS $PRODUCTS.bak; mv $PRODUCTS_TMP $PRODUCTS
rm -rf $PRODUCTS.bak

rm -rf $HELP.bak
mv $HELP $HELP.bak; mv $HELP_TMP $HELP
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
