package io.github.jason54.letterrush.reward;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.config.LetterRushConfig;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.Identifier;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loads optional reward lists from datapack JSON files at {@code letterrush/rewards/*.json}.
 * When present, these rewards replace the config file list until the next reload.
 */
public final class DatapackRewardLoader {
    private static final Gson GSON = new Gson();
    private static List<RewardDefinition> datapackRewards = List.of();

    public static List<RewardDefinition> getDatapackRewards() {
        return datapackRewards;
    }

    /**
     * Replaces the cached rewards with non-null entries of positive weight from
     * {@code letterrush/rewards} JSON files, skipping paths containing {@code /example}.
     * Read or parse failures stop processing that file but retain entries already accepted.
     * An empty result makes {@link #activeRewards()} fall back to the config list.
     * Resource discovery failures propagate without replacing the cached rewards.
     */
    public static void reload(ResourceManager manager) {
        List<RewardDefinition> merged = new ArrayList<>();
        Map<Identifier, Resource> found = manager.listResources("letterrush/rewards", id -> id.getPath().endsWith(".json"));
        for (Map.Entry<Identifier, Resource> entry : found.entrySet()) {
            String path = entry.getKey().getPath();
            if (path.contains("/example") || path.endsWith("/example_datapack.json")) {
                continue;
            }
            try (InputStreamReader reader = new InputStreamReader(entry.getValue().open(), StandardCharsets.UTF_8)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                if (root.has("rewards")) {
                    for (var element : root.getAsJsonArray("rewards")) {
                        RewardDefinition reward = GSON.fromJson(element, RewardDefinition.class);
                        if (reward != null && reward.weight > 0) {
                            merged.add(reward);
                        }
                    }
                }
            } catch (Exception e) {
                LetterRush.LOGGER.warn("Failed to read reward datapack {}", entry.getKey(), e);
            }
        }
        datapackRewards = List.copyOf(merged);
        if (!datapackRewards.isEmpty()) {
            LetterRush.LOGGER.info("Loaded {} LetterRush reward(s) from datapacks.", datapackRewards.size());
        }
    }

    public static List<RewardDefinition> activeRewards() {
        if (!datapackRewards.isEmpty()) {
            return datapackRewards;
        }
        return LetterRushConfig.get().rewards;
    }
}
