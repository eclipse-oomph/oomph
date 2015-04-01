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

for f in *.zip; do
  echo "Repackaging $f"

  rm -rf $PRODUCTS.tmp
  mkdir $PRODUCTS.tmp
  cd $PRODUCTS.tmp

  if [[ $f == *x86_64* ]]; then
    bitness=64
  else
    bitness=32              
  fi
  
  unzip -qq $SOURCE/$f
  #zip --delete -qq plugins/com.ibm.icu_*.jar 'com/*'

  inifile=oomph.ini
  if [[ $f == *macosx* ]]; then
    inifile=oomph.app/Contents/MacOS/$inifile
  fi

  sed -e 's/^Eclipse.*Installer$/Eclipse Installer/' $inifile > $inifile.tmp
  mv $inifile.tmp $inifile

  echo "-Doomph.installer.update.url=http://download.eclipse.org/oomph/products/repository" >> $inifile
  echo "-Doomph.update.url=http://download.eclipse.org/oomph/updates/latest" >> $inifile

  if [[ $f == *macosx* ]]; then
    #if [[ "$PACK_AND_SIGN" == true ]]; then
    #   MacOS executable signing is currently broken!
    #   See https://bugs.eclipse.org/bugs/show_bug.cgi?id=446390
    #  
    #  echo "  Signing oomph.app"
    #  zip -r -q unsigned.zip oomph.app
    #  rm -rf oomph.app
    #  curl -o signed.zip -F filedata=@unsigned.zip http://build.eclipse.org:31338/macsign.php
    #  unzip -qq signed.zip
    #  rm -f signed.zip
    #fi
    
    rm oomph
    ln -s oomph.app/Contents/MacOS/oomph oomph
    tar -czf $PRODUCTS/eclipse-installer-mac$bitness.tar.gz *

  elif [[ $f == *win32* ]]; then
    rm -f eclipsec.exe

    if [[ "$PACK_AND_SIGN" == true ]]; then
      echo "  Signing oomph.exe"
      curl -o signed.exe -F filedata=@oomph.exe http://build.eclipse.org:31338/winsign.php
      mv signed.exe oomph.exe
    fi
    
    zip -r -9 -qq --symlinks $PRODUCTS/$f *
    
    extractor=eclipse-installer-win$bitness.exe
    marker=$GIT/plugins/org.eclipse.oomph.extractor/marker.txt
    
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
      mv $PRODUCTS/$extractor-signed $PRODUCTS/$extractor
      
      actualSize=$(wc -c "$PRODUCTS/$extractor" | cut -f 1 -d ' ')
      if [ $actualSize -lt 40000000 ]; then
        echo "$PRODUCTS/$extractor is just $actualSize bytes large!"
        exit 1
      fi
    fi

  elif [[ $f == *linux* ]]; then
    zip -r -9 -qq --symlinks $PRODUCTS/eclipse-installer-linux$bitness.zip *
  fi
done

rm -rf $PRODUCTS.tmp
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

