# Minecraft 26.x (26.1.2 / 26.2 / 26.3)

Supported **26.x** releases: **26.1.2**, **26.2**, **26.3** (plus Yarn **1.21.11**). Minecraft **26.x** is **unobfuscated** (Mojang names: `net.minecraft.world.item.Item`, `Identifier`, `GuiGraphicsExtractor`, etc.). It is **not** compatible with the Yarn 1.21.x sources under `common/src/main/java/`.

> **Français** — Versions 26.x supportées : **26.1.2**, **26.2**, **26.3**. Mappings Mojang (non obfusqué). Sources dédiées ; pas de Yarn 1.21.x. Voir [Français](#français).

## Build

- **Gradle** 9.5.1 (`gradle/wrapper/gradle-wrapper.properties`)
- **JDK 25** to compile against Minecraft 26.x
- **Architectury Loom** 1.17 (`dev.architectury.loom` for 1.21 / `dev.architectury.loom-no-remap` for 26, `unobfuscated=true`)
- Profiles: `gradle/versions/26.1.properties` … `26.3.properties`

Dedicated sources (no **Architectury** on 26.x — native Fabric API / NeoForge):

- `common/src/main/javaMojmap26/` — shared logic + client overlay (`RouletteOverlay`)
- `fabric/src/main/javaMojmap26/` — Fabric entry + pickup mixin
- `neoforge/src/main/javaMojmap26/` — NeoForge entry
- Metadata without Architectury dependency: `fabric/src/unobfuscated/resources/`, `neoforge/src/unobfuscated/resources/`

## Notable APIs (26.2)

- HUD rendering: `GuiGraphicsExtractor` (no longer `GuiGraphics` / `DrawContext`)
- Fabric HUD: `HudElementRegistry`; networking: `PayloadTypeRegistry.clientboundPlay()`
- NeoForge reload: `AddServerReloadListenersEvent`; pickup: `ItemEntityPickupEvent.Post`

## Commands

```bash
export JAVA_HOME=/path/to/jdk-25
cat gradle/versions/common.properties gradle/versions/26.2.properties > gradle.properties
./gradlew :fabric:remapJar :neoforge:remapJar
```

Full rebuild of supported versions (**1.21.11**, **26.1.2**, **26.2**, **26.3**):

```bash
./scripts/build-all-versions.sh
# Windows: powershell -File scripts/build-all-versions.ps1
```

JDK **25** is required for 26.x profiles. **1.21.11** builds with `--release 21` (JDK 21+).

**26.3**: `ServerPlayer.drop` requires `Prediction` — see `javaMojmap26_3util/PlayerDropHelper.java`.

---

## Français

- JDK **25**, Loom no-remap (`unobfuscated=true`)
- Sources sous `javaMojmap26/` ; métadonnées dans `*/unobfuscated/resources/`
- Rebuild : `./scripts/build-all-versions.sh` → **1.21.11**, **26.1.2**, **26.2**, **26.3**
