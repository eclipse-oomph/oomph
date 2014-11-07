#!/bin/bash

# Prepare archiving of the build results

rm -rf updates
mkdir updates
cp -a git/sites/org.eclipse.oomph.site/target/repository/* updates

cd updates
echo "Zipping update site..."
zip -r -9 -qq org.eclipse.oomph.site.zip *
cd ..

for f in git/products/org.eclipse.oomph.setup.installer.product/target/products/*.zip; do
  echo "Repackaging $f..."
  rm -rf tmp
  mkdir tmp

  unzip -qq $f -d tmp
  zip --delete -qq tmp/plugins/com.ibm.icu_*.jar com/*

  inifile=oomph.ini
  if [[ $f == *macosx* ]]; then
    inifile=oomph.app/Contents/MacOS/$inifile
  fi

  sed -e 's/^Oomph.*Installer$/Oomph Installer/' tmp/$inifile > tmp/$inifile.tmp
  mv tmp/$inifile.tmp tmp/$inifile

  echo "-Doomph.installer.update.url=http://hudson.eclipse.org/oomph/job/integration/lastSuccessfulBuild/artifact/products/repository" >> tmp/$inifile
  echo "-Doomph.update.url=http://hudson.eclipse.org/oomph/job/integration/lastSuccessfulBuild/artifact/updates" >> tmp/$inifile

  rm $f
  cd tmp
  if [[ $f == *macosx* ]]; then
    rm oomph
    ln -s oomph.app/Contents/MacOS/oomph oomph
    tar -czf ../$f *
    rename .zip .tar.gz ../$f
  else
    zip -r -9 -qq --symlinks ../$f *
  fi
  cd ..
done

rm -rf tmp
rm -rf products
mkdir products
cp -a git/products/org.eclipse.oomph.setup.installer.product/target/products/*.zip git/products/org.eclipse.oomph.setup.installer.product/target/products/*.tar.gz products
cd products
ls * > index.txt

cp -a ../git/products/org.eclipse.oomph.setup.installer.product/target/repository .
cd repository
echo "Zipping product repository..."
zip -r -9 -qq repository.zip *

cd ../..
rm -rf help.zip
rm -rf help
mkdir help
cd git

cp releng/org.eclipse.oomph.releng.helpcenter/html/* ../help
cp releng/org.eclipse.oomph.releng.helpcenter/docs.txt ../help/.docs

for i in $( cat releng/org.eclipse.oomph.releng.helpcenter/docs.txt ); do
  echo "Unzipping $i..."
  unzip -qq plugins/$i/target/$i-*-SNAPSHOT.jar \
    "javadoc/*" \
    "schemadoc/*" \
    "html/*" \
    "images/*" \
    "about.html" \
    "plugin.properties" \
    -d ../help/$i || echo "Ok"

  for j in $( find ../help/$i/html -name "*.html" ); do
    sed -e 's/<!-- \(<div class="help_breadcrumbs.*\) -->/\1/g' $j > $j.tmp
    mv $j.tmp $j
  done
done

cd ..

echo "Zipping help center..."
zip -r -9 -qq help.zip help
