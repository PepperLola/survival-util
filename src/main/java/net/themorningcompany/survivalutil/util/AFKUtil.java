package net.themorningcompany.survivalutil.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AFKUtil {
    private static boolean isAFK = false;
    private static final int requiredAFKTime = 20 * 20; // 10 seconds * 20 ticks per second
    private static int elapsedTicks = 0;

    private Vector3d lastPosition;
    private Vector3d currentPosition;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            setAFK(false);
            return;
        }

        currentPosition = player.getPositionVec();
        if (currentPosition == lastPosition) {

            elapsedTicks++;
            if (elapsedTicks > requiredAFKTime && !isAFK()) {
                setAFK(true);
                MCUtil.sendPlayerMessage(player, "You are now AFK.", TextFormatting.RED);
            }
        } else {
            elapsedTicks = 0;
            lastPosition = currentPosition;
            if (isAFK()) {
                setAFK(false);
                MCUtil.sendPlayerMessage(player, "You are no longer AFK.", TextFormatting.RED);
            }
        }
    }

    public static boolean isAFK() {
        return isAFK;
    }

    public static void setAFK(boolean AFK) {
        isAFK = AFK;
    }
}
