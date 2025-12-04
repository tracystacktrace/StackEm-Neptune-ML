package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.tracystacktrace.stackem.modloader.CacheConfig;
import net.tracystacktrace.stackem.modloader.ModLoaderStackedImpl;
import net.tracystacktrace.stackem.modloader.patch.CompatibilityTools;
import net.tracystacktrace.stackem.modloader.gui.GuiTextureStack;
import net.tracystacktrace.stackem.modloader.imageglue.ImageGlueBridge;
import net.tracystacktrace.stackem.modloader.imageglue.segment.SegmentsProvider;
import net.tracystacktrace.stackem.modloader.patch.QuickEntityRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class mod_StackEmNeptune extends BaseMod {

    @Override
    public String Version() {
        return "1.0";
    }

    public String Name() {
        return "Stack 'Em Neptune";
    }

    public String Description() {
        return "Support for stacked texturepacks with runtime texture gluing!";
    }

    public String Icon() {
        return "/net/tracystacktrace/stackem/icon.png";
    }

    public mod_StackEmNeptune() {
    }

    public static void doTick(Minecraft client) {
        if (client.currentScreen != null && client.currentScreen.getClass().isAssignableFrom(GuiTexturePacks.class)) {
            client.displayGuiScreen(new GuiTextureStack(((GuiTexturePacks) client.currentScreen).guiScreen));
        }
    }

    @Override
    public boolean OnTickInGame(Minecraft client) {
        if (client.currentScreen != null && client.currentScreen.getClass().isAssignableFrom(GuiTexturePacks.class)) {
            client.displayGuiScreen(new GuiTextureStack(((GuiTexturePacks) client.currentScreen).guiScreen));
            return true;
        }
        return false;
    }

    @Override
    public boolean OnTickInGUI(Minecraft client, GuiScreen gui) {
        if (gui != null && gui.getClass().isAssignableFrom(GuiTexturePacks.class)) {
            client.displayGuiScreen(new GuiTextureStack(((GuiTexturePacks) gui).guiScreen));
            return true;
        }
        return false;
    }

    @Override
    public void ModsLoaded() {
        if(!(ModLoader.getMinecraftInstance().entityRenderer instanceof QuickEntityRenderer)) {
            CompatibilityTools.log("Warning! Something cancelled custom EntityRenderer code; are you using OverrideAPI?");
            ModLoader.SetInGameHook(this, true, true);
            ModLoader.SetInGUIHook(this, true, true);
        }
    }

    static {
        CompatibilityTools.log("Preparing the environment, thinking very hard!");
        CompatibilityTools.getKnownWithEnvironment();
        CompatibilityTools.loadingPresentLang();
        SegmentsProvider.loadSegmentsData();

        if(CompatibilityTools.OBFUSCATED_ENV) {
            CompatibilityTools.log("Running in DEV environment, no obfuscation present!");
        }

        CompatibilityTools.log("Initializing mod, applying required patches");
        final Minecraft client = ModLoader.getMinecraftInstance();

        // Apply quick proxy for faster tick processing
        client.entityRenderer = new QuickEntityRenderer(client);

        // Quickly form config folder
        final File configFolder = new File(Minecraft.getMinecraftDir(), "config");
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }

        // Fallback to default texturepack
        client.texturePackList.selectedTexturePack = new TexturePackDefault();

        final List<File> collector = new ArrayList<>();
        final String[] candidates = CacheConfig.getCacheData(configFolder);
        final File[] files = CacheConfig.getPossibleTexturePacks(Minecraft.getMinecraftDir());

        // Stream to collect enough data
        Arrays.stream(candidates)
                .map(c -> Arrays.stream(files)
                        .filter(f -> f.getName().toLowerCase().endsWith(".zip"))
                        .filter(f -> f.getName().contains(c))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .forEach(collector::add);

        CompatibilityTools.log("How many texturepacks were pre-fetched? " + collector.size());

        // Force set current texturepack as StackEm internal implementation
        client.texturePackList.selectedTexturePack = new ModLoaderStackedImpl(client.texturePackList.selectedTexturePack, collector);

        ImageGlueBridge.processTexturesSegments(client.renderEngine);
    }
}
