#!/bin/bash

if [[ "$GIT" == "" ]]; then
  GIT=$WORKSPACE/git
fi

if [[ "$NOTARIZE" == "" ]]; then
  NOTARIZE=false
fi

set -o nounset
set -o errexit

###########################################################################
echo ""

rm -rf $WORKSPACE/updates
mkdir $WORKSPACE/updates
cp -a $GIT/sites/org.eclipse.oomph.site/target/repository/* $WORKSPACE/updates

cd $WORKSPACE/updates
echo "Zipping update site"
zip -r -9 -qq org.eclipse.oomph.site.zip * -x plugins/*.pack.gz

PRODUCTS=$WORKSPACE/products
rm -rf $PRODUCTS
mkdir $PRODUCTS

TMP=$WORKSPACE/eclipse-installer

TIMESTAMP=$(date +%s%N)
echo "Copying repackaged-products"
cd $GIT/products/org.eclipse.oomph.setup.installer.product/target/repackaged-products/
for f in eclipse-inst*; do
  echo "Copying $f"
  if [[ $f == *.dmg && $NOTARIZE == true ]]; then
    echo "Notarizing $f"
    UNNOTARIZED_DMG=${f/.dmg/}.$TIMESTAMP.dmg
    mkdir -p $TMP
    cp -a $f $TMP/$UNNOTARIZED_DMG
    cd $TMP

    RESPONSE=$(curl -X POST -F file=@$UNNOTARIZED_DMG -F 'options={"primaryBundleId": "app-bundle", "staple": true};type=application/json' https://cbi.eclipse.org/macos/xcrun/notarize)
    UUID=$(echo $RESPONSE | grep -Po '"uuid"\s*:\s*"\K[^"]+')
    STATUS=$(echo $RESPONSE | grep -Po '"status"\s*:\s*"\K[^"]+')
    echo "  Progress: $RESPONSE"

    while [[ $STATUS == 'IN_PROGRESS' ]]; do
      sleep 1m
      RESPONSE=$(curl -s https://cbi.eclipse.org/macos/xcrun/$UUID/status)
      STATUS=$(echo $RESPONSE | grep -Po '"status"\s*:\s*"\K[^"]+')
      echo "  Progress: $RESPONSE"
    done

    if [[ $STATUS != 'COMPLETE' ]]; then
      echo "Notarization failed: $RESPONSE"
      exit 1
    else
      mv $UNNOTARIZED_DMG $UNNOTARIZED_DMG.unnotarized
      echo "  Downloading stapled result"
      curl -JO https://cbi.eclipse.org/macos/xcrun/$UUID/download
      ls -sail
      echo "  Copying stapled notarized result"
      cp -a $UNNOTARIZED_DMG $PRODUCTS/$f
    fi

    cd -
    rm -rf $TMP
  else
    cp -a $f $PRODUCTS
  fi
done

cp -a $GIT/products/org.eclipse.oomph.setup.installer.product/target/repository $PRODUCTS

rm -rf $WORKSPACE/help
mkdir $WORKSPACE/help

cd $GIT
cp releng/org.eclipse.oomph.releng.helpcenter/html/* $WORKSPACE/help
cp releng/org.eclipse.oomph.releng.helpcenter/docs.txt $WORKSPACE/help/.docs

for i in $( cat releng/org.eclipse.oomph.releng.helpcenter/docs.txt | sed 's/\r//' ); do
  echo "Unzipping $i"
  unzip -qq plugins/$i/target/$i-*-SNAPSHOT.jar \
    "javadoc/*" \
    "schemadoc/*" \
    "html/*" \
    "images/*" \
    "about.html" \
    "plugin.properties" \
    -d $WORKSPACE/help/$i || echo "Ok"

  for j in $( find $WORKSPACE/help/$i/html -name "*.html" ); do
    sed -e 's/<!-- \(<div class="help_breadcrumbs.*\) -->/\1/g' $j > $j.tmp
    mv $j.tmp $j
  done
done

echo ""
