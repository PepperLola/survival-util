package io.github.themorningcompany.survivalutil.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class MCUtil {
    public static void sendPlayerMessage(ClientPlayerEntity player, String message) {
        player.sendMessage(new StringTextComponent(message), player.getUniqueID());
    }

    public static void sendPlayerMessage(ClientPlayerEntity player, String message, TextFormatting formatting) {
        player.sendMessage(new StringTextComponent(message).func_240699_a_(formatting), player.getUniqueID());
    }

    public static boolean isPlayerInSurvival(PlayerEntity player) {
        return !(player.isCreative() || player.isSpectator());
    }

    public static void disconnectFromServer() {
        boolean isSinglePlayer = Minecraft.getInstance().isIntegratedServerRunning();
        boolean isConnectedToRealms = Minecraft.getInstance().isConnectedToRealms();

        if (Minecraft.getInstance().world == null) return;

        Minecraft.getInstance().world.sendQuittingDisconnectingPacket();

        if (isSinglePlayer) {
            Minecraft.getInstance().unloadWorld(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
        } else {
            Minecraft.getInstance().unloadWorld();
        }

        if (isSinglePlayer) {
            Minecraft.getInstance().displayGuiScreen(new MainMenuScreen());
        } else if (isConnectedToRealms) {
            RealmsBridgeScreen realmsbridgescreen = new RealmsBridgeScreen();
            realmsbridgescreen.func_231394_a_(new MainMenuScreen());
        } else {
            Minecraft.getInstance().displayGuiScreen(new MultiplayerScreen(new MainMenuScreen()));
        }
    }
}
