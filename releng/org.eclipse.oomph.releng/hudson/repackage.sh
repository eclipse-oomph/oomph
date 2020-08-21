#!/bin/bash

if [[ "$GIT" == "" ]]; then
  GIT=$WORKSPACE/git
fi

if [[ "$PACK_AND_SIGN" == "" ]]; then
  PACK_AND_SIGN=false
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

# DISABLE THIS WHOLE PART!
if [[ false == true ]]; then
SOURCE=$GIT/products/org.eclipse.oomph.setup.installer.product/target/products
cd $SOURCE

for f in *.zip *.tar.gz; do
  echo "Repackaging $f"

  rm -rf $TMP
  mkdir $TMP
  cd $TMP

  if [[ $f == *x86_64* ]]; then
    bitness=64
  else
    bitness=32
  fi

  unzip -qq $SOURCE/$f

  inifile=eclipse-inst.ini
  if [[ $f == *macosx* ]]; then
    inifile="Eclipse Installer.app/Contents/Eclipse/$inifile"
  fi

  sed -e 's/^Eclipse.*Installer$/Eclipse Installer/' "$inifile" > "$inifile.tmp"
  mv "$inifile.tmp" "$inifile"

  if [[ "$BUILD_TYPE" == "" || "$BUILD_TYPE" == none || "$BUILD_TYPE" == nightly ]]; then
    echo "-Doomph.installer.update.url=http://download.eclipse.org/oomph/products/latest/repository" >> "$inifile"
    echo "-Doomph.update.url=http://download.eclipse.org/oomph/updates/latest" >> "$inifile"
  fi

  if [[ $f == *macosx* ]]; then
    if [[ "$PACK_AND_SIGN" == true ]]; then
      echo "  Signing Eclipse Installer.app"
      chmod -R a-st "Eclipse Installer.app"
      zip -r -q unsigned.zip "Eclipse Installer.app"
      rm -rf "Eclipse Installer.app"
      curl -o signed.zip -F file=@unsigned.zip -F entitlements=@$GIT/releng/org.eclipse.oomph.releng/hudson/installer.entitlements http://172.30.206.146:8282/macosx-signing-service/1.0.1-SNAPSHOT

      actualSize=$(wc -c signed.zip | cut -f 1 -d ' ')
      if [ $actualSize -lt 40000000 ]; then
        echo "signed.zip is just $actualSize bytes large!"
        echo ""
        cat signed.zip
        echo ""
        exit 1
      fi

      unzip -qq signed.zip
      rm -f unsigned.zip signed.zip
    fi

    chmod -R a-st "Eclipse Installer.app"
    chmod a+x "Eclipse Installer.app/Contents/MacOS/eclipse-inst"
    echo "  Building eclipse-inst-mac$bitness.tar.gz"
    tar -czf $PRODUCTS/eclipse-inst-mac$bitness.tar.gz "Eclipse Installer.app"

    if [[ "$PACK_AND_SIGN" == true ]]; then
      echo "  Building eclipse-inst-mac$bitness.dmg"

      TIMESTAMP=$(date +%s%N)
      UNNOTARIZED_DMG=$PRODUCTS/eclipse-inst-mac$bitness.$TIMESTAMP.dmg
      curl -o $UNNOTARIZED_DMG --write-out '%{http_code}\n' -F sign=true -F source=@$PRODUCTS/eclipse-inst-mac$bitness.tar.gz http://build.eclipse.org:31338/dmg-packager

      echo "  Notarizing eclipse-inst-mac$bitness.$TIMESTAMP.dmg"
      RESPONSE=$(curl -X POST -F file=@$UNNOTARIZED_DMG -F 'options={"primaryBundleId": "app-bundle", "staple": true};type=application/json' http://172.30.206.146:8383/macos-notarization-service/notarize)
      UUID=$(echo $RESPONSE | grep -Po '"uuid"\s*:\s*"\K[^"]+')
      STATUS=$(echo $RESPONSE | grep -Po '"status"\s*:\s*"\K[^"]+')
      echo "  Progress: $RESPONSE"

      while [[ $STATUS == 'IN_PROGRESS' ]]; do
        sleep 1m
        RESPONSE=$(curl -s http://172.30.206.146:8383/macos-notarization-service/$UUID/status)
        STATUS=$(echo $RESPONSE | grep -Po '"status"\s*:\s*"\K[^"]+')
        echo "  Progress: $RESPONSE"
      done

      if [[ $STATUS != 'COMPLETE' ]]; then
        echo "Notarization failed: $RESPONSE"
        # Continue, but ignoring this one.
        # exit 1
      else
        mv $UNNOTARIZED_DMG $UNNOTARIZED_DMG.unnotarized
        echo "  Downloading stapled result"
        curl -JO http://172.30.206.146:8383/macos-notarization-service/$UUID/download
        echo "  Copying stapled notarized result"
        cp -a UNNOTARIZED_DMG $PRODUCTS
      fi
    fi

  elif [[ $f == *win32* ]]; then
    rm -f eclipsec.exe

    if [[ "$PACK_AND_SIGN" == true ]]; then
      echo "  Signing eclipse-inst.exe"
      curl -o signed.exe -F filedata=@eclipse-inst.exe http://build.eclipse.org:31338/winsign.php
      mv signed.exe eclipse-inst.exe

      actualSize=$(wc -c eclipse-inst.exe | cut -f 1 -d ' ')
      if [ $actualSize -lt 200000 ]; then
        echo "eclipse-inst.exe is just $actualSize bytes large!"
        echo ""
        cat eclipse-inst.exe
        echo ""
        exit 1
      fi
    fi

    if [[ $OSTYPE == cygwin ||  $OSTYPE = msys ]]; then
      ZIP_OPTIONS="-r -9 -qq"
    else
      ZIP_OPTIONS="-r -9 -qq --symlinks"
    fi

    zip $ZIP_OPTIONS $PRODUCTS/$f *

    extractor=eclipse-inst-win$bitness.exe
    marker=$GIT/plugins/org.eclipse.oomph.extractor/marker.txt

    cp -a $GIT/plugins/org.eclipse.oomph.extractor/extractor-$bitness.exe extractor.exe

    echo "  Creating $extractor"
    cat extractor.exe \
      $marker \
      $GIT/plugins/org.eclipse.oomph.extractor.lib/target/org.eclipse.oomph.extractor.lib-*-SNAPSHOT.jar \
      $marker \
      $GIT/plugins/org.eclipse.oomph.extractor/Concat/descriptor-$bitness.txt \
      $marker \
      $PRODUCTS/$f \
      $marker > $PRODUCTS/$extractor

    rm -f $PRODUCTS/$f

    if [[ "$PACK_AND_SIGN" == true ]]; then
      echo "  Signing $extractor"
      curl -o $PRODUCTS/$extractor-signed -F filedata=@$PRODUCTS/$extractor http://build.eclipse.org:31338/winsign.php
      cp -a $PRODUCTS/$extractor $WORKSPACE
      mv $PRODUCTS/$extractor-signed $PRODUCTS/$extractor

      actualSize=$(wc -c "$PRODUCTS/$extractor" | cut -f 1 -d ' ')
      if [ $actualSize -lt 40000000 ]; then
        echo "$PRODUCTS/$extractor is just $actualSize bytes large!"
        echo ""
        cat "$PRODUCTS/$extractor"
        echo ""
        exit 1
      fi
    fi

  elif [[ $f == *linux* ]]; then
    cd ..
    tar -czf $PRODUCTS/eclipse-inst-linux$bitness.tar.gz eclipse-installer
    cd $TMP
  fi
done

rm -rf $TMP
# DISABLE THIS WHOLE PART!
else
  TIMESTAMP=$(date +%s%N)
  echo "Copying repackaged-products"
  cd $GIT/products/org.eclipse.oomph.setup.installer.product/target/repackaged-products/
  for f in eclipse-inst*; do
    echo "Copying $f"
    if [[ $f == *.dmg ]]; then
      echo "Notarizing $f"
      UNNOTARIZED_DMG=${f/.dmg/}.$TIMESTAMP.dmg
      mkdir -p $TMP
      cp -a $f $TMP/$UNNOTARIZED_DMG
      cd $TMP

      RESPONSE=$(curl -X POST -F file=@$UNNOTARIZED_DMG -F 'options={"primaryBundleId": "app-bundle", "staple": true};type=application/json' http://172.30.206.146:8383/macos-notarization-service/notarize)
      UUID=$(echo $RESPONSE | grep -Po '"uuid"\s*:\s*"\K[^"]+')
      STATUS=$(echo $RESPONSE | grep -Po '"status"\s*:\s*"\K[^"]+')
      echo "  Progress: $RESPONSE"

      while [[ $STATUS == 'IN_PROGRESS' ]]; do
        sleep 1m
        RESPONSE=$(curl -s http://172.30.206.146:8383/macos-notarization-service/$UUID/status)
        STATUS=$(echo $RESPONSE | grep -Po '"status"\s*:\s*"\K[^"]+')
        echo "  Progress: $RESPONSE"
      done

      if [[ $STATUS != 'COMPLETE' ]]; then
        echo "Notarization failed: $RESPONSE"
        exit 1
      fi

      mv $UNNOTARIZED_DMG $UNNOTARIZED_DMG.unnotarized

      echo "  Downloading stapled result"

      curl -JO http://172.30.206.146:8383/macos-notarization-service/$UUID/download

      ls -sail

      echo "  Copying stapled notarized result"

      cp -a $UNNOTARIZED_DMG $PRODUCTS

      cd -
      rm -rf $TMP
    else
      cp -a $f $PRODUCTS
    fi
  done
fi

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
