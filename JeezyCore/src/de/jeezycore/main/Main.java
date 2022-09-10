package de.jeezycore.main;

import de.jeezycore.colors.Color;
import de.jeezycore.commands.*;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.events.ChatEvent;
import de.jeezycore.events.InventoryClickEvent;
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
        this.getCommand("chat-disable").setExecutor(new ChatDisabler());
        this.getCommand("chat-enable").setExecutor(new ChatDisabler());
        this.getCommand("jeezy-spawn-set").setExecutor(new SetSpawn());
        // Register Listener
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
        // Creating Connection / Creating Table
        JeezySQL con = new JeezySQL();
        con.createTable();
    }

    @Override
    public void onDisable() {
        System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" shutting down..."+Color.RESET);
    }


}
