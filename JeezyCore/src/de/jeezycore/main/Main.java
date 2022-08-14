package de.jeezycore.main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Successfully started JeezyCore coded by JeezyDevelopment!");
    }

    @Override
    public void onDisable() {
        System.out.println("Stopped JeezyCore!");
    }


}
