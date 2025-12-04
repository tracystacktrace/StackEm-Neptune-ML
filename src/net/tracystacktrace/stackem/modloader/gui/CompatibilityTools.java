package net.tracystacktrace.stackem.modloader.gui;

import net.minecraft.src.StringTranslate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class CompatibilityTools {
    private static final Map<String, String> ownTranslateKey = new HashMap<>();
    public static boolean EXTENDED_CHARSET = false;
    public static boolean EXTENDED_PREFIX_VALUES = false;

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
            EXTENDED_CHARSET = true;
            EXTENDED_PREFIX_VALUES = true;
        }
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
