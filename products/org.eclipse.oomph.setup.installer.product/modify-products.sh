#!/bin/bash
echo "Modify the projects"
pwd

exec 2>&1
set -e
set -x

for i in $(find . -name "eclipsec.exe"); do
  echo $i
  mv "$i" "$(dirname $i)/eclipse-instc.exe"
done

OLD_IFS=$IFS
IFS=$'\n'
for i in $(find . -name "eclipse-inst.ini"); do
  echo $i
  grep -e "Eclipse" "$i"
  LC_ALL=C sed -e 's/^Eclipse.*Installer$/Eclipse Installer/g;' "$i" > "$i.tmp"
  if [[ "$BUILD_TYPE" == "" || "$BUILD_TYPE" == none || "$BUILD_TYPE" == nightly ]]; then
    echo "-Doomph.installer.update.url=http://download.eclipse.org/oomph/products/latest/repository" >> "$i.tmp"
    echo "-Doomph.update.url=http://download.eclipse.org/oomph/updates/latest" >> "$i.tmp"
  fi
  mv "$i.tmp" "$i"
done

if [[ "$RESTRICTED_VERSION" != "none" ]]; then
  for i in $(find . -name "config.ini"); do
    if [[ $i == *.restricted* ]]; then
      echo $i
      script='s/^\(oomph.setup.product.version.filter=$\)/\1.*\\\\.'$RESTRICTED_VERSION'/;' 
      script='s/^\(oomph.setup.product.catalog.filter=$\)/\1org\\\\.eclipse\\\\.products/;'"$script"
      script='s/^\(oomph.setup.product.filter=$\)/\1(?\!org\\\\.eclipse\\\\.products\\\\.org\\\\.eclipse\\\\.platform\\\\.ide).*/;'"$script"
      sed -e \
        "$script" \
        $i > $i.tmp
      mv $i.tmp $i
    fi
  done
fi

IFS=$OLD_IFS
