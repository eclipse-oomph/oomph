#!/bin/bash

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

echo ""

DROPS=$DOWNLOADS/drops
UPDATES=$DOWNLOADS/updates
UPDATES_TMP=$UPDATES.tmp

#####################
# DOWNLOADS/UPDATES #
#####################

rm -rf $UPDATES_TMP
cp -a $UPDATES $UPDATES_TMP

$BASH $SCRIPTS/composeRepositories.sh \
  "$DOWNLOADS" \
  "cleanup" \
  "" \
  ""

mv $UPDATES $UPDATES.bak; mv $UPDATES_TMP $UPDATES

for t in nightly milestone; do
  for f in $DROPS/$t/*; do
    if [[ -f $f/REMOVE ]]; then
      echo "Deleting $f"
      rm -rf $f
    fi
  done
done

echo ""
