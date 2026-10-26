LetterRush 1.0.0 — toutes les versions stables 1.21.x
=====================================================

Dossiers release/1.21.1 … release/1.21.11 (11 versions Minecraft).

Fabric : un jar par version (letterrush-1.0.0-<mc>+fabric.jar).

NeoForge : jar natif quand la toolchain Gradle le permet ; sinon jar
« substitute » d’une version voisine (même série 21.x NeoForge).
Les substitutes gardent le nom du jar source (ex. 1.21.6/ contient
letterrush-1.0.0-1.21.5+neoforge.jar).

Substitutes NeoForge :
  1.21.2  ← 1.21.3
  1.21.6  ← 1.21.5
  1.21.7  ← 1.21.8
  1.21.9  ← 1.21.8
  1.21.10 ← 1.21.8
  1.21.11 ← 1.21.8

Minecraft 26.x (expérimental) :
  BUILD_26=true ./scripts/build-all-versions.sh  (JDK 25)
  Dossiers release/26.1 … release/26.3 — voir docs/MINECRAFT-26.md

Rebuild : ./scripts/build-all-versions.sh
