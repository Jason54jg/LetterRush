# Installation

## Requirements

| Loader | Dependencies |
|--------|--------------|
| **Fabric** | Fabric Loader + [Fabric API](https://modrinth.com/mod/fabric-api) + [Architectury API](https://modrinth.com/mod/architectury-api) *(1.21.11)* |
| **NeoForge** | NeoForge **21.11.x** (for MC 1.21.11) + Architectury API *(1.21.11)* |

On **26.x** builds, see [Versions & build](Versions).

## Download

1. **Modrinth** (recommended, once published): [letterrush](https://modrinth.com/project/letterrush)
2. **GitHub Actions / Releases**: build artifacts from the repo
3. **Local build**: see [Versions & build](Versions)

Put the **Fabric** *or* **NeoForge** jar matching your Minecraft version into `mods/`.

> Each Fabric jar targets **one** MC version (e.g. 1.21.11 → `>=1.21.11 <1.21.12`). Do not mix versions.

## Server / client

- Core logic runs on the **server** (commands, rounds, rewards)
- Roulette overlay is **client-side** (if enabled in config)

Install the mod on the server **and** on clients for the roulette animation. Without the client mod, the game still works (reward + server messages).

## Quick check

1. Start the server / world
2. `config/letterrush.json` is created on first launch
3. In-game (op level 2 by default): `/letterrush start`
