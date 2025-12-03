package net.tracystacktrace.stackem.neptune.category;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public enum EnumCategory {
    AUDIO("audio"),
    ANIMATION("animation"),
    BLOCKS("blocks"),
    ENTITIES("entities"),
    ENVIRONMENT("environment"),
    FONTS("fonts"),
    GUI("gui"),
    HUD("hud"),
    ITEMS("items"),
    PAINTINGS("paintings"),
    QOL("qol"),
    QUEER("queer");

    public final String identifier;

    EnumCategory(String identifier) {
        this.identifier = identifier;
    }

    public String cookI18NString() {
        return "stackem.category." + this.identifier;
    }

    public static EnumCategory define(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        final String trimmedID = id.trim();
        for (EnumCategory category : EnumCategory.values()) {
            if (category.identifier.equalsIgnoreCase(trimmedID)) {
                return category;
            }
        }

        return null;
    }

    @SuppressWarnings({"ForLoopReplaceableByForEach", "ManualArrayToCollectionCopy", "UseBulkOperation"})
    public static String[] collect(
            Function<String, String> translator,
            EnumCategory[] categories,
            String[] custom
    ) {
        final List<String> names = new ArrayList<>();

        if (categories != null && categories.length > 0) {
            for (int i = 0; i < categories.length; i++) {
                names.add(translator.apply(categories[i].cookI18NString()));
            }
        }

        if (custom != null && custom.length > 0) {
            for (int i = 0; i < custom.length; i++) {
                names.add(custom[i]);
            }
        }

        if (names.isEmpty()) {
            return null;
        }

        return names.toArray(new String[0]);
    }
}
