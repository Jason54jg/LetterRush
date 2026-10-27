# Publishing LetterRush on Modrinth

> **Français** — Guide de publication Modrinth. Voir [Français](#français) en bas.

## Supported game versions

| Minecraft | Fabric jar | NeoForge jar | JDK (build) |
|-----------|------------|--------------|-------------|
| **1.21.11** | `letterrush-1.0.0-1.21.11+fabric.jar` | `…+neoforge.jar` | 21+ |
| **26.1.2** | `letterrush-1.0.0-26.1.2+fabric.jar` | `…+neoforge.jar` | 25 |
| **26.2** | `letterrush-1.0.0-26.2+fabric.jar` | `…+neoforge.jar` | 25 |
| **26.3** | `letterrush-1.0.0-26.3+fabric.jar` | `…+neoforge.jar` | 25 |

Each Fabric jar is pinned to **that** MC version only (e.g. 1.21.11 → `>=1.21.11 <1.21.12`). Do not reuse a jar across major MC versions.

## Files to upload

After a full rebuild, jars are under `release/<minecraft_version>/`:

```
release/1.21.11/letterrush-1.0.0-1.21.11+fabric.jar
release/1.21.11/letterrush-1.0.0-1.21.11+neoforge.jar
release/26.1.2/…
release/26.2/…
release/26.3/…
```

**Assets**

| Asset | Path |
|-------|------|
| Icon | `common/src/main/resources/assets/letterrush/icon.png` |
| Banner | `docs/branding/banner.png` |
| Logo | `docs/branding/logo.png` |

## Rebuild all versions

```bash
./scripts/build-all-versions.sh
# Windows:
powershell -File scripts/build-all-versions.ps1
```

Gradle profiles: `gradle/versions/common.properties` + `gradle/versions/<mc>.properties`.  
Details for 26.x: [`docs/MINECRAFT-26.md`](docs/MINECRAFT-26.md).

## Modrinth project setup

Suggested metadata:

| Field | Value |
|-------|--------|
| **Slug** | `letterrush` |
| **Loaders** | Fabric, NeoForge |
| **Categories** | Adventure · Game Mechanics · Multiplayer |
| **Side** | Required on server; client optional (roulette HUD) |
| **License** | MIT |

### Dependencies

| Dependency | Fabric | NeoForge | Note |
|------------|--------|----------|------|
| **Architectury API** | required | required | Especially for **1.21.11** |
| **Fabric API** | required | — | Fabric only |
| NeoForge | — | required | **21.11.x** for MC 1.21.11 |

Create **one Modrinth version per** `(MC × loader)` pair (8 uploads for a full release), or group loaders per MC version if you prefer fewer Modrinth versions with both jars.

### Version naming

Examples:

- `1.0.0+mc1.21.11` — Fabric + NeoForge for 1.21.11  
- `1.0.0+mc26.3` — Fabric + NeoForge for 26.3  

Match `displayVersion` / jar names already produced by the build (`1.0.0+mc…`).

## Suggested tags

`adventure`, `game-mechanics`, `multiplayer`, `fabric`, `neoforge`, `minigame`

## Docs / links

- Player docs: https://jason54jg.github.io/LetterRush/  
- Source: https://github.com/Jason54jg/LetterRush  
- In-repo wiki sources: `wiki/`

---

## Français

Versions supportées : **1.21.11**, **26.1.2**, **26.2**, **26.3** (Fabric + NeoForge natifs, pas de jars « substitute »).

1. Rebuild : `./scripts/build-all-versions.sh` (ou `.ps1` sous Windows)  
2. Uploadez les jars sous `release/<version>/`  
3. Icône : `assets/letterrush/icon.png` — bannière : `docs/branding/banner.png`  
4. Dépendances : **Architectury API** (requis) ; **Fabric API** sur Fabric ; NeoForge **21.11.x** pour MC 1.21.11  
5. Un jar Fabric = **une** version Minecraft précise  

Doc joueur : [jason54jg.github.io/LetterRush](https://jason54jg.github.io/LetterRush/).
