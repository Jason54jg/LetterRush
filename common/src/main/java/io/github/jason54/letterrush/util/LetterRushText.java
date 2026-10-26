package io.github.jason54.letterrush.util;

import io.github.jason54.letterrush.config.LetterRushConfig;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * Resolves configurable message templates with translation fallbacks.
 */
public final class LetterRushText {
    private LetterRushText() {
    }

    public static MutableText message(String configKey, String translationKey, Object... args) {
        String template = LetterRushConfig.get().messages().get(configKey);
        if (template != null && !template.isBlank()) {
            return Text.literal(format(template, args));
        }
        return Text.translatable(translationKey, args);
    }

    private static String format(String template, Object... args) {
        String result = template;
        for (int i = 0; i < args.length; i++) {
            result = result.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return result;
    }
}
