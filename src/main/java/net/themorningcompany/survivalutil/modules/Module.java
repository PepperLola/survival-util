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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModuleType getType() {
        return type;
    }

    public void setType(ModuleType type) {
        this.type = type;
    }

    public static enum ModuleType {
        UTIL
    }
}
