# Publier LetterRush sur Modrinth

## Fichiers à uploader

| Loader | Fichier |
|--------|---------|
| Fabric | `release/letterrush-1.0.0-1.21.1+fabric.jar` |
| NeoForge | `release/letterrush-1.0.0-1.21.1+neoforge.jar` |

Icône : `common/src/main/resources/assets/letterrush/icon.png`

## Versions Minecraft

Cible de développement : **1.21.1**. Les métadonnées autorisent **1.21.1 à 1.21.3** (`<1.21.4`).

Pour compiler contre une autre patch 1.21.x, modifiez `gradle.properties` :

```properties
minecraft_version=1.21.3
yarn_mappings=1.21.3+build.x   # voir https://fabricmc.net/develop/
neoforge_version=21.1.xxx      # voir https://projects.neoforged.net/
fabric_api_version=0.xxx+1.21.3
```

Puis : `./gradlew :fabric:remapJar :neoforge:remapJar`

## Dépendances à déclarer sur Modrinth

- **Architectury API** (required)
- **Fabric API** (Fabric uniquement)

## Description courte (EN)

> Race to pick up an item whose English name starts with the announced letter. Boss bar timer, weighted rewards, multiplayer-ready.

## Tags suggérés

`adventure`, `game-mechanics`, `multiplayer`, `fabric`, `neoforge`
