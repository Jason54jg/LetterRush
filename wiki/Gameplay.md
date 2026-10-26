# Gameplay

## Déroulement d’une manche

1. **Tirage** — une lettre parmi celles de la config (`letters`, défaut A–Z)
2. **Annonce** — titre à l’écran + message chat + boss bar avec le temps restant
3. **Objectif** — ramasser (pick-up réel d’entité item) un objet dont le **nom anglais** commence par cette lettre
4. **Victoire** — premier joueur valide → titre gagnant + récompense pondérée
5. **Temps écoulé** — si personne n’a gagné : *Time's up!* / *Temps écoulé!*

## Qu’est-ce qui compte ?

| Cas | Valide ? |
|-----|----------|
| Nom anglais vanilla (ex. `Diamond` pour `D`) | Oui |
| Objet renommé à l’enclume (custom name) | Oui, si le nom custom commence par la lettre |
| Objet déjà en inventaire (sans pick-up) | Non |
| Donner / shift-clic depuis un coffre | Non (ce n’est pas un pick-up d’entité) |

La détection se base sur le **nom anglais** (locale `en_us`), pas sur le nom traduit français.

## Récompense

Tirage pondéré parmi les entrées `rewards` (config ou datapack) :

- `item` — donne un item
- `experience` — XP
- `command` — commande serveur (`@p` = gagnant)

Optionnel : animation **roulette** HUD (`roulette_animation`, `roulette_ticks`).

Sons et particules de victoire peuvent être désactivés dans la config.
