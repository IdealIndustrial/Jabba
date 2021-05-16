package mcp.mobius.betterbarrels.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DelayedUpdates {

    public DelayedUpdates() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    private static final List<DelayCoords> delayCoords = new LinkedList<DelayCoords>();
    
    public static void mark(int x, int y, int z) {
        delayCoords.add(new DelayCoords(x, y, z, 3));
    }

    @SubscribeEvent
    public void onPlayerTickEventClient(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.SERVER || delayCoords.isEmpty())
            return;
        Iterator<DelayCoords> iterator = delayCoords.iterator();
        while (iterator.hasNext()) {
            DelayCoords coords = iterator.next();
            if (--coords.timer <= 0) {
                Minecraft.getMinecraft().theWorld.markBlockForUpdate(coords.x, coords.y, coords.z);
                iterator.remove();
            }
        }
    }

    private static class DelayCoords {
        int x, y, z, timer;

        public DelayCoords(int x, int y, int z, int timer) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.timer = timer;
        }
    }
}
