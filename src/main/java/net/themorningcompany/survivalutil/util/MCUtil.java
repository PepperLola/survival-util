package net.themorningcompany.survivalutil.util;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.ArrayUtils;
import sun.security.util.ArrayUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;

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

        damageLocation = player.getPositionVec().toString();

        if (damageSource == null) {
            damageType = "None";
        } else {
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

    public static int getSlotOfItem(PlayerEntity player, Item itemStack) {
        System.out.println(itemStack);
        PlayerInventory inv = player.inventory;
        for (int i = 0; i < inv.mainInventory.size(); i++) {
            if (inv.mainInventory.get(i).getItem().equals(itemStack)) {
                return i;
            }
        }
        return -1;
    }
}
