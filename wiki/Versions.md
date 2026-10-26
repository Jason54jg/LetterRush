# Versions & build

## Versions supportées

| Minecraft | JDK (build) | Fabric | NeoForge |
|-----------|-------------|--------|----------|
| **1.21.11** | 21+ | Oui | Oui (21.11.x) |
| **26.1.2** | 25 | Oui | selon profil |
| **26.2** | 25 | Oui | selon profil |
| **26.3** | 25 | Oui | selon profil |

Détails mappings / 26.x : [`docs/MINECRAFT-26.md`](https://github.com/Jason54jg/LetterRush/blob/main/docs/MINECRAFT-26.md).

## Build local (toutes versions)

```bash
./scripts/build-all-versions.sh
# Windows :
powershell -File scripts/build-all-versions.ps1
```

Artefacts : `release/<version>/`.

Une seule version :

```bash
# Fusionner le profil Gradle puis builder
cat gradle/versions/common.properties gradle/versions/<mc>.properties > gradle.properties
./gradlew :fabric:remapJar :neoforge:remapJar
```

## GitHub Actions

Workflow **Build LetterRush** (`.github/workflows/build-letterrush.yml`) :

- Trigger : push / PR sur `main` (chemins code / gradle)
- Matrice : `1.21.11` (Java 21), `26.1.2` / `26.2` / `26.3` (Java 25)
- Artefacts uploadés : `letterrush-<mc>`

Workflow **Deploy Wiki** (`.github/workflows/deploy-wiki.yml`) :

- Trigger : push sur `main` si `wiki/**` change
- Publie le dossier `wiki/` vers le [GitHub Wiki](https://github.com/Jason54jg/LetterRush/wiki) du dépôt

## Structure du projet

| Dossier | Rôle |
|---------|------|
| `common/` | Logique serveur, config, rewards, réseau |
| `fabric/` | Entrée Fabric + mixin pick-up |
| `neoforge/` | Entrée NeoForge + event pick-up |
| `wiki/` | Sources de cette documentation (sync CI) |
