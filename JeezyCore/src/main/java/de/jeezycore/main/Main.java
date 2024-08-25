package de.jeezycore.main;

import de.jeezycore.commands.basic.gamemodes.Creative;
import de.jeezycore.commands.basic.gamemodes.Survival;
import de.jeezycore.commands.basic.msg.*;
import de.jeezycore.commands.basic.multiverse.World;
import de.jeezycore.commands.chatColors.ChatColors;
import de.jeezycore.commands.chatColors.CreateChatColor;
import de.jeezycore.commands.chatColors.GrantChatColor;
import de.jeezycore.commands.chatColors.UnGrantChatColors;
import de.jeezycore.commands.ranks.DeleteRank;
import de.jeezycore.commands.ranks.UnGrantRank;
import de.jeezycore.commands.staff.Logs;
import de.jeezycore.db.TabListSQL;
import de.jeezycore.db.hikari.HikariCP;
import de.jeezycore.disguise.manger.DisguiseManager;
import de.jeezycore.colors.Color;
import de.jeezycore.commands.basic.*;
import de.jeezycore.commands.chat.ChatDisabler;
import de.jeezycore.commands.disguise.DisguiseCommand;
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
import de.jeezycore.commands.spawn.SetSpawn;
import de.jeezycore.commands.spawn.Spawn;
import de.jeezycore.commands.staff.StaffRankDisable;
import de.jeezycore.commands.staff.StaffRankEnable;
import de.jeezycore.commands.tags.*;
import de.jeezycore.config.JeezyConfig;
import de.jeezycore.discord.JeezyBot;
import de.jeezycore.events.*;
import de.jeezycore.events.chat.ChatEvent;
import de.jeezycore.events.inventories.JeezyInventories;
import de.jeezycore.utils.HTTPUtility;
import de.jeezycore.utils.NameMC;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static de.jeezycore.utils.ArrayStorage.*;
import static de.jeezycore.utils.NameTag.scoreboard;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.GREEN_BOLD+" Successfully"+Color.CYAN_BOLD+" started JeezyCore coded by JeezyDevelopment!"+Color.RESET);
        World world = new World();
        world.loadWorldsOnStartup();
        JeezyConfig file_Jeezy_config = new JeezyConfig();
        file_Jeezy_config.create_folder();
        HTTPUtility httpUtility = new HTTPUtility(this);
        DisguiseManager disguiseManager = new DisguiseManager(this, httpUtility);
        // Register Commands
        this.getCommand("create-rank").setExecutor(new CreateRank());
        this.getCommand("delete-rank").setExecutor(new DeleteRank());
        this.getCommand("grant").setExecutor(new GrantRank());
        this.getCommand("ungrant").setExecutor(new UnGrantRank());
        this.getCommand("chat-disable").setExecutor(new ChatDisabler());
        this.getCommand("chat-enable").setExecutor(new ChatDisabler());
        this.getCommand("gm0").setExecutor(new Survival());
        this.getCommand("gm1").setExecutor(new Creative());
        this.getCommand("spawnSet").setExecutor(new SetSpawn());
        this.getCommand("spawn").setExecutor(new Spawn());
        this.getCommand("ping").setExecutor(new Ping());
        this.getCommand("msg").setExecutor(new Msg());
        this.getCommand("tpm").setExecutor(new ControlMsg());
        this.getCommand("r").setExecutor(new Reply());
        this.getCommand("ignore").setExecutor(new Ignore());
        this.getCommand("unignore").setExecutor(new UnIgnore());
        this.getCommand("pmSound").setExecutor(new PmSound());
        this.getCommand("gc").setExecutor(new GlobalChat());
        this.getCommand("day").setExecutor(new TimeChanger());
        this.getCommand("night").setExecutor(new TimeChanger());
        //this.getCommand("fly").setExecutor(new Fly());
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
        this.getCommand("logs").setExecutor(new Logs());
        this.getCommand("staffrank-enable").setExecutor(new StaffRankEnable());
        this.getCommand("staffrank-disable").setExecutor(new StaffRankDisable());
        this.getCommand("helpop").setExecutor(new Helpop());
        this.getCommand("create-tag").setExecutor(new CreateTag());
        this.getCommand("grant-tag").setExecutor(new GrantTag());
        this.getCommand("ungrant-tag").setExecutor(new UnGrantTag());
        this.getCommand("delete-tag").setExecutor(new DeleteTag());
        this.getCommand("tags").setExecutor(new Tags());
        this.getCommand("create-chatColor").setExecutor(new CreateChatColor());
        this.getCommand("chatColors").setExecutor(new ChatColors());
        this.getCommand("grant-chatColor").setExecutor(new GrantChatColor());
        this.getCommand("ungrant-chatColor").setExecutor(new UnGrantChatColors());
        //this.getCommand("minerals").setExecutor(new Minerals());
        //this.getCommand("addminerals").setExecutor(new addMinerals());
        //this.getCommand("removeminerals").setExecutor(new removeMinerals());
        //this.getCommand("coins").setExecutor(new Minerals());
        //this.getCommand("daily-reward").setExecutor(new DailyReward());
        //this.getCommand("store").setExecutor(new Store());
        this.getCommand("disguise").setExecutor(new DisguiseCommand(disguiseManager));
        this.getCommand("brodcast").setExecutor(new Brodcast());
        //this.getCommand("languages").setExecutor(new Languages());
        //this.getCommand("lang").setExecutor(new Lang());
        //this.getCommand("friends").setExecutor(new FriendsCommands());
        this.getCommand("world").setExecutor(new World());
        this.getCommand("sync").setExecutor(new Sync());
        this.getCommand("playtime").setExecutor(new PlayTime());
        this.getCommand("build").setExecutor(new Build());

        // Register Listener
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new JeezyInventories(), this);
        getServer().getPluginManager().registerEvents(new LeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new RespawnEvent(), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getServer().getPluginManager().registerEvents(new WeatherEvent(), this);
        getServer().getPluginManager().registerEvents(new QuitEvent(disguiseManager), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportEvent(), this);
        getServer().getPluginManager().registerEvents(new BreakBlocksEvent(), this);
        // Setups Databases + Tables
        HikariCP hikariCP = new HikariCP();
        hikariCP.start();
        // Launching discord bot
        /*
       JeezyBot bot = new JeezyBot();
       bot.start();
         */

       // Schedule Tips
        /*
        Tips tips = new Tips();
        tips.scheduleTips();
         */

       // Putting in the Languages
        /*
        LanguagesAPI languagesAPI = new LanguagesAPI();
        languagesAPI.setTranslateUrl();
        languagesAPI.setLanguages();
         */

      // MineralsSQL mineralsSQL = new MineralsSQL();
      // mineralsSQL.start();

        // Get rewards ready at server start
       // RewardSQL rewardSQL = new RewardSQL();
       // rewardSQL.tagData();


      TabListSQL tabListSQL = new TabListSQL();
      tabListSQL.getTabListRanks();
      tabListSQL.getTabListPerms();
      tabListSQL.getUsers();

      for (int i = 0; i < rankTabListPerms.size(); i++) {
          if (!rankTabListPerms.get(i).contains("vuzle.tab")) {
              rankTabListPerms.remove(i);
          }
      }

        for (String i : rankTabListSorting.keySet()) {
            scoreboard.registerNewTeam(i).setPrefix(rankTabListSorting.get(i).replace("&", "§"));
            System.out.println(i + " "+rankTabListSorting.get(i).replace("&", "§"));
        }
        scoreboard.registerNewTeam("QNameMC").setPrefix("§l§9✔§2 ");
        scoreboard.registerNewTeam("ZDefault").setPrefix("§2");

        /*
        scoreboard.registerNewTeam("Founder").setPrefix("§9");
        scoreboard.registerNewTeam("Manager").setPrefix("§4");
        scoreboard.registerNewTeam("Staff").setPrefix("§b");
        scoreboard.registerNewTeam("Partner").setPrefix("§f");
        scoreboard.registerNewTeam("Famous").setPrefix("§6");
        scoreboard.registerNewTeam("Creator").setPrefix("§5");
        scoreboard.registerNewTeam("MiniCreator").setPrefix("§e");
        scoreboard.registerNewTeam("Mineral").setPrefix("§3");
        scoreboard.registerNewTeam("Obsidian").setPrefix("§0");
        scoreboard.registerNewTeam("Silver").setPrefix("§7");
        scoreboard.registerNewTeam("QNameMC").setPrefix("§l§9✔§2 ");
        scoreboard.registerNewTeam("ZDefault").setPrefix("§2");


         */

        /*
      NameMC nameMC = new NameMC();
      nameMC.getLikes();

         */

        allBuilders.add(UUID.fromString("3f7d7552-6faa-4fd1-a6a8-42694aa04889"));
        allBuilders.add(UUID.fromString("e150b93f-a86b-488c-8414-4065f2717c5c"));

    }

    @Override
    public void onDisable() {
        System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" shutting down..."+Color.RESET);
    }
}