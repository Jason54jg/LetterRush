# Configuration

Fichier : `config/letterrush.json` (créé au premier lancement du serveur).

Recharge sans redémarrer : `/letterrush reload`.

## Options principales

| Clé | Défaut | Description |
|-----|--------|-------------|
| `default_duration_seconds` | `60` | Durée d’une manche (`start` sans argument). Min. 5 |
| `permission_level` | `2` | Niveau de permission des commandes |
| `letters` | `"ABCDEFGHIJKLMNOPQRSTUVWXYZ"` | Lettres autorisées pour le tirage |
| `play_sounds` | `true` | Sons |
| `winner_particles` | `true` | Particules de victoire |
| `roulette_animation` | `true` | HUD roulette côté client |
| `roulette_ticks` | `60` | Durée animation (ticks). Min. 10 |
| `messages` | `{}` | Surcharges de messages (placeholders `{0}`, `{1}`, …) |
| `rewards` | *(liste par défaut)* | Récompenses pondérées |

## Récompenses (config)

```json
{
  "id": "diamonds",
  "weight": 15,
  "type": "item",
  "item": "minecraft:diamond",
  "count": 3,
  "display": "3 Diamonds"
}
```

Types :

| `type` | Champs utiles |
|--------|----------------|
| `item` | `item`, `count` |
| `experience` | `amount` |
| `command` | `command` (exécutée côté serveur ; `@p` = gagnant) |

`weight` : plus la valeur est haute, plus la récompense a de chances d’être tirée.

## Récompenses par défaut

| Affichage | Poids | Type |
|-----------|-------|------|
| 3 Diamonds | 15 | item |
| Golden Apple | 10 | item |
| 250 XP | 25 | experience |
| 8 Emeralds | 20 | item |
| Nether Star | 5 | item |
| Firework Show | 8 | command |

## Messages custom

Dans `messages`, vous pouvez redéfinir les textes (clés internes du mod). Utilisez `{0}`, `{1}`, … pour les arguments (lettre, joueur, etc.).

Si un datapack charge au moins une récompense LetterRush, **il remplace** la liste `rewards` du fichier config — voir [Datapacks](Datapacks).
