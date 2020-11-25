package net.themorningcompany.survivalutil.command;


import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.themorningcompany.survivalutil.SurvivalUtil;

public class ModuleCommand {
    public ModuleCommand() {}

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("module").requires(source -> source.hasPermissionLevel(0))
            .then(Commands.literal("enable").then(Commands.argument("module", MessageArgument.message())
                .executes(context -> {
                    String moduleId = MessageArgument.getMessage(context, "module").getUnformattedComponentText();
                    SurvivalUtil.modules.forEach(module -> {
                        if (module.getId().equals(moduleId)) {
                            module.enable();
                        }
                    });
                    return 1;
                })))
            .then(Commands.literal("disable").then(Commands.argument("module", MessageArgument.message())
                .executes(context -> {
                    String moduleId = MessageArgument.getMessage(context, "module").getUnformattedComponentText();
                    SurvivalUtil.modules.forEach(module -> {
                        if (module.getId().equals(moduleId)) {
                            module.disable();
                        }
                    });
                    return 1;
                })))
            .then(Commands.literal("toggle").then(Commands.argument("module", MessageArgument.message())
                .executes(context -> {
                    String moduleId = MessageArgument.getMessage(context, "module").getUnformattedComponentText();
                    SurvivalUtil.modules.forEach(module -> {
                        if (module.getId().equals(moduleId)) {
                            module.setEnabled(!module.isEnabled());
                        }
                    });
                    return 1;
                })))
        );
    }
}
