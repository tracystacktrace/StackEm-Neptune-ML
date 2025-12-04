package net.tracystacktrace.stackem.modloader.imageglue.segment;

import net.tracystacktrace.stackem.modloader.CompatibilityTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SegmentsProvider {
    public static SegmentedTexture[] TEXTURES;

    public static void loadSegmentsData() {
        final InputStream inputStream = CompatibilityTools.class.getResourceAsStream("/stackem.segments.txt");
        if (inputStream == null) {
            CompatibilityTools.log("Couldn't find stackem.segments.txt! Corrupted mod zip?");
            return;
        }

        final List<SegmentedTexture> candidates = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || !line.contains(":")) {
                    continue;
                }
                candidates.add(tryToUnpack(line));
            }
        } catch (IOException e) {
            CompatibilityTools.log("Failed to load stackem.segments.txt, expect problems: " + e.getMessage());
            e.printStackTrace();
        }

        TEXTURES = candidates.toArray(new SegmentedTexture[0]);
    }

    // texture/path.png: [];[]
    private static SegmentedTexture tryToUnpack(String line) {
        final String[] rawStrip = line.split(":");

        final int[][] data = Arrays.stream(rawStrip[1].trim().split(";"))
                .map(String::trim)
                .filter(s -> s.startsWith("[") && s.endsWith("]"))
                .map(SegmentsProvider::unpackSegment)
                .distinct()
                .toArray(int[][]::new);

        return new SegmentedTexture(rawStrip[0].trim(), data);
    }

    private static int[] unpackSegment(String s) {
        final String[] rawStrings = s.substring(1, s.length() - 1).split(",");
        final int[] cookedInts = new int[rawStrings.length];
        for (int i = 0; i < cookedInts.length; i++) {
            cookedInts[i] = Integer.parseInt(rawStrings[i]);
        }
        return cookedInts;
    }

}
