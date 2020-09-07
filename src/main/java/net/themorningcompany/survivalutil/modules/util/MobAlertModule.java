package net.themorningcompany.survivalutil.modules.util;

import net.themorningcompany.survivalutil.modules.Module;
import net.themorningcompany.survivalutil.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobAlertModule extends Module {
    private List<Entity> currentEntities;
    private List<Entity> previousEntities;

    private static Map<EntityType, String> mobColors = new HashMap<>();

    static {
        mobColors.put(EntityType.CREEPER, TextFormatting.GREEN.toString());
        mobColors.put(EntityType.ZOMBIE, TextFormatting.DARK_GREEN.toString());
        mobColors.put(EntityType.ZOMBIE_VILLAGER, TextFormatting.DARK_GREEN.toString());
        mobColors.put(EntityType.ZOMBIE_HORSE, TextFormatting.DARK_GREEN.toString());
        mobColors.put(EntityType.SPIDER, TextFormatting.DARK_PURPLE.toString());
        mobColors.put(EntityType.ENDERMAN, TextFormatting.LIGHT_PURPLE.toString());
        mobColors.put(EntityType.SKELETON, TextFormatting.GRAY.toString());
    }

    public MobAlertModule() {
        super("mob-alert", "Mob Alerts", ModuleType.UTIL);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        previousEntities = currentEntities;
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        Vector3d pos1 = player.getPositionVec().subtract(10, 10, 10);
        Vector3d pos2 = player.getPositionVec().add(10, 10, 10);
        currentEntities = player.world.getEntitiesInAABBexcluding(player, new AxisAlignedBB(pos1, pos2), entity -> (entity instanceof MonsterEntity));

        if (currentEntities == null || previousEntities == null) return;

        // arriving and leaving logic
        currentEntities.forEach((entity) -> {
            if (!previousEntities.contains(entity)) {
                mobEntered(entity);
            }
        });

        previousEntities.forEach((entity) -> {
            if (!currentEntities.contains(entity)) {
                mobLeft(entity);
            }
        });
    }

    private void mobEntered(Entity entity) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        if (entity == null) return;
        String entityName = I18n.format(entity.getType().getName().getString());
        if (mobColors.containsKey(entity.getType())) {
            entityName = mobColors.get(entity.getType()) + entityName;
        }
        MCUtil.sendPlayerMessage(Minecraft.getInstance().player, String.format("%s has entered the radius.", entityName));
    }

    private void mobLeft(Entity entity) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        if (entity == null) return;
        String entityName = I18n.format(entity.getType().getName().getString());
        MCUtil.sendPlayerMessage(Minecraft.getInstance().player, String.format("%s has left the radius.", entityName));
    }
}
