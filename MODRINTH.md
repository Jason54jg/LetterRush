# Publishing LetterRush on Modrinth

> **Français** — Guide de publication Modrinth. Voir [Français](#français) en bas.

## Files to upload

Layout under `release/<minecraft_version>/`:

| MC | Fabric | NeoForge |
|----|--------|----------|
| 1.21.1 | `release/1.21.1/letterrush-1.0.0-1.21.1+fabric.jar` | `…+neoforge.jar` (native) |
| 1.21.2 | `release/1.21.2/…+fabric.jar` | `letterrush-1.0.0-1.21.3+neoforge.jar` (substitute) |
| 1.21.3–1.21.5 | `release/<mc>/…+fabric.jar` | native |
| 1.21.6 | native Fabric | substitute from **1.21.5** |
| 1.21.7 | native Fabric | substitute from **1.21.8** |
| 1.21.8 | native | native |
| 1.21.9–1.21.11 | native Fabric | substitute from **1.21.8** |

Icon: `common/src/main/resources/assets/letterrush/icon.png`  
Banner / logo: `docs/branding/banner.png`, `docs/branding/logo.png`

## Rebuild all versions

```bash
./scripts/build-all-versions.sh
```

Gradle profiles: `gradle/versions/<mc>.properties` + `gradle/versions/common.properties`.

## Modrinth dependencies

- **Architectury API** (required)
- **Fabric API** (Fabric only)

## Suggested tags

`adventure`, `game-mechanics`, `multiplayer`, `fabric`, `neoforge`

---

## Français

Uploadez les jars sous `release/<version>/` (voir tableau ci-dessus). Icône : `assets/letterrush/icon.png`. Rebuild : `./scripts/build-all-versions.sh`. Dépendances : Architectury API (+ Fabric API sur Fabric).
