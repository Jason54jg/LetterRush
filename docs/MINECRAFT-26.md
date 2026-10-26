# Minecraft 26.x (26.1 → 26.3)

Minecraft **26.1+** est **non obfusqué** (noms **Mojang** : `net.minecraft.world.item.Item`, `Identifier`, `GuiGraphicsExtractor`, etc.). Ce n’est **pas** compatible avec les sources Yarn 1.21.x du dossier `common/src/main/java/`.

## Build

- **Gradle** 9.5.1 (`gradle/wrapper/gradle-wrapper.properties`)
- **JDK 25** pour compiler contre Minecraft 26.x
- **Architectury Loom** 1.17 (`dev.architectury.loom` pour 1.21 / `dev.architectury.loom-no-remap` pour 26, `unobfuscated=true`)
- Profils : `gradle/versions/26.1.properties` … `26.3.properties`

Sources dédiées (sans **Architectury** sur 26.x — Fabric API / NeoForge natifs) :

- `common/src/main/javaMojmap26/` — logique partagée + overlay client (`RouletteOverlay`)
- `fabric/src/main/javaMojmap26/` — entrées Fabric + mixin pickup
- `neoforge/src/main/javaMojmap26/` — entrées NeoForge
- Métadonnées sans dépendance Architectury : `fabric/src/unobfuscated/resources/`, `neoforge/src/unobfuscated/resources/`

## API notables (26.2)

- Rendu HUD : `GuiGraphicsExtractor` (plus `GuiGraphics` / `DrawContext`)
- Fabric HUD : `HudElementRegistry` ; networking : `PayloadTypeRegistry.clientboundPlay()`
- NeoForge reload : `AddServerReloadListenersEvent` ; pickup : `ItemEntityPickupEvent.Post`

## Commandes

```bash
export JAVA_HOME=/path/to/jdk-25
cat gradle/versions/common.properties gradle/versions/26.2.properties > gradle.properties
./gradlew :fabric:remapJar :neoforge:remapJar
```

Rebuild complet incluant 26.x :

```bash
BUILD_26=true ./scripts/build-all-versions.sh
```

Sans `BUILD_26=true`, le script ne build que **1.21.1 → 1.21.11** (JDK 21, Yarn + Architectury).

Profils **26.1** / **26.1.1** : `skip_neoforge=true` (pas de jar NeoForge publié pour ces versions). Sur **Fabric 26.1 / 26.1.1**, l’overlay roulette HUD est désactivé (Fabric API encore basée sur `GuiGraphics`, absent du jeu) ; le réseau et la logique serveur fonctionnent.

**26.3** : `ServerPlayer.drop` exige `Prediction` — voir `javaMojmap26_3util/PlayerDropHelper.java`.
