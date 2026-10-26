# Publier LetterRush sur Modrinth

## Fichiers à uploader

Structure `release/<version_minecraft>/` :

| MC | Fabric | NeoForge |
|----|--------|----------|
| 1.21.1 | `release/1.21.1/letterrush-1.0.0-1.21.1+fabric.jar` | `…+neoforge.jar` |
| 1.21.2 | `release/1.21.2/letterrush-1.0.0-1.21.2+fabric.jar` | utiliser `letterrush-1.0.0-1.21.3+neoforge.jar` (substitute) |
| 1.21.3 | `release/1.21.3/…` | oui |
| 1.21.4 | `release/1.21.4/…` | oui |
| 1.21.5 | `release/1.21.5/…` | oui (build le plus récent) |

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
