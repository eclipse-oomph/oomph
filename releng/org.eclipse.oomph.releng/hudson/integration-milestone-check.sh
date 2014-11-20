#!/bin/bash

if [[ "$MILESTONE_NAME == "" ]]; then
  echo "[ERROR] The milestone name is missing!"
  exit -1
fi

if [[ "$COMMIT_ID == "" ]]; then
  echo "[ERROR] The commit ID is missing!"
  exit -1
fi
