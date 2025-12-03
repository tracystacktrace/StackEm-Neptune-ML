package net.tracystacktrace.stackem.neptune.fetch;

import net.tracystacktrace.stackem.neptune.container.PreviewTexturePack;
import net.tracystacktrace.stackem.tools.NeptuneProperties;
import net.tracystacktrace.stackem.tools.SafetyTools;
import net.tracystacktrace.stackem.tools.ZipFileHelper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class FetchMaster {
    public static List<PreviewTexturePack> collectPreviews(File texturepackFolder) {
        if (!texturepackFolder.exists() || !texturepackFolder.isDirectory()) {
            return null;
        }

        final File[] candidatesArray = texturepackFolder.listFiles();

        if (candidatesArray == null || candidatesArray.length == 0) {
            return null;
        }

        final List<PreviewTexturePack> collector = new ArrayList<>();

        for (File candidate : candidatesArray) {
            if (candidate.isDirectory() || !candidate.getName().toLowerCase().endsWith(".zip")) {
                continue;
            }

            final PreviewTexturePack pack = buildPreview(candidate);
            if (pack != null) {
                collector.add(pack);
            }
        }

        return collector;
    }

    public static PreviewTexturePack buildPreview(File texturepackFile) {
        // Check for basic existence of the file
        if (!texturepackFile.exists() || !texturepackFile.isFile()) {
            System.out.printf("[Stack 'Em] Not a file (ignoring): %s\n", texturepackFile.getName());
            return null;
        }

//        // Check for file being locked
//        if (SafetyTools.isFileLocked(texturepackFile)) {
//            System.out.printf("[Stack 'Em] File is locked (ignoring): %s\n", texturepackFile.getName());
//            return null;
//        }

        // Calculate the SHA-256 of texturepack
        String sha256;
        try {
            sha256 = SafetyTools.getSHA256(texturepackFile);
        } catch (IOException e) {
            sha256 = "SHA-256 NOT CALCULATED!!!";
        }

        // Read the inside of the zip file
        try (final ZipFile zipFile = new ZipFile(texturepackFile)) {
            // Get pack.txt description lines
            final String[] packTxtContent = ZipFileHelper.readTextFile(zipFile, "pack.txt", reader -> {
                final String line1 = reader.readLine();
                final String line2 = reader.readLine();
                return new String[]{line1, line2};
            });

            // If pack.txt is empty - ignore
            if (packTxtContent == null) {
                System.out.printf("[Stack 'Em] File does not contain pack.txt: %s\n", texturepackFile.getName());
                return null;
            }

            // Safely handle empty strings of pack.txt
            if (packTxtContent[0] == null || packTxtContent[0].isEmpty())
                packTxtContent[0] = "";
            if (packTxtContent[1] == null || packTxtContent[1].isEmpty())
                packTxtContent[1] = "";

            // Construct a preview instance
            final PreviewTexturePack pack = new PreviewTexturePack(
                    texturepackFile,
                    texturepackFile.getName(),
                    packTxtContent[0],
                    packTxtContent[1],
                    sha256
            );

            // Try to obtain pack.png image (BufferedImage)
            final BufferedImage packPngImage = ZipFileHelper.readImage(zipFile, "pack.png");
            if (packPngImage != null) {
                pack.setIcon(packPngImage);
            }

            // Try to read stackem.properties
            FetchMaster.setPossibleProperties(zipFile, pack);

            zipFile.close();
            return pack;
        } catch (IOException e) {
            System.out.printf("Failed to process \"%s\" texturepack, reason: %s\n", texturepackFile.getName(), e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static void setPossibleProperties(ZipFile zipFile, PreviewTexturePack pack) {
        final ZipEntry stackemCfgZipEntry = zipFile.getEntry("stackem.properties");
        if (stackemCfgZipEntry != null) {
            try {
                final InputStream stackemCfgInputStream = zipFile.getInputStream(stackemCfgZipEntry);
                final NeptuneProperties properties = new NeptuneProperties();
                properties.open(stackemCfgInputStream);
                stackemCfgInputStream.close();

                final String target_game = properties.getString("target_game");
                final String website = properties.getString("website");
                final String[] authors = properties.getStringArray("authors");
                final String[] custom_category = properties.getStringArray("custom_category");
                final String[] category = properties.getStringArray("category");

                //put them here
                pack.setStackemData(target_game, website, authors, custom_category, category);

            } catch (IOException e) {
                System.out.printf("Failed to process [stackem.properties] of %s, reason: %s\n", pack.getName(), e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
