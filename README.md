# LetterRush

Mini-jeu multijoueur **letter race** pour Minecraft **1.21.1–1.21.11** (survie, sans dimension custom). Préparation **26.1 / 26.2 / 26.3** (Mojang, voir [docs/MINECRAFT-26.md](docs/MINECRAFT-26.md)). Compatible **Fabric** et **NeoForge** via [Architectury](https://github.com/architectury/Architectury).

## Jars (Modrinth)

Build **1.21.1 → 1.21.11** :

```bash
./scripts/build-all-versions.sh
```

Artefacts dans `release/<version>/` (voir [release/README.txt](release/README.txt)).

Version de dev par défaut : **1.21.11** (`gradle/versions/` pour chaque MC).

Dépendances : Fabric Loader + Fabric API, ou NeoForge (21.1+ / 21.3+ / 21.4+ / 21.5+ / 21.8+ selon MC ; voir substitutes dans `release/README.txt`), plus **Architectury API 13.0.8+**.

## Commandes

| Commande | Description |
|----------|-------------|
| `/letterrush start` | Lance une manche (durée par défaut dans la config) |
| `/letterrush start <secondes>` | Durée personnalisée (5–3600 s) |
| `/letterrush stop` | Arrête la manche en cours |
| `/letterrush status` | Lettre active et temps restant |
| `/letterrush reload` | Recharge `config/letterrush.json` et les datapacks |

Permission par défaut : niveau op **2** (`permission_level` dans la config).

## Gameplay

1. Une lettre **A–Z** est tirée au sort (titre + chat).
2. Un **boss bar** affiche le compte à rebours.
3. Le premier joueur qui **ramasse** (pickup réel d’une entité item) un objet dont le **nom anglais** commence par cette lettre gagne.
4. Les objets **renommés** à l'enclume comptent si le nom custom commence par la lettre.
5. Le gagnant reçoit une **récompense aléatoire pondérée** (roulette HUD optionnelle).

Si personne ne gagne avant la fin du timer : message *Time's up!*

## Configuration

Fichier : `config/letterrush.json` (créé au premier démarrage).

- `default_duration_seconds`, `permission_level`, `letters`
- `rewards` : liste `{ "weight", "type": "item"|"experience"|"command", ... }`
- `messages` : clés optionnelles avec placeholders `{0}`, `{1}`, …
- `play_sounds`, `winner_particles`, `roulette_animation`, `roulette_ticks`

### Datapack (récompenses)

Placez des fichiers JSON sous :

`data/<namespace>/letterrush/rewards/mon_fichier.json`

```json
{
  "rewards": [
    { "id": "bonus", "weight": 10, "type": "item", "item": "minecraft:diamond", "count": 1, "display": "Diamond" }
  ]
}
```

Si au moins une récompense datapack est chargée (`/reload`), elle **remplace** la liste du fichier config.

Modèle JSON (à copier dans votre datapack, **pas** chargé automatiquement) : [`docs/examples/datapack_rewards.example.json`](docs/examples/datapack_rewards.example.json).

## Build

```bash
./gradlew :fabric:remapJar :neoforge:remapJar
```

Java **21** requis.

## Structure

- `common/` — logique serveur, config, récompenses, réseau
- `fabric/` — entrée Fabric + mixin pickup
- `neoforge/` — entrée NeoForge + événement pickup

## Modrinth

Guide de publication : [MODRINTH.md](MODRINTH.md). Icône du mod : `assets/letterrush/icon.png`.

## Licence

MIT — voir [LICENSE](LICENSE).
