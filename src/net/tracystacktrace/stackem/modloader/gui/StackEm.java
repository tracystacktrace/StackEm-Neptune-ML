package net.tracystacktrace.stackem.modloader.gui;

public final class StackEm {
    public static boolean isValidWebsite(String website) {
        return website != null && (website.startsWith("https://") || website.startsWith("http://"));
    }
}
