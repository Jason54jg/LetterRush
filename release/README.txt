LetterRush 1.0.0 — supported Minecraft versions
===============================================

Supported releases (folders under release/):
  1.21.11
  26.1.2
  26.2
  26.3

Each Fabric jar is pinned to its MC version
  (e.g. 1.21.11 → >=1.21.11 <1.21.12).

Fabric: letterrush-1.0.0-<mc>+fabric.jar
NeoForge: letterrush-1.0.0-<mc>+neoforge.jar (all supported versions)

Rebuild (JDK 25 for 26.x; JDK 21+ OK for 1.21.11):
  ./scripts/build-all-versions.sh
  powershell -File scripts/build-all-versions.ps1

26.x details: docs/MINECRAFT-26.md

--- Français ---
Versions supportées : 1.21.11, 26.1.2, 26.2, 26.3.
Rebuild : scripts/build-all-versions.sh (ou .ps1).
