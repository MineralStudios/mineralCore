package de.jeezycore.main;

import de.jeezycore.colors.Color;
import de.jeezycore.commands.CreateRank;
import de.jeezycore.commands.GrantRank;
import de.jeezycore.commands.JeezyCoreGuide;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.events.ChatEvent;
import de.jeezycore.events.ClickEvent;
import de.jeezycore.events.JoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.GREEN_BOLD+" Successfully"+Color.CYAN_BOLD+" started JeezyCore coded by JeezyDevelopment!"+Color.RESET);
        // Register Commands
        this.getCommand("jeezycore-guide").setExecutor(new JeezyCoreGuide());
        this.getCommand("create-rank").setExecutor(new CreateRank());
        this.getCommand("grant-rank").setExecutor(new GrantRank());
        // Register Listener
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new ClickEvent(), this);
        // Creating Connection / Creating Table
        JeezySQL con = new JeezySQL();
        con.createTable();
    }

    @Override
    public void onDisable() {
        System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" shutting down..."+Color.RESET);
    }


}
