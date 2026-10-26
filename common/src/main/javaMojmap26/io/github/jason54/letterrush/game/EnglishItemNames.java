package io.github.jason54.letterrush.game;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.jason54.letterrush.LetterRush;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.Identifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Resolves item display names in English so letter matching is locale-independent.
 */
public final class EnglishItemNames {
    private static Map<String, String> englishByKey;
    /** Uppercase A–Z letters that have at least one registered item name. */
    private static Set<Character> lettersWithItems;
    private static final Identifier EN_US = Identifier.fromNamespaceAndPath("minecraft", "lang/en_us.json");

    private EnglishItemNames() {
    }

    public static void invalidateCache() {
        englishByKey = null;
        lettersWithItems = null;
    }

    private static void ensureLoaded(MinecraftServer server) {
        if (englishByKey != null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        // Prefer server resource packs, then classpath (client assets are not on SERVER_DATA).
        if (!loadFromResourceManager(server.getResourceManager(), map)) {
            loadFromClasspath(map);
        }
        if (map.isEmpty()) {
            LetterRush.LOGGER.warn("Could not load English item names; using description-id fallback.");
        } else {
            LetterRush.LOGGER.info("Loaded {} English name entries for LetterRush matching.", map.size());
        }
        englishByKey = Map.copyOf(map);
    }

    private static boolean loadFromResourceManager(ResourceManager manager, Map<String, String> map) {
        try {
            var resources = manager.getResourceStack(EN_US);
            if (resources.isEmpty()) {
                return false;
            }
            try (InputStreamReader reader = new InputStreamReader(resources.getFirst().open(), StandardCharsets.UTF_8)) {
                putLangEntries(JsonParser.parseReader(reader).getAsJsonObject(), map);
            }
            return !map.isEmpty();
        } catch (Exception e) {
            LetterRush.LOGGER.debug("ResourceManager en_us load failed", e);
            return false;
        }
    }

    private static void loadFromClasspath(Map<String, String> map) {
        try (var stream = EnglishItemNames.class.getClassLoader().getResourceAsStream("assets/minecraft/lang/en_us.json")) {
            if (stream == null) {
                return;
            }
            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                putLangEntries(JsonParser.parseReader(reader).getAsJsonObject(), map);
            }
        } catch (Exception e) {
            LetterRush.LOGGER.debug("Classpath en_us load failed", e);
        }
    }

    private static void putLangEntries(JsonObject root, Map<String, String> map) {
        for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
            if (entry.getValue().isJsonPrimitive()) {
                map.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
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

    /**
     * Letters (A–Z) for which at least one registered item has an English name starting with that letter.
     */
    public static Set<Character> lettersWithItems(MinecraftServer server) {
        if (lettersWithItems != null) {
            return lettersWithItems;
        }
        ensureLoaded(server);
        Set<Character> found = new TreeSet<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack stack = item.getDefaultInstance();
            if (stack.isEmpty()) {
                continue;
            }
            Character letter = firstLetterOf(getMatchName(server, stack));
            if (letter != null) {
                found.add(letter);
            }
        }
        lettersWithItems = Set.copyOf(found);
        LetterRush.LOGGER.info(
                "LetterRush item letter index: {} letters covering {} items — {}",
                lettersWithItems.size(),
                BuiltInRegistries.ITEM.size(),
                lettersWithItems
        );
        return lettersWithItems;
    }

    /**
     * Config letters intersected with letters that have real items. Falls back to all item letters if empty.
     */
    public static List<Character> playableLetterPool(MinecraftServer server, List<Character> configured) {
        Set<Character> available = lettersWithItems(server);
        List<Character> pool = new ArrayList<>();
        Set<Character> seen = new HashSet<>();
        for (Character c : configured) {
            char upper = Character.toUpperCase(c);
            if (available.contains(upper) && seen.add(upper)) {
                pool.add(upper);
            }
        }
        if (pool.isEmpty()) {
            pool.addAll(available);
        }
        return pool;
    }

    private static Character firstLetterOf(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String trimmed = name.stripLeading();
        if (trimmed.isEmpty()) {
            return null;
        }
        char first = trimmed.charAt(0);
        if (!Character.isLetter(first)) {
            return null;
        }
        return Character.toUpperCase(first);
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
