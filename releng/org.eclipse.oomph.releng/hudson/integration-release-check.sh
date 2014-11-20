#!/bin/bash

if [[ "$RELEASE_VERSION == "" ]]; then
  echo "[ERROR] The version of the release is missing!"
  exit -1
fi

if [[ "$COMMIT_ID == "" ]]; then
  echo "[ERROR] The ID of the commit to build is missing!"
  exit -1
fi
