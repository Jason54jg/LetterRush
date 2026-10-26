# Minecraft 26.x (26.1 → 26.3)

Minecraft **26.1+** est **non obfusqué** (noms **Mojang** : `net.minecraft.world.item.Item`, `Identifier`, etc.). Ce n’est **pas** compatible avec les sources Yarn 1.21.x du dossier `common/src/main/java/`.

## Build

- **Gradle** 9.5.1 (`gradle/wrapper/gradle-wrapper.properties`)
- **JDK 25** pour compiler contre Minecraft 26.x
- **Architectury Loom** 1.17 (`dev.architectury.loom` / `dev.architectury.loom-no-remap`)
- Profils : `gradle/versions/26.1.properties` … `26.3.properties` (`unobfuscated=true`)

Sources dédiées :

- `common/src/main/javaMojmap26/` — logique partagée (Mojang)
- `fabric/src/main/javaMojmap26/` — client + entrées Fabric

## État actuel (WIP)

La chaîne Gradle dual (1.21 Yarn + 26 Mojang) est en place. Le port Mojang du mod est **avancé** mais le build 26.x **échoue encore** à la compilation : les JAR **Architectury API** publiés sur Maven utilisent encore des références **intermediary** (`class_3222`, `class_8710`, …) alors que Minecraft 26 n’expose que des noms Mojang.

Tant qu’Architectury ne publie pas d’artefacts alignés Mojang pour 26.x (ou qu’on ne remplace pas Architectury par Fabric/NeoForge natif sur cette branche), les jars `release/26.*` ne peuvent pas être générés.

## Commandes

```bash
export JAVA_HOME=/path/to/jdk-25
cat gradle/versions/common.properties gradle/versions/26.2.properties > gradle.properties
./gradlew :fabric:compileJava   # vérif partielle
```

Rebuild complet incluant 26.x (expérimental) :

```bash
BUILD_26=true ./scripts/build-all-versions.sh
```

Sans `BUILD_26=true`, le script ne build que **1.21.1 → 1.21.11** (stable).
