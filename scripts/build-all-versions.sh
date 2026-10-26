#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

VERSIONS=(1.21.1 1.21.2 1.21.3 1.21.4 1.21.5)

merge_properties() {
  local mc="$1"
  cat gradle/versions/common.properties gradle/versions/"${mc}.properties" > gradle.properties
}

mkdir -p release

for mc in "${VERSIONS[@]}"; do
  echo "========== Minecraft ${mc} =========="
  merge_properties "$mc"
  if grep -q '^skip_neoforge=true' gradle.properties; then
    ./gradlew --no-daemon clean :fabric:remapJar
  else
    ./gradlew --no-daemon clean :fabric:remapJar :neoforge:remapJar
  fi
  out="release/${mc}"
  mkdir -p "$out"
  cp fabric/build/libs/letterrush-*+fabric.jar "$out/"
  if compgen -G "neoforge/build/libs/letterrush-*+neoforge.jar" > /dev/null; then
    cp neoforge/build/libs/letterrush-*+neoforge.jar "$out/"
  fi
  ls -la "$out"
done

# NeoForge has no 1.21.2 artifact in this repo — ship the 1.21.3 jar as substitute.
if [[ -f release/1.21.3/letterrush-1.0.0-1.21.3+neoforge.jar ]]; then
  cp release/1.21.3/letterrush-1.0.0-1.21.3+neoforge.jar release/1.21.2/
  echo "NeoForge 1.21.2: using letterrush-1.0.0-1.21.3+neoforge.jar (substitute)"
fi

merge_properties "1.21.5"
echo "Done. Artifacts under release/<version>/"
