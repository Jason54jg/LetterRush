# LetterRush

Multiplayer **letter race** mini-game for Minecraft **1.21.11**, **26.1.2**, **26.2**, and **26.3** (survival, no custom dimension). 26.x uses Mojang mappings — see [docs/MINECRAFT-26.md](docs/MINECRAFT-26.md). Supports **Fabric** and **NeoForge** (Architectury on 1.21.11).

> **Français** — Mini-jeu multijoueur « letter race » en survie (**1.21.11**, **26.1.2**, **26.2**, **26.3**). Fabric + NeoForge. Voir la section [Français](#français) plus bas.

## Jars (Modrinth)

Build supported versions:

```bash
./scripts/build-all-versions.sh
# Windows: powershell -File scripts/build-all-versions.ps1
```

Artifacts land in `release/<version>/` (see [release/README.txt](release/README.txt)).

Supported folders: **1.21.11**, **26.1.2**, **26.2**, **26.3**. Each Fabric jar targets **that** Minecraft version only (`fabric.mod.json`).

**GitHub CI**: **1.21.11** (Java 21) and **26.1.2 / 26.2 / 26.3** (Java 25).

Dependencies: Fabric Loader + Fabric API, or NeoForge (**21.11.x** for 1.21.11); **Architectury API** on 1.21.11.

## Commands

| Command | Description |
|---------|-------------|
| `/letterrush start` | Start a round (default duration from config) |
| `/letterrush start <seconds>` | Custom duration (5–3600 s) |
| `/letterrush stop` | Stop the current round |
| `/letterrush status` | Active letter and time remaining |
| `/letterrush reload` | Reload `config/letterrush.json` and datapacks |

Default permission: op level **2** (`permission_level` in config).

## Gameplay

1. A letter **A–Z** is rolled (title + chat).
2. A **boss bar** shows the countdown.
3. The first player to **pick up** (real item-entity pickup) an item whose **English name** starts with that letter wins.
4. Anvil-**renamed** items count if the custom name starts with the letter.
5. The winner gets a **weighted random reward** (optional roulette HUD).

If nobody wins before the timer ends: *Time's up!*

## Configuration

File: `config/letterrush.json` (created on first launch).

- `default_duration_seconds`, `permission_level`, `letters`
- `rewards`: list of `{ "weight", "type": "item"|"experience"|"command", ... }`
- `messages`: optional keys with `{0}`, `{1}`, … placeholders
- `play_sounds`, `winner_particles`, `roulette_animation`, `roulette_ticks`

### Datapack (rewards)

Place JSON files under:

`data/<namespace>/letterrush/rewards/my_file.json`

```json
{
  "rewards": [
    { "id": "bonus", "weight": 10, "type": "item", "item": "minecraft:diamond", "count": 1, "display": "Diamond" }
  ]
}
```

If at least one datapack reward is loaded (`/reload`), it **replaces** the config file list.

JSON template (copy into your datapack; **not** loaded automatically): [`docs/examples/datapack_rewards.example.json`](docs/examples/datapack_rewards.example.json).

## Build

```bash
./gradlew :fabric:remapJar :neoforge:remapJar
```

| Target | JDK | Command |
|--------|-----|---------|
| **1.21.11** | **21+** | included in `./scripts/build-all-versions.sh` |
| **26.1.2 / 26.2 / 26.3** | **25** | same script (`JAVA_HOME` → JDK 25) |

Single-version Gradle build: `cat gradle/versions/common.properties gradle/versions/<mc>.properties > gradle.properties` then `./gradlew …` with the matching JDK.

## Structure

- `common/` — server logic, config, rewards, networking
- `fabric/` — Fabric entry + pickup mixin
- `neoforge/` — NeoForge entry + pickup event

## Branding

- Logo: [`docs/branding/logo.png`](docs/branding/logo.png)
- Banner: [`docs/branding/banner.png`](docs/branding/banner.png)
- In-game / Modrinth icon: `common/src/main/resources/assets/letterrush/icon.png`

## Wiki

Documentation joueur / serveur : sources dans [`wiki/`](wiki/) — publiée sur le [GitHub Wiki](https://github.com/Jason54jg/LetterRush/wiki) via Actions (`.github/workflows/deploy-wiki.yml`).

## Modrinth

Page projet (bientôt) : [modrinth.com/project/letterrush](https://modrinth.com/project/letterrush).  
Publishing guide: [MODRINTH.md](MODRINTH.md).

## License

MIT — see [LICENSE](LICENSE).

---

## Français

Mini-jeu multijoueur **letter race** pour Minecraft **1.21.11**, **26.1.2**, **26.2**, **26.3** (survie, sans dimension custom). Compatible **Fabric** et **NeoForge**.

- Builds : `./scripts/build-all-versions.sh` → `release/<version>/`
- Chaque jar Fabric cible **une** version MC (ex. 1.21.11 → `>=1.21.11 <1.21.12`)
- Commandes : `/letterrush start|stop|status|reload`
- Le premier joueur qui **ramasse** un objet dont le **nom anglais** commence par la lettre tirée gagne
- Config : `config/letterrush.json` ; récompenses aussi via datapack

Détails techniques et build 26.x : sections anglaises ci-dessus et [docs/MINECRAFT-26.md](docs/MINECRAFT-26.md).  
Wiki FR : [github.com/Jason54jg/LetterRush/wiki](https://github.com/Jason54jg/LetterRush/wiki) (sources `wiki/`).
