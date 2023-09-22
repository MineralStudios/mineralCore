package de.jeezycore.config;


import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.*;

public class JeezyConfig {
     private final File folder = new File("plugins/JeezyCore");

     public static File config = new File("plugins/JeezyCore/config.yml");
     public static File database = new File("plugins/JeezyCore/database.yml");
     public static File tips = new File("plugins/JeezyCore/tips.yml");
     public static File redis = new File("plugins/JeezyCore/redis.yml");

     public static File discord = new File("plugins/JeezyCore/discord.yml");
     public static File multiverse = new File("plugins/JeezyCore/multiverse.yml");

     public static YamlConfiguration config_defaults = YamlConfiguration.loadConfiguration(config);
     public static YamlConfiguration database_defaults = YamlConfiguration.loadConfiguration(database);
     public static YamlConfiguration tips_defaults = YamlConfiguration.loadConfiguration(tips);
     public static YamlConfiguration redis_defaults = YamlConfiguration.loadConfiguration(redis);
     public static YamlConfiguration discord_defaults = YamlConfiguration.loadConfiguration(discord);
     public static YamlConfiguration multiverse_defaults = YamlConfiguration.loadConfiguration(multiverse);



    private void create_config_defaults() {
        try {
            config.createNewFile();

            String hubEnabled = "hub.enabled";
            String hubServerName = "hub.serverName";

            config_defaults.addDefault(hubEnabled, false);
            config_defaults.addDefault(hubServerName, "hub");


            String x = "entry-spawn-point.x";
            String y = "entry-spawn-point.y";
            String z = "entry-spawn-point.z";
            String pitch = "entry-spawn-point.pitch";
            String yaw = "entry-spawn-point.yaw";
            String world = "entry-spawn-point.world";

            config_defaults.addDefault(x, "");
            config_defaults.addDefault(y, "");
            config_defaults.addDefault(z, "");
            config_defaults.addDefault(pitch, "");
            config_defaults.addDefault(yaw, "");
            config_defaults.addDefault(world, "");

            String path_spawn_settings_1 = "spawn-settings.defaultJoinMessage";
            String path_spawn_settings_2 = "spawn-settings.coreJoinMessage";
            String path_spawn_settings_3 = "spawn-settings.spawn-at-spawnpoint-on-join";
            String path_spawn_settings_4 = "spawn-settings.spawn-command";
            String path_spawn_settings_5 = "spawn-settings.respawn-at-spawn";
            String path_spawn_settings_6 = "spawn-settings.respawn-after-death-at-spawn";
            config_defaults.addDefault(path_spawn_settings_1, false);
            config_defaults.addDefault(path_spawn_settings_2, false);
            config_defaults.addDefault(path_spawn_settings_3, false);
            config_defaults.addDefault(path_spawn_settings_4, false);
            config_defaults.addDefault(path_spawn_settings_5, false);
            config_defaults.addDefault(path_spawn_settings_6, false);


            String path_settings_1 = "settings.disable_raining";
            String path_settings_2 = "settings.disable-death-msg";
            config_defaults.addDefault(path_settings_1, false);
            config_defaults.addDefault(path_settings_2, false);

            String path_tags_1 = "tags.categories";
            List<Object> tag_categories_list = new ArrayList<>();
            config_defaults.addDefault(path_tags_1, tag_categories_list);

            String path_chat_1 = "chat.chat_format";
            String path_chat_2 = "chat.muted";
            String path_chat_3 = "chat.ignored_roles_on_chat-mute";

            config_defaults.addDefault(path_chat_1, "[rank] &2[player][tag]&f: [msg]");
            config_defaults.addDefault(path_chat_2, false);

            List<Object> list = new ArrayList<>();
            list.add("Owner");
            config_defaults.addDefault(path_chat_3, list);

            String path_friends_1 = "friends.notifications";
            config_defaults.addDefault(path_friends_1, true);

            config_defaults.options().copyDefaults(true);
            config_defaults.save(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create_database_defaults() {
        try {
            database.createNewFile();
            String path_database_1 = "MYSQL.ip";
            String path_database_2 = "MYSQL.mysql-port";
            String path_database_3 = "MYSQL.user";
            String path_database_4 = "MYSQL.password";
            String path_database_5 = "MYSQL.database";

            database_defaults.addDefault(path_database_1, "localhost");
            database_defaults.addDefault(path_database_2, 3306);
            database_defaults.addDefault(path_database_3, "");
            database_defaults.addDefault(path_database_4, "");
            database_defaults.addDefault(path_database_5, "");

            database_defaults.options().copyDefaults(true);
            database_defaults.save(database);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create_redis_defaults() {
        try {
            redis.createNewFile();
            String path_redis_1 = "REDIS.ip";
            String path_redis_2 = "REDIS.port";
            String path_redis_3 = "REDIS.user";
            String path_redis_4 = "REDIS.password";
            redis_defaults.addDefault(path_redis_1, "localhost");
            redis_defaults.addDefault(path_redis_2, 6379);
            redis_defaults.addDefault(path_redis_3, "default");
            redis_defaults.addDefault(path_redis_4, "");

            redis_defaults.options().copyDefaults(true);
            redis_defaults.save(redis);
        } catch (Exception e) {

        }

    }

    public void create_tips_defaults() {
        try {
            tips.createNewFile();
            String path_tips_status = "TIP.status";
            String path_tips_delayAfterStartup = "TIP.delay";
            String path_tips_delay = "TIP.period";
            String path_tips_1 = "TIP.one";
            String path_tips_2 = "TIP.two";
            String path_tips_3 = "TIP.three";
            String path_tips_4 = "TIP.four";
            String path_tips_5 = "TIP.five";
            List<Object> list_1 = new ArrayList<>();
            List<Object> list_2 = new ArrayList<>();
            List<Object> list_3 = new ArrayList<>();
            List<Object> list_4 = new ArrayList<>();
            List<Object> list_5 = new ArrayList<>();
            tips_defaults.addDefault(path_tips_status, false);
            tips_defaults.addDefault(path_tips_delayAfterStartup, 300000);
            tips_defaults.addDefault(path_tips_delay, 300000);
            tips_defaults.addDefault(path_tips_1, list_1);
            tips_defaults.addDefault(path_tips_2, list_2);
            tips_defaults.addDefault(path_tips_3, list_3);
            tips_defaults.addDefault(path_tips_4, list_4);
            tips_defaults.addDefault(path_tips_5, list_5);

            tips_defaults.options().copyDefaults(true);
            tips_defaults.save(tips);


        } catch (Exception e) {

        }

    }

    public void create_discord_defaults() {
        try {
            discord.createNewFile();
            String path_discord_1 = "discord-bot-secret.token";
            String path_discord_2 = "discord-support.activated";
            String path_discord_3 = "discord-realtime-chat.discordToMc";
            String path_discord_4 = "discord-realtime-chat.mcToDiscord";
            String path_discord_5 = "discord-granting-notifications.onGranting";
            String path_discord_6 = "discord-granting-notifications.onUnGranting";
            String path_discord_7 = "discord-muting-notifications.onMute";
            String path_discord_8 = "discord-muting-notifications.onTempMute";
            String path_discord_9 = "discord-muting-notifications.onUnMute";
            String path_discord_10 = "discord-banning-notifications.onBan";
            String path_discord_11 = "discord-banning-notifications.onTempBan";
            String path_discord_12 = "discord-banning-notifications.onUnBan";
            String path_discord_13 = "discord-text-channels-realtime-chat.discordToMc";
            String path_discord_14 = "discord-text-channels-realtime-chat.mcToDiscord";
            String path_discord_15 = "discord-text-channels-granting.onGranting";
            String path_discord_16 = "discord-text-channels-granting.onUnGranting";
            String path_discord_17 = "discord-text-channels-muting.onMute";
            String path_discord_18 = "discord-text-channels-muting.onTempMute";
            String path_discord_19 = "discord-text-channels-muting.onUnMute";
            String path_discord_20 = "discord-text-channels-banning.onBan";
            String path_discord_21 = "discord-text-channels-banning.onTempBan";
            String path_discord_22 = "discord-text-channels-banning.onUnBan";

            discord_defaults.addDefault(path_discord_1, "");
            discord_defaults.addDefault(path_discord_2, true);
            discord_defaults.addDefault(path_discord_3, true);
            discord_defaults.addDefault(path_discord_4, true);
            discord_defaults.addDefault(path_discord_5, true);
            discord_defaults.addDefault(path_discord_6, true);
            discord_defaults.addDefault(path_discord_7, true);
            discord_defaults.addDefault(path_discord_8, true);
            discord_defaults.addDefault(path_discord_9, true);
            discord_defaults.addDefault(path_discord_10, true);
            discord_defaults.addDefault(path_discord_11, true);
            discord_defaults.addDefault(path_discord_12, true);
            discord_defaults.addDefault(path_discord_13, "");
            discord_defaults.addDefault(path_discord_14, "");
            discord_defaults.addDefault(path_discord_15, "");
            discord_defaults.addDefault(path_discord_16, "");
            discord_defaults.addDefault(path_discord_17, "");
            discord_defaults.addDefault(path_discord_18, "");
            discord_defaults.addDefault(path_discord_19, "");
            discord_defaults.addDefault(path_discord_20, "");
            discord_defaults.addDefault(path_discord_21, "");
            discord_defaults.addDefault(path_discord_22, "");
            discord_defaults.options().copyDefaults(true);
            discord_defaults.save(discord);
        } catch (Exception e) {

        }
    }

    public void multiverse_defaults() {
        try {
            multiverse.createNewFile();
            String path_multiverse_1 = "Multiverse.worlds";

            List<Object> worlds = new ArrayList<>();

            multiverse_defaults.addDefault(path_multiverse_1, worlds);

            multiverse_defaults.options().copyDefaults(true);
            multiverse_defaults.save(multiverse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void create_folder() {
        if (!folder.exists()) {
            folder.mkdir();
    }
        create_config_defaults();
        create_database_defaults();
        create_redis_defaults();
        create_tips_defaults();
        create_discord_defaults();
        multiverse_defaults();
    }
}