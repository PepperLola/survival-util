package io.github.themorningcompany.survivalutil.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Objects;

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

    public static String getDisconnectReason(PlayerEntity player, DamageSource damageSource) {
        String playerDisplayName = player.getDisplayName().getString();
        String damageLocation;
        String damageType;

        if (damageSource == null) {
            damageLocation = "N/A";
            damageType = "None";
        } else {
            if (damageSource.getDamageLocation() == null) {
                damageLocation = "N/A";
            } else {
                damageLocation = damageSource.getDamageLocation().toString();
            }
            damageType = damageSource.getDamageType();
        }

        return String.format("Player Name: %s | Location: %s | Damage Source: %s", playerDisplayName, damageLocation, damageType);
    }

    public static void copyDisconnectReason(PlayerEntity player, DamageSource damageSource) {
        new Thread(() -> {
            String disconnectReason = getDisconnectReason(player, damageSource);
            System.out.println(disconnectReason);
            StringSelection selection = new StringSelection(disconnectReason);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
        }, "Disconnect Reason Copy").start();
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
