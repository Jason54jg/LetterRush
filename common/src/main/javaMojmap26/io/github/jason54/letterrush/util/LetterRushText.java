package io.github.jason54.letterrush.util;

import io.github.jason54.letterrush.config.LetterRushConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/** Resolves configurable message templates with translation fallbacks. */
public final class LetterRushText {
    private LetterRushText() {
    }

    public static MutableComponent message(String configKey, String translationKey, Object... args) {
        String template = LetterRushConfig.get().messages().get(configKey);
        if (template != null && !template.isBlank()) {
            return Component.literal(format(template, args));
        }
        return Component.translatable(translationKey, args);
    }

    private static String format(String template, Object... args) {
        String result = template;
        for (int i = 0; i < args.length; i++) {
            result = result.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return result;
    }
}
