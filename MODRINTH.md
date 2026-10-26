# Publier LetterRush sur Modrinth

## Fichiers à uploader

Structure `release/<version_minecraft>/` :

| MC | Fabric | NeoForge |
|----|--------|----------|
| 1.21.1 | `release/1.21.1/letterrush-1.0.0-1.21.1+fabric.jar` | `…+neoforge.jar` (natif) |
| 1.21.2 | `release/1.21.2/…+fabric.jar` | `letterrush-1.0.0-1.21.3+neoforge.jar` (substitute) |
| 1.21.3–1.21.5 | `release/<mc>/…+fabric.jar` | natif |
| 1.21.6 | Fabric natif | substitute depuis **1.21.5** |
| 1.21.7 | Fabric natif | substitute depuis **1.21.8** |
| 1.21.8 | natif | natif |
| 1.21.9–1.21.11 | Fabric natif | substitute depuis **1.21.8** |

Icône : `common/src/main/resources/assets/letterrush/icon.png`

## Rebuild toutes les versions

```bash
./scripts/build-all-versions.sh
```

Profils Gradle : `gradle/versions/<mc>.properties` + `gradle/versions/common.properties`.

## Dépendances Modrinth

- **Architectury API** (required)
- **Fabric API** (Fabric uniquement)

## Tags suggérés

`adventure`, `game-mechanics`, `multiplayer`, `fabric`, `neoforge`
