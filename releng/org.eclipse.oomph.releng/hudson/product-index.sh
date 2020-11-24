#!/bin/sh

root=/home/data/httpd/download.eclipse.org/oomph/epp
cd $root

named_releases=$(for i in *; do echo $i | grep "^[a-z]*$"; done)
numbered_releases=$(for i in *; do echo $i | grep "^[0-9]*-[0-9]*$"; done)

echo '<?xml version="1.0" encoding="UTF-8"?>'
echo "<index>"
echo ""

for i in $named_releases $numbered_releases; do
  cd $root/$i;

  milestones=$(for i in *; do echo $i | grep "^M[0-9-]*$"; done)
  release_candidates=$(for i in *; do echo $i | grep "^RC[0-9]*$"; done)
  releases=$(for i in *; do echo $i | grep "^R[az]*$"; done)

  latest=$(for j in $milestones $release_candidates $releases; do echo $j; done | tail -1)


  for j in $latest;  do
    cd $root/$i/$j
    #echo $i/$j
    #ls eclipse-inst*
    for b in 32 64 aarch64; do
      for os in win mac linux; do
        for jre in "true" "false"; do
          if [ "$jre" == "false" ]; then
            prefix="eclipse-inst-"
          else
            prefix="eclipse-inst-jre-"
          fi

          if [ -f $prefix$os$b.exe ]; then
            candidate=$prefix$os$b.exe
          elif [ -f $prefix$os$b.dmg ]; then
            candidate=$prefix$os$b.dmg
          elif [ -f $prefix$os-$b.tar.gz ]; then
            candidate=$prefix$os-$b.tar.gz
          elif [ -f $prefix$os$b.tar.gz ]; then
            if [ "$os" == "mac" -a -f eclipse-inst-$os$b.dmg ]; then
              # Don't include mac *.tar.gz if there is a *.dmg
              candidate=""
            else
              candidate=$prefix$os$b.tar.gz
            fi
          else
            candidate=""
          fi

          if [ "$candidate" != "" ]; then
            #echo "  >> $i/$j/$(ls $candidate)"
            echo "  <installer release=\"$i/$j\" os=\"$os\" bitness=\"$b\" jre=\"$jre\" url=\"https://www.eclipse.org/downloads/download.php?file=/oomph/epp/$i/$j/$candidate\"/>"
          fi
        done
      done
    done
  done

  echo ""

done

echo "</index>"
