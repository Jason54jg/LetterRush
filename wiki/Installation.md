# Installation

## Prérequis

| Loader | Dépendances |
|--------|-------------|
| **Fabric** | Fabric Loader + [Fabric API](https://modrinth.com/mod/fabric-api) + [Architectury API](https://modrinth.com/mod/architectury-api) *(1.21.11)* |
| **NeoForge** | NeoForge **21.11.x** (pour MC 1.21.11) + Architectury API *(1.21.11)* |

Sur les branches **26.x**, suivez les notes de [Versions & build](Versions).

## Téléchargement

1. **Modrinth** (recommandé, dès publication) : [letterrush](https://modrinth.com/project/letterrush)
2. **GitHub Actions / Releases** : artefacts de build du dépôt
3. **Build local** : voir [Versions & build](Versions)

Placez le jar **Fabric** *ou* **NeoForge** correspondant à votre version de Minecraft dans le dossier `mods/`.

> Chaque jar Fabric cible **une** version MC précise (ex. 1.21.11 → `>=1.21.11 <1.21.12`). Ne mélangez pas les versions.

## Côté serveur / client

- Logique principale **serveur** (commandes, manche, récompenses)
- Overlay roulette côté **client** (si activé dans la config)

Installez le mod sur le serveur **et** sur les clients pour l’animation de roulette. Sans le mod client, le jeu fonctionne quand même (récompense + messages serveur).

## Vérification rapide

1. Démarrez le serveur / le monde
2. Un fichier `config/letterrush.json` est créé au premier lancement
3. En jeu (op niveau 2 par défaut) : `/letterrush start`
