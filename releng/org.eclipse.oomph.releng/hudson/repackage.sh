#!/bin/bash

# Prepare archiving of the build results

rm -rf $WORKSPACE/updates
mkdir $WORKSPACE/updates
cp -a $WORKSPACE/git/sites/org.eclipse.oomph.site/target/repository/* $WORKSPACE/updates

cd $WORKSPACE/updates
echo "Zipping update site"
zip -r -9 -qq org.eclipse.oomph.site.zip * -x plugins/*.pack.gz

cd $WORKSPACE
for f in git/products/org.eclipse.oomph.setup.installer.product/target/products/*.zip; do
  echo "Repackaging $f"
  rm -rf $WORKSPACE/tmp
  mkdir $WORKSPACE/tmp

  unzip -qq $f -d $WORKSPACE/tmp
  zip --delete -qq $WORKSPACE/tmp/plugins/com.ibm.icu_*.jar 'com/*'

  inifile=oomph.ini
  if [[ $f == *macosx* ]]; then
    inifile=oomph.app/Contents/MacOS/$inifile
  fi

  sed -e 's/^Oomph.*Installer$/Oomph Installer/' $WORKSPACE/tmp/$inifile > $WORKSPACE/tmp/$inifile.tmp
  mv $WORKSPACE/tmp/$inifile.tmp $WORKSPACE/tmp/$inifile

  echo "-Doomph.installer.update.url=http://hudson.eclipse.org/oomph/job/integration/lastSuccessfulBuild/artifact/products/repository" >> $WORKSPACE/tmp/$inifile
  echo "-Doomph.update.url=http://hudson.eclipse.org/oomph/job/integration/lastSuccessfulBuild/artifact/updates" >> $WORKSPACE/tmp/$inifile

  cd $WORKSPACE
  rm $f

  cd $WORKSPACE/tmp
  if [[ $f == *macosx* ]]; then
    rm oomph
    ln -s oomph.app/Contents/MacOS/oomph oomph
    tar -czf ../$f *
    rename .zip .tar.gz ../$f
  else
    zip -r -9 -qq --symlinks ../$f *
  fi
done

rm -rf $WORKSPACE/tmp

rm -rf $WORKSPACE/products
mkdir $WORKSPACE/products
cp -a \
  $WORKSPACE/git/products/org.eclipse.oomph.setup.installer.product/target/products/*.zip \
  $WORKSPACE/git/products/org.eclipse.oomph.setup.installer.product/target/products/*.tar.gz \
  $WORKSPACE/products

rm -rf $WORKSPACE/help
mkdir $WORKSPACE/help

cd $WORKSPACE/git
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
