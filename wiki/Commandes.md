# Commandes

Permission par défaut : **niveau op 2** (`permission_level` dans la config).

| Commande | Description |
|----------|-------------|
| `/letterrush start` | Lance une manche (durée = `default_duration_seconds`) |
| `/letterrush start <secondes>` | Durée personnalisée (**5–3600**) |
| `/letterrush stop` | Arrête la manche en cours |
| `/letterrush status` | Affiche la lettre active et le temps restant |
| `/letterrush reload` | Recharge `config/letterrush.json` et les datapacks de récompenses |

## Exemples

```mcfunction
/letterrush start
/letterrush start 90
/letterrush status
/letterrush stop
/letterrush reload
```

Si une manche est déjà active, `start` est refusé jusqu’à `stop` ou fin naturelle.
