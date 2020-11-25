package net.themorningcompany.survivalutil.modules.util;

import net.themorningcompany.survivalutil.modules.Module;
import net.themorningcompany.survivalutil.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class DurabilityNotificationModule extends Module {

    private List<Item> warns = new ArrayList<>();

    public DurabilityNotificationModule() {
        super("durability-notification", "Durability Notifier", ModuleType.UTIL);
    }

    @SubscribeEvent
    public void onToolUse(TickEvent.ClientTickEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (!this.isEnabled()) return;
        if (player == null) return;
        PlayerInventory inv = player.inventory;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack item = inv.getStackInSlot(i);
            int itemDamage = item.getDamage();
            if (item.getMaxDamage() == 0) return;
            String warnString = "Your " + (item.hasDisplayName() ? String.format("\"%s\"", item.getDisplayName().getString()) : item.getItem().getName().getString()) + " is on " + (item.getMaxDamage() - itemDamage) + " durability.";
            if (warns.contains(item.getItem()) && itemDamage >= item.getMaxDamage() - 1) {
                MCUtil.sendPlayerMessage(player, warnString, TextFormatting.RED);
                warns.remove(item.getItem());
            } else if (!warns.contains(item.getItem()) && itemDamage >= item.getMaxDamage() - 10 && itemDamage < item.getMaxDamage() - 1) {
                MCUtil.sendPlayerMessage(player, warnString, TextFormatting.RED);
                warns.add(item.getItem());
            }
        }
    }
}
