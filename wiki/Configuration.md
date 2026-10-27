# Configuration

File: `config/letterrush.json` (created on first server launch).

Reload without restart: `/letterrush reload`.

## Main options

| Key | Default | Description |
|-----|---------|-------------|
| `permission_level` | `2` | Command permission level |
| `letters` | `"ABCDEFGHIJKLMNOPQRSTUVWXYZ"` | Letters allowed for the roll |
| `play_sounds` | `true` | Sounds |
| `winner_particles` | `true` | Winner particles |
| `roulette_animation` | `true` | Client roulette HUD |
| `roulette_ticks` | `60` | Animation length (ticks). Min. 10 |
| `messages` | `{}` | Message overrides (placeholders `{0}`, `{1}`, …) |
| `rewards` | *(default list)* | Weighted rewards |

## Rewards (config)

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

Types:

| `type` | Useful fields |
|--------|----------------|
| `item` | `item`, `count` |
| `experience` | `amount` |
| `command` | `command` (run on server; `@p` = winner) |

`weight`: higher values are more likely to be picked.

## Default rewards

| Display | Weight | Type |
|---------|--------|------|
| 3 Diamonds | 15 | item |
| Golden Apple | 10 | item |
| 250 XP | 25 | experience |
| 8 Emeralds | 20 | item |
| Nether Star | 5 | item |
| Firework Show | 8 | command |

## Custom messages

In `messages`, you can override mod texts. Use `{0}`, `{1}`, … for arguments (letter, player, etc.).

If a datapack loads at least one LetterRush reward, **it replaces** the config `rewards` list — see [Datapacks](Datapacks).
