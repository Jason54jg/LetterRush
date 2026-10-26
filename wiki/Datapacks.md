# Datapacks (récompenses)

Vous pouvez définir des récompenses via datapack au lieu (ou en remplacement) de la liste dans `config/letterrush.json`.

## Emplacement

```
data/<namespace>/letterrush/rewards/<fichier>.json
```

Exemple : `data/monpack/letterrush/rewards/bonus.json`

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

Mêmes types que la config : `item`, `experience`, `command`.

## Comportement

- Dès qu’**au moins une** récompense datapack est chargée (`/reload` ou `/letterrush reload`), la liste datapack **remplace** celle du fichier config.
- Modèle d’exemple dans le dépôt : [`docs/examples/datapack_rewards.example.json`](https://github.com/Jason54jg/LetterRush/blob/main/docs/examples/datapack_rewards.example.json) *(non chargé automatiquement — à copier dans votre datapack)*.

## Astuce

Pour tester : placez le JSON dans un datapack monde, `/reload`, puis `/letterrush start`.
