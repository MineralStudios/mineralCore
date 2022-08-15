package de.jeezycore.main;

import de.jeezycore.colors.Color;
import de.jeezycore.commands.CreateRank;
import de.jeezycore.commands.JeezyCoreGuide;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.events.ChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.GREEN_BOLD+" Successfully"+Color.CYAN_BOLD+" started JeezyCore coded by JeezyDevelopment!"+Color.RESET);
        // Register Commands
        this.getCommand("jeezycore-guide").setExecutor(new JeezyCoreGuide());
        this.getCommand("create-rank").setExecutor(new CreateRank());
        // Register Listener
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        // Creating Connection / Creating Table
        JeezySQL con = new JeezySQL();
        con.createTable();
    }

    @Override
    public void onDisable() {
        System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" shutting down..."+Color.RESET);
    }


}
