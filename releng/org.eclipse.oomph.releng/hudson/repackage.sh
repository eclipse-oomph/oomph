#!/bin/bash

if [[ "$GIT" == "" ]]; then
  GIT=$WORKSPACE/git
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

  unzip -qq $SOURCE/$f
  zip --delete -qq plugins/com.ibm.icu_*.jar 'com/*'

  inifile=oomph.ini
  if [[ $f == *macosx* ]]; then
    inifile=oomph.app/Contents/MacOS/$inifile
  fi

  sed -e 's/^Oomph.*Installer$/Oomph Installer/' $inifile > $inifile.tmp
  mv $inifile.tmp $inifile

  echo "-Doomph.installer.update.url=http://hudson.eclipse.org/oomph/job/integration/lastSuccessfulBuild/artifact/products/repository" >> $inifile
  echo "-Doomph.update.url=http://hudson.eclipse.org/oomph/job/integration/lastSuccessfulBuild/artifact/updates" >> $inifile

  if [[ $f == *macosx* ]]; then
    rm oomph
    ln -s oomph.app/Contents/MacOS/oomph oomph
    tar -czf $PRODUCTS/$f *
    rename .zip .tar.gz $PRODUCTS/$f
  else
    zip -r -9 -qq --symlinks $PRODUCTS/$f *
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

