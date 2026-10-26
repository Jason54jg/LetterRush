package io.github.jason54.letterrush.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.LetterRushRuntime;
import io.github.jason54.letterrush.reward.RewardDefinition;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * JSON configuration stored in {@code config/letterrush.json}.
 */
public final class LetterRushConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static LetterRushConfig instance = LetterRushConfig.defaultConfig();

    @SerializedName("default_duration_seconds")
    public int defaultDurationSeconds = 60;

    @SerializedName("permission_level")
    public int permissionLevel = 2;

    /** Letters allowed for random selection (defaults to A–Z). */
    public String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @SerializedName("play_sounds")
    public boolean playSounds = true;

    @SerializedName("winner_particles")
    public boolean winnerParticles = true;

    @SerializedName("roulette_animation")
    public boolean rouletteAnimation = true;

    @SerializedName("roulette_ticks")
    public int rouletteTicks = 60;

    public Map<String, String> messages = new HashMap<>();

    public List<RewardDefinition> rewards = new ArrayList<>();

    public static LetterRushConfig get() {
        return instance;
    }

    public static void load(MinecraftServer server) {
        Path path = configPath();
        if (!Files.exists(path)) {
            instance = defaultConfig();
            save(path);
            LetterRush.LOGGER.info("Created default LetterRush config at {}", path);
            return;
        }
        try (Reader reader = Files.newBufferedReader(path)) {
            LetterRushConfig loaded = GSON.fromJson(reader, LetterRushConfig.class);
            if (loaded == null) {
                loaded = defaultConfig();
            }
            loaded.normalize();
            instance = loaded;
        } catch (Exception e) {
            LetterRush.LOGGER.error("Failed to load LetterRush config, using defaults", e);
            instance = defaultConfig();
        }
        LetterRush.LOGGER.info("LetterRush config loaded ({} rewards).", instance.rewards.size());
    }

    public static void reload(MinecraftServer server) {
        load(server);
        io.github.jason54.letterrush.game.EnglishItemNames.invalidateCache();
    }

    public static void save() {
        save(configPath());
    }

    private static void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException e) {
            LetterRush.LOGGER.error("Failed to save LetterRush config", e);
        }
    }

    private static Path configPath() {
        return LetterRushRuntime.configDirectory().resolve("letterrush.json");
    }

    private void normalize() {
        if (defaultDurationSeconds < 5) {
            defaultDurationSeconds = 5;
        }
        if (permissionLevel < 0) {
            permissionLevel = 0;
        }
        if (letters == null || letters.isBlank()) {
            letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        letters = letters.toUpperCase(Locale.ROOT).replaceAll("[^A-Z]", "");
        if (letters.isEmpty()) {
            letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if (rewards == null || rewards.isEmpty()) {
            rewards = defaultConfig().rewards;
        }
        if (messages == null) {
            messages = new HashMap<>();
        }
        if (rouletteTicks < 10) {
            rouletteTicks = 10;
        }
    }

    public static LetterRushConfig defaultConfig() {
        LetterRushConfig config = new LetterRushConfig();
        config.rewards = List.of(
                RewardDefinition.item("diamonds", 15, "minecraft:diamond", 3, "3 Diamonds"),
                RewardDefinition.item("golden_apple", 10, "minecraft:golden_apple", 1, "Golden Apple"),
                RewardDefinition.experience("xp_burst", 25, 250, "250 XP"),
                RewardDefinition.item("emeralds", 20, "minecraft:emerald", 8, "8 Emeralds"),
                RewardDefinition.item("lucky_star", 5, "minecraft:nether_star", 1, "Nether Star"),
                RewardDefinition.command("fireworks", 8, "give @p minecraft:firework_rocket 16", "Firework Show")
        );
        return config;
    }

    public List<Character> letterPool() {
        List<Character> pool = new ArrayList<>();
        for (char c : letters.toCharArray()) {
            pool.add(c);
        }
        return pool;
    }

    public Messages messages() {
        return new Messages(messages);
    }

    public record Messages(Map<String, String> map) {
        public String get(String key) {
            return map.get(key);
        }
    }
}
