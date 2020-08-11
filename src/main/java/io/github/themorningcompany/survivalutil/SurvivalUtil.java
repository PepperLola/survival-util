package io.github.themorningcompany.survivalutil;

import io.github.themorningcompany.survivalutil.modules.Module;
import io.github.themorningcompany.survivalutil.modules.util.AutoDisconnectModule;
import io.github.themorningcompany.survivalutil.modules.util.DurabilityNotificationModule;
import io.github.themorningcompany.survivalutil.modules.util.MobAlertModule;
import io.github.themorningcompany.survivalutil.util.AFKUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("survivalutil")
public class SurvivalUtil
{
    private static final Logger LOGGER = LogManager.getFormatterLogger("SurvivalUtil");
    public static final String MOD_ID = "survivalutil";

    public static List<Module> modules = new ArrayList<Module>();

    static {
        modules.addAll(Arrays.asList(
                new MobAlertModule(),
                new AutoDisconnectModule(),
                new DurabilityNotificationModule()
        ));
    }

    public SurvivalUtil() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new AFKUtil());

        System.setProperty("java.awt.headless", "false");

        LOGGER.info("Enabling mod...");
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }
}

