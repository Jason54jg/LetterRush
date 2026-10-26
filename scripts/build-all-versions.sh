#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

VERSIONS=(
  1.21.1 1.21.2 1.21.3 1.21.4 1.21.5
  1.21.6 1.21.7 1.21.8 1.21.9 1.21.10 1.21.11
)

merge_properties() {
  local mc="$1"
  cat gradle/versions/common.properties gradle/versions/"${mc}.properties" > gradle.properties
}

mkdir -p release

for mc in "${VERSIONS[@]}"; do
  echo "========== Minecraft ${mc} =========="
  merge_properties "$mc"
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
  cp fabric/build/libs/letterrush-*+fabric.jar "$out/"
  if [[ "$skip_neo" == false ]] && compgen -G "neoforge/build/libs/letterrush-*+neoforge.jar" > /dev/null; then
    cp neoforge/build/libs/letterrush-*+neoforge.jar "$out/"
  fi
  ls -la "$out"
done

# NeoForge substitutes (no stable NeoForge toolchain for these MC versions)
copy_neo_substitute() {
  local target_mc="$1"
  local source_mc="$2"
  local src="release/${source_mc}/letterrush-1.0.0-${source_mc}+neoforge.jar"
  local dst="release/${target_mc}/"
  if [[ -f "$src" ]]; then
    mkdir -p "$dst"
    cp "$src" "$dst/"
    echo "NeoForge ${target_mc}: substitute from ${source_mc} ($(basename "$src"))"
  fi
}

copy_neo_substitute "1.21.2" "1.21.3"
copy_neo_substitute "1.21.6" "1.21.5"
copy_neo_substitute "1.21.7" "1.21.8"
copy_neo_substitute "1.21.9" "1.21.8"
copy_neo_substitute "1.21.10" "1.21.8"
copy_neo_substitute "1.21.11" "1.21.8"

merge_properties "1.21.11"
echo "Done. Artifacts under release/<version>/"
