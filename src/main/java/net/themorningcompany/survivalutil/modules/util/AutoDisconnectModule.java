package net.themorningcompany.survivalutil.modules.util;

import net.themorningcompany.survivalutil.modules.Module;
import net.themorningcompany.survivalutil.util.AFKUtil;
import net.themorningcompany.survivalutil.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AutoDisconnectModule extends Module {

    private boolean shouldDisconnect = false;
    private DamageSource lastDamageSource = null;

    public AutoDisconnectModule() {
        super("auto-disconnect", "Auto-Disconnect", ModuleType.UTIL);
    }

    @SubscribeEvent
    public void onSoundPlay(SoundEvent.SoundSourceEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (!this.isEnabled()) return;
        if (player == null) return;

        boolean isCreeper = event.getName().equals("entity.creeper.primed");
        boolean isSurvival = MCUtil.isPlayerInSurvival(player);
        if (isCreeper && isSurvival && AFKUtil.isAFK()) {
            shouldDisconnect = true;
        }
//        System.out.println(event.getName());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (!this.isEnabled()) return;
        if (player == null) return;
        double playerHealth = player.getHealth();
        if (playerHealth < 1) {
            MCUtil.copyDisconnectReason(player, lastDamageSource);
            if (MCUtil.isPlayerInSurvival(player) || shouldDisconnect) {
                MCUtil.disconnectFromServer();
            }
        }
        shouldDisconnect = false;
    }

    @SubscribeEvent
    public void onDamage(LivingDamageEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (!this.isEnabled()) return;
        if (player == null) return;
        if (!(event.getEntity() instanceof PlayerEntity)) return;
        if (!(event.getEntity() == player)) return;

        lastDamageSource = event.getSource();
    }
}
