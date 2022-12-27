package de.jeezycore.main;

import de.jeezycore.colors.Color;
import de.jeezycore.commands.basic.*;
import de.jeezycore.commands.chat.ChatDisabler;
import de.jeezycore.commands.minerals.Minerals;
import de.jeezycore.commands.minerals.addMinerals;
import de.jeezycore.commands.permissions.PermissionAdd;
import de.jeezycore.commands.permissions.PermissionRemove;
import de.jeezycore.commands.punishments.ban.Ban;
import de.jeezycore.commands.punishments.ban.TempBan;
import de.jeezycore.commands.punishments.ban.UnBan;
import de.jeezycore.commands.punishments.mute.Mute;
import de.jeezycore.commands.punishments.mute.TempMute;
import de.jeezycore.commands.punishments.mute.UnMute;
import de.jeezycore.commands.punishments.wipe.WipeAll;
import de.jeezycore.commands.punishments.wipe.WipeBans;
import de.jeezycore.commands.punishments.wipe.WipeMutes;
import de.jeezycore.commands.ranks.CreateRank;
import de.jeezycore.commands.ranks.GrantRank;
import de.jeezycore.commands.rewards.DailyReward;
import de.jeezycore.commands.spawn.SetSpawn;
import de.jeezycore.commands.spawn.Spawn;
import de.jeezycore.commands.staff.StaffRankDisable;
import de.jeezycore.commands.staff.StaffRankEnable;
import de.jeezycore.commands.tags.*;
import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.db.MineralsSQL;
import de.jeezycore.db.RewardSQL;
import de.jeezycore.discord.JeezyBot;
import de.jeezycore.events.*;
import de.jeezycore.events.chat.ChatEvent;
import de.jeezycore.events.inventories.JeezyInventories;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.GREEN_BOLD+" Successfully"+Color.CYAN_BOLD+" started JeezyCore coded by JeezyDevelopment!"+Color.RESET);
        JeezyConfig file_Jeezy_config = new JeezyConfig();
        file_Jeezy_config.create_folder();
        // Register Commands
        this.getCommand("create-rank").setExecutor(new CreateRank());
        this.getCommand("grant").setExecutor(new GrantRank());
        this.getCommand("chat-disable").setExecutor(new ChatDisabler());
        this.getCommand("chat-enable").setExecutor(new ChatDisabler());
        this.getCommand("spawnSet").setExecutor(new SetSpawn());
        this.getCommand("spawn").setExecutor(new Spawn());
        this.getCommand("ping").setExecutor(new Ping());
        this.getCommand("msg").setExecutor(new Msg());
        this.getCommand("r").setExecutor(new Msg());
        this.getCommand("day").setExecutor(new TimeChanger());
        this.getCommand("night").setExecutor(new TimeChanger());
        this.getCommand("fly").setExecutor(new Fly());
        this.getCommand("report").setExecutor(new Report());
        this.getCommand("core").setExecutor(new Core());
        this.getCommand("permissionAdd").setExecutor(new PermissionAdd());
        this.getCommand("permissionRemove").setExecutor(new PermissionRemove());
        this.getCommand("ban").setExecutor(new Ban());
        this.getCommand("tempban").setExecutor(new TempBan());
        this.getCommand("unban").setExecutor(new UnBan());
        this.getCommand("mute").setExecutor(new Mute());
        this.getCommand("tempmute").setExecutor(new TempMute());
        this.getCommand("unmute").setExecutor(new UnMute());
        this.getCommand("wipebans").setExecutor(new WipeBans());
        this.getCommand("wipemutes").setExecutor(new WipeMutes());
        this.getCommand("wipeall").setExecutor(new WipeAll());
        this.getCommand("staffrank-enable").setExecutor(new StaffRankEnable());
        this.getCommand("staffrank-disable").setExecutor(new StaffRankDisable());
        this.getCommand("helpop").setExecutor(new Helpop());
        this.getCommand("create-tag").setExecutor(new CreateTag());
        this.getCommand("grant-tag").setExecutor(new GrantTag());
        this.getCommand("ungrant-tag").setExecutor(new UnGrantTag());
        this.getCommand("delete-tag").setExecutor(new DeleteTag());
        this.getCommand("tags").setExecutor(new Tags());
        this.getCommand("minerals").setExecutor(new Minerals());
        this.getCommand("addminerals").setExecutor(new addMinerals());
        this.getCommand("coins").setExecutor(new Minerals());
        this.getCommand("daily-reward").setExecutor(new DailyReward());
        // Register Listener
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new JeezyInventories(), this);
        getServer().getPluginManager().registerEvents(new LeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new RespawnEvent(), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getServer().getPluginManager().registerEvents(new WeatherEvent(), this);
        // Creating Connection / Creating Table
        JeezySQL con = new JeezySQL();
        con.createTable();
        // Launching discord bot
       JeezyBot bot = new JeezyBot();
       bot.start();

       MineralsSQL mineralsSQL = new MineralsSQL();
       mineralsSQL.start();

        // Get rewards ready at server start
        RewardSQL rewardSQL = new RewardSQL();
        rewardSQL.tagData();

    }

    @Override
    public void onDisable() {
        System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" shutting down..."+Color.RESET);
    }


}
