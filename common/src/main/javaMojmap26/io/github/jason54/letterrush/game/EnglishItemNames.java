package io.github.jason54.letterrush.game;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.jason54.letterrush.LetterRush;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Resolves item display names in English so letter matching is locale-independent.
 */
public final class EnglishItemNames {
    private static Map<String, String> englishByKey;
    private static final Identifier EN_US = Identifier.fromNamespaceAndPath("minecraft", "lang/en_us.json");

    private EnglishItemNames() {
    }

    public static void invalidateCache() {
        englishByKey = null;
    }

    private static void ensureLoaded(MinecraftServer server) {
        if (englishByKey != null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        ResourceManager manager = server.getResourceManager();
        try {
            var resources = manager.getResourceStack(EN_US);
            if (!resources.isEmpty()) {
                try (InputStreamReader reader = new InputStreamReader(resources.getFirst().open(), StandardCharsets.UTF_8)) {
                    JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                        if (entry.getValue().isJsonPrimitive()) {
                            map.put(entry.getKey(), entry.getValue().getAsString());
                        }
                    }
                }
            } else {
                LetterRush.LOGGER.warn("Could not load minecraft:lang/en_us.json; falling back to description ids.");
            }
        } catch (Exception e) {
            LetterRush.LOGGER.error("Failed to load English language file", e);
        }
        englishByKey = Map.copyOf(map);
    }

    /**
     * Returns the name used for letter matching (English for vanilla items, custom name when renamed).
     */
    public static String getMatchName(MinecraftServer server, ItemStack stack) {
        if (stack.isEmpty()) {
            return "";
        }
        if (stack.hasNonDefault(DataComponents.CUSTOM_NAME)) {
            return stack.getHoverName().getString();
        }
        ensureLoaded(server);
        String descriptionId = stack.getItem().getDescriptionId();
        String english = englishByKey.get(descriptionId);
        if (english != null && !english.isBlank()) {
            return english;
        }
        int lastDot = descriptionId.lastIndexOf('.');
        if (lastDot >= 0 && lastDot + 1 < descriptionId.length()) {
            return humanizeToken(descriptionId.substring(lastDot + 1));
        }
        return stack.getHoverName().getString();
    }

    public static boolean startsWithLetter(MinecraftServer server, ItemStack stack, char letter) {
        String name = getMatchName(server, stack);
        if (name == null || name.isBlank()) {
            return false;
        }
        String trimmed = name.stripLeading();
        if (trimmed.isEmpty()) {
            return false;
        }
        char first = trimmed.charAt(0);
        if (!Character.isLetter(first)) {
            return false;
        }
        return Character.toUpperCase(first) == Character.toUpperCase(letter);
    }

    private static String humanizeToken(String token) {
        String[] parts = token.split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }
        return builder.toString();
    }
}
