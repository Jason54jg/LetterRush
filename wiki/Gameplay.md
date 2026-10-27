# Gameplay

## Round flow

1. **Roll** — a letter from the config pool (`letters`, default A–Z), limited to letters that have matching items
2. **Announce** — on-screen title + chat + boss bar with elapsed time
3. **Goal** — pick up (real item-entity pickup) an item whose **English name** starts with that letter
4. **Win** — first valid player → winner title + weighted reward
5. **Round continues** until someone wins (no hard timeout)

## What counts?

| Case | Valid? |
|------|--------|
| Vanilla English name (e.g. `Diamond` for `D`) | Yes |
| Anvil-renamed item (custom name) | Yes, if the custom name starts with the letter |
| Item already in inventory (no pick-up) | No |
| Giving / shift-click from a chest | No (not an item-entity pick-up) |

Matching uses the **English** name (`en_us`), not the translated client language.

## Reward

Weighted pick from the `rewards` list (config or datapack):

- `item` — grant an item
- `experience` — XP
- `command` — server command (`@p` = winner)

Optional: **roulette** HUD animation (`roulette_animation`, `roulette_ticks`).

Winner sounds and particles can be disabled in config.
