# Datapacks (rewards)

You can define rewards via datapack instead of (or replacing) the list in `config/letterrush.json`.

## Location

```
data/<namespace>/letterrush/rewards/<file>.json
```

Example: `data/mypack/letterrush/rewards/bonus.json`

## Format

```json
{
  "rewards": [
    {
      "id": "datapack_cookie",
      "weight": 30,
      "type": "item",
      "item": "minecraft:cookie",
      "count": 8,
      "display": "8 Cookies"
    },
    {
      "id": "datapack_xp",
      "weight": 20,
      "type": "experience",
      "amount": 500,
      "display": "500 XP (datapack)"
    }
  ]
}
```

Same types as config: `item`, `experience`, `command`.

## Behavior

- As soon as **at least one** datapack reward is loaded (`/reload` or `/letterrush reload`), the datapack list **replaces** the config file list.
- Example template in the repo: [`docs/examples/datapack_rewards.example.json`](https://github.com/Jason54jg/LetterRush/blob/main/docs/examples/datapack_rewards.example.json) *(not loaded automatically — copy it into your datapack)*.

## Tip

To test: put the JSON in a world datapack, `/reload`, then `/letterrush start`.
