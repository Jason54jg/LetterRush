# Versions & build

## Supported versions

| Minecraft | JDK (build) | Fabric | NeoForge |
|-----------|-------------|--------|----------|
| **1.21.11** | 21+ | Yes | Yes (21.11.x) |
| **26.1.2** | 25 | Yes | per profile |
| **26.2** | 25 | Yes | per profile |
| **26.3** | 25 | Yes | per profile |

Mappings / 26.x details: [`docs/MINECRAFT-26.md`](https://github.com/Jason54jg/LetterRush/blob/main/docs/MINECRAFT-26.md).

## Local build (all versions)

```bash
./scripts/build-all-versions.sh
# Windows:
powershell -File scripts/build-all-versions.ps1
```

Artifacts: `release/<version>/`.

Single version:

```bash
# Merge Gradle profile then build
cat gradle/versions/common.properties gradle/versions/<mc>.properties > gradle.properties
./gradlew :fabric:remapJar :neoforge:remapJar
```

## GitHub Actions

Workflow **Build LetterRush** (`.github/workflows/build-letterrush.yml`):

- Trigger: push / PR on `main` (code / gradle paths)
- Matrix: `1.21.11` (Java 21), `26.1.2` / `26.2` / `26.3` (Java 25)
- Uploaded artifacts: `letterrush-<mc>`

Workflow **Deploy Docs** (`.github/workflows/deploy-wiki.yml`):

- Trigger: push on `main` when `wiki/**` changes
- Publishes `wiki/` to [GitHub Pages](https://jason54jg.github.io/LetterRush/)
- Once: Settings → Pages → Source = **GitHub Actions**

## Project layout

| Folder | Role |
|--------|------|
| `common/` | Server logic, config, rewards, networking |
| `fabric/` | Fabric entry + pick-up mixin |
| `neoforge/` | NeoForge entry + pick-up event |
| `wiki/` | Documentation sources (GitHub Pages) |
