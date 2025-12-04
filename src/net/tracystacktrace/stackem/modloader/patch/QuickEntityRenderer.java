package net.tracystacktrace.stackem.modloader.patch;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityRendererProxy;
import net.minecraft.src.mod_StackEmNeptune;

/**
 * A very very very crude but nice way to make the GUI swap be done quicker!
 * <br>
 * However, I remember that some mods do this too, so OnTickInGUI and OnTickInGame were also added
 */
public class QuickEntityRenderer extends EntityRendererProxy {
    private final Minecraft client;

    public QuickEntityRenderer(Minecraft client) {
        super(client);
        this.client = client;
    }

    public void updateCameraAndRender(float f) {
        mod_StackEmNeptune.doTick(this.client);
        super.updateCameraAndRender(f);
    }
}
