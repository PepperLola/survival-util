package net.themorningcompany.survivalutil;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.themorningcompany.survivalutil.command.ModuleCommand;
import net.themorningcompany.survivalutil.modules.Module;
import net.themorningcompany.survivalutil.modules.util.AutoDisconnectModule;
import net.themorningcompany.survivalutil.modules.util.DurabilityNotificationModule;
import net.themorningcompany.survivalutil.modules.util.ElytraSwitchModule;
import net.themorningcompany.survivalutil.modules.util.MobAlertModule;
import net.themorningcompany.survivalutil.util.AFKUtil;
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
    public static String MOD_CATEGORY = "Survival Util";

    public static List<Module> modules = new ArrayList<Module>();

    static {
        modules.addAll(Arrays.asList(
                new MobAlertModule(),
                new AutoDisconnectModule(),
                new DurabilityNotificationModule(),
                new ElytraSwitchModule()
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
        ClientRegistry.registerKeyBinding(new KeyBinding("key.elytraSwitch", 66, "Survival Util"));
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        LOGGER.info("REGISTERING COMMANDS!");
        ModuleCommand.register(event.getDispatcher());
    }
}

