#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

# Supported release targets only
VERSIONS=(
  1.21.11
  26.1.2
  26.2
  26.3
)

merge_properties() {
  local mc="$1"
  cat gradle/versions/common.properties gradle/versions/"${mc}.properties" > gradle.properties
}

mkdir -p release

for mc in "${VERSIONS[@]}"; do
  echo "========== Minecraft ${mc} =========="
  merge_properties "$mc"
  if grep -q '^unobfuscated=true' gradle.properties; then
    if [[ -z "${JAVA_HOME:-}" ]] || ! "$JAVA_HOME/bin/java" -version 2>&1 | grep -q 'version "25'; then
      echo "Skip ${mc}: JDK 25 required (set JAVA_HOME). See docs/MINECRAFT-26.md"
      continue
    fi
  fi
  skip_neo=false
  if grep -q '^skip_neoforge=true' gradle.properties; then
    skip_neo=true
    ./gradlew --no-daemon clean :fabric:remapJar
  else
    ./gradlew --no-daemon clean :fabric:remapJar :neoforge:remapJar
  fi
  out="release/${mc}"
  mkdir -p "$out"
  rm -f "$out"/*.jar
  cp "fabric/build/libs/letterrush-1.0.0-${mc}+fabric.jar" "$out/"
  if [[ "$skip_neo" == false ]] && [[ -f "neoforge/build/libs/letterrush-1.0.0-${mc}+neoforge.jar" ]]; then
    cp "neoforge/build/libs/letterrush-1.0.0-${mc}+neoforge.jar" "$out/"
  fi
  ls -la "$out"
done

merge_properties "26.3"
echo "Done. Artifacts under release/<version>/ (1.21.11, 26.1.2, 26.2, 26.3)"
