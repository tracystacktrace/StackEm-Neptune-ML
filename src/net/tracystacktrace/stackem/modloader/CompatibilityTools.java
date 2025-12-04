package net.tracystacktrace.stackem.modloader;

import net.minecraft.src.GuiSlot;
import net.minecraft.src.StringTranslate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class CompatibilityTools {
    private static final Map<String, String> ownTranslateKey = new HashMap<>();
    public static boolean RESIZABLE_WIDTH = false;

    private static boolean classExists(String s) {
        try {
            Class.forName(s);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void getKnownWithEnvironment() {
        if (classExists("net.minecraft.src.mod_NFC") || classExists("mod_NFC")) {
            RESIZABLE_WIDTH = true;
        }
        //TODO: Add compatibility for other mods
    }

    public static void loadingPresentLang() {
        final InputStream inputStream = CompatibilityTools.class.getResourceAsStream("/stackem.default.lang");
        if (inputStream == null) {
            System.out.println("ACHTUNG ass me");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#") || !line.contains("=")) {
                    continue;
                }

                final String[] rawSplit = line.split("=");
                ownTranslateKey.put(rawSplit[0], rawSplit[1]);
            }
        } catch (IOException e) {
            System.out.println("ACHTUNG ass");
            e.printStackTrace();
        }
    }

    public static boolean isValidWebsite(String website) {
        return website != null && (website.startsWith("https://") || website.startsWith("http://"));
    }

    public static void resizeWidth(GuiSlot slot, int w) {
        if (RESIZABLE_WIDTH) {
            try {
                final Field fieldL = GuiSlot.class.getDeclaredField("boxWidthLeft");
                final Field fieldR = GuiSlot.class.getDeclaredField("boxWidthRight");
                fieldL.setAccessible(true);
                fieldR.setAccessible(true);

                final int splitW = w / 2;
                fieldL.set(slot, splitW);
                fieldR.set(slot, splitW);
            } catch (NoSuchFieldException e) {
                System.out.println("Failed to resize the width");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.out.println("LOL");
                e.printStackTrace();
            }
        }
    }

    public static String translateKey(String key) {
        final String result = StringTranslate.getInstance().translateKey(key);
        if (key.equals(result)) {
            return ownTranslateKey.getOrDefault(key, "N/A " + key);
        }
        return result;
    }

    public static String translateKey(String key, String arg1) {
        final String result = StringTranslate.getInstance().translateKey(key);
        if (key.equals(result)) {
            return String.format(ownTranslateKey.getOrDefault(key, "N/A " + key), arg1);
        }
        return String.format(result, arg1);
    }
}
