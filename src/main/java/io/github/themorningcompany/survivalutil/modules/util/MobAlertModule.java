package io.github.themorningcompany.survivalutil.modules.util;

import io.github.themorningcompany.survivalutil.modules.Module;
import io.github.themorningcompany.survivalutil.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class MobAlertModule extends Module {
    private List<Entity> currentEntities;
    private List<Entity> previousEntities;

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
