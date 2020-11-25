package net.themorningcompany.survivalutil.modules.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.themorningcompany.survivalutil.modules.Module;

import java.util.Collections;

import static net.themorningcompany.survivalutil.SurvivalUtil.MOD_CATEGORY;

public class ElytraSwitchModule extends Module {
    public ElytraSwitchModule() {
        super("elytra-switch", "Elytra Switch", ModuleType.UTIL);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        KeyBinding[] keybinds = Minecraft.getInstance().gameSettings.keyBindings;

        for (KeyBinding keybind : keybinds) {
            if (!keybind.getKeyCategory().equals(MOD_CATEGORY)) continue;
            if (!keybind.isKeyDown()) continue;

            String key_id = keybind.getKeyDescription();
            System.out.println(key_id);
            if (key_id.equals("key.elytraSwitch")) {
                PlayerEntity player = Minecraft.getInstance().player;
                World playerWorld = player.getEntityWorld();
                ItemStack chestplateItem = player.inventory.armorItemInSlot(2);
                String chestplateItemName = chestplateItem.getItem().getName().getString().toLowerCase();
                System.out.println(chestplateItemName);
                player.inventory.mainInventory.forEach(item -> {
                    String itemName = item.getItem().getName().getString().toLowerCase();
                    if (chestplateItemName.contains("elytra") && itemName.contains("chestplate")) {
                        player.inventory.mainInventory.set(player.inventory.mainInventory.indexOf(item), chestplateItem);
                        player.inventory.armorInventory.set(player.inventory.armorInventory.indexOf(chestplateItem), item);
                    } else if (chestplateItemName.contains("chestplate") && itemName.contains("elytra")) {
                        player.inventory.mainInventory.set(player.inventory.mainInventory.indexOf(item), chestplateItem);
                        player.inventory.armorInventory.set(player.inventory.armorInventory.indexOf(chestplateItem), item);
                    }
                });
            }
        }
    }
}
