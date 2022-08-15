package de.jeezycore.main;

import de.jeezycore.commands.JeezyCoreGuide;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.events.ChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Successfully started JeezyCore coded by JeezyDevelopment!");
        // Register Commands
        this.getCommand("jeezycore-guide").setExecutor(new JeezyCoreGuide());
        // Register Listener
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        // Creating Connection
        JeezySQL con = new JeezySQL();
        con.createConnection();
    }

    @Override
    public void onDisable() {
        System.out.println("Stopped JeezyCore!");
    }


}
