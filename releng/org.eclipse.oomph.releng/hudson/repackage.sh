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

SOURCE=$GIT/products/org.eclipse.oomph.setup.installer.product/target/products
cd $SOURCE

TMP=$WORKSPACE/eclipse-installer

for f in *.zip; do
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
      zip -r -q unsigned.zip "Eclipse Installer.app"
      rm -rf "Eclipse Installer.app"
      curl -o signed.zip -F file=@unsigned.zip http://build.eclipse.org:31338/macsign.php
      
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

    chmod a+x "Eclipse Installer.app/Contents/MacOS/eclipse-inst"
    tar -czf $PRODUCTS/eclipse-inst-mac$bitness.tar.gz "Eclipse Installer.app"

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

    zip -r -9 -qq --symlinks $PRODUCTS/$f *

    extractor=eclipse-inst-win$bitness.exe
    marker=$GIT/plugins/org.eclipse.oomph.extractor/marker.txt

    cp -a /opt/public/tools/oomph/extractor-$bitness.exe extractor.exe
    zip -9 -qq $PRODUCTS/$f extractor.exe

    echo "  Creating $extractor"
    cat /opt/public/tools/oomph/extractor-$bitness.exe \
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
cp -a $GIT/products/org.eclipse.oomph.setup.installer.product/target/repository $PRODUCTS

rm -rf $WORKSPACE/help
mkdir $WORKSPACE/help

cd $GIT
cp releng/org.eclipse.oomph.releng.helpcenter/html/* $WORKSPACE/help
cp releng/org.eclipse.oomph.releng.helpcenter/docs.txt $WORKSPACE/help/.docs

for i in $( cat releng/org.eclipse.oomph.releng.helpcenter/docs.txt ); do
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

