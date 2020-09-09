package net.themorningcompany.survivalutil.modules.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.themorningcompany.survivalutil.modules.Module;
import net.themorningcompany.survivalutil.util.MCUtil;
import org.lwjgl.system.CallbackI;

public class AutoWaterBucketModule extends Module {
    public AutoWaterBucketModule() {
        super("auto-water-bucket", "Auto Water Bucket", ModuleType.UTIL);
    }

    @SubscribeEvent
    public void onPlayerFall(LivingFallEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (event.getEntity() == null) return;
        if (!(event.getEntity() instanceof PlayerEntity)) return;
        if (!event.getEntity().equals(player)) return;

        System.out.println(player.fallDistance);

        Vector3d position = player.getPositionVec().subtract(0, 2, 0);
        boolean isBlockBelowSolid = player.getEntityWorld().getBlockState(new BlockPos(position)).isSolid();

        if (isBlockBelowSolid) {
            int waterBucketSlot = MCUtil.getSlotOfItem(player, Item.getItemById(661)); // 661 = water bucket item id
            ItemStack previousItemStack;
            ItemStack waterBucketStack = player.inventory.getStackInSlot(waterBucketSlot);
            if (waterBucketSlot < 9) {
                player.inventory.currentItem = waterBucketSlot;
            } else {
                previousItemStack = player.inventory.getStackInSlot(0);
                player.inventory.setInventorySlotContents(0, waterBucketStack);
            }

        }
    }
}
