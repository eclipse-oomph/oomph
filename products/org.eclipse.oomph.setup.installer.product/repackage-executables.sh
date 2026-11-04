#!/bin/bash
echo "Repackage Windows executable archives: $1"

exec 2>&1
set -e
set -x

cd "$2"
shopt -s nullglob

case "$1" in
  unpack)
    rm -rf executables-to-sign
    mkdir -p executables-to-sign
    for archive in extraArtifacts/*.executable.win32.win32.*.zip; do
      unzip -q "$archive" -d "executables-to-sign/$(basename "$archive" .zip)"
    done
    ;;
  pack)
    for archive in extraArtifacts/*.executable.win32.win32.*.zip; do
      name=$(basename "$archive")
      # Recreate the archive: ZIP timestamps have only two-second precision,
      # so updating it in place can miss a freshly signed executable.
      rm "$archive"
      (
        cd "executables-to-sign/${name%.zip}"
        zip -qr "../../extraArtifacts/$name" .
      )
    done
    rm -rf executables-to-sign
    ;;
  *)
    echo "Usage: $0 {unpack|pack} build-directory" >&2
    exit 1
    ;;
esac
