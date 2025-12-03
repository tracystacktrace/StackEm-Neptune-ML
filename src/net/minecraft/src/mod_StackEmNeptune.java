package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.tracystacktrace.stackem.modloader.CacheConfig;
import net.tracystacktrace.stackem.modloader.ModLoaderStackedImpl;
import net.tracystacktrace.stackem.modloader.gui.GuiTextureStack;

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

    public mod_StackEmNeptune() {
        System.out.println("Loading Stack' Em, Part 2");
        ModLoader.SetInGUIHook(this, true, true);
    }

    @Override
    public boolean OnTickInGUI(Minecraft game, GuiScreen current) {
        if (game.currentScreen != null && game.currentScreen.getClass().isAssignableFrom(GuiTexturePacks.class)) {
            game.displayGuiScreen(new GuiTextureStack(((GuiTexturePacks) game.currentScreen).guiScreen));

//            game.currentScreen = new GuiTextureStack(((GuiTexturePacks)game.currentScreen).guiScreen);
//            game.setIngameNotInFocus();
//            ScaledResolution var2 = new ScaledResolution(game.gameSettings, game.displayWidth, game.displayHeight);
//            int var3 = var2.getScaledWidth();
//            int var4 = var2.getScaledHeight();
//            game.currentScreen.setWorldAndResolution(game, var3, var4);
//            game.skipRenderWorld = false;
        }
        return true;
    }

    static {
        System.out.println("Initializing Stack 'Em, Part 1");
        final Minecraft client = ModLoader.getMinecraftInstance();

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

        // Force set current texturepack as StackEm internal implementation
        client.texturePackList.selectedTexturePack = new ModLoaderStackedImpl(client.texturePackList.selectedTexturePack, collector);
    }
}
