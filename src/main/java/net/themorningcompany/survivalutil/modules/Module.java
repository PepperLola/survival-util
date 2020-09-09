package net.themorningcompany.survivalutil.modules;

import net.minecraftforge.common.MinecraftForge;

public class Module {
    private boolean enabled = false;
    private String id;
    private String name;
    private ModuleType type;

    public Module(String id, String name, ModuleType type) {
        this.id = id;
        this.name = name;
        this.type = type;

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void enable() {
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static enum ModuleType {
        FUN,
        UTIL
    }
}
