package de.jeezycore.config;

import com.google.common.collect.Lists;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class JeezyConfig {
     private final File folder = new File("plugins/JeezyCore");
     public static File config = new File("plugins/JeezyCore/config.yml");
     public static File database = new File("plugins/JeezyCore/database.yml");
     public static File discord = new File("plugins/JeezyCore/discord.yml");
     public static YamlConfiguration config_defaults = YamlConfiguration.loadConfiguration(config);
     public static YamlConfiguration database_defaults = YamlConfiguration.loadConfiguration(database);
     public static YamlConfiguration discord_defaults = YamlConfiguration.loadConfiguration(discord);



    private void create_config_defaults() {
        try {
            config.createNewFile();
            config_defaults.addDefault("entry-spawn-point", "");
            String path_spawn_settings_1 = "spawn-settings.spawn-at-spawnpoint-on-join";
            String path_spawn_settings_2 = "spawn-settings.spawn-command";
            String path_spawn_settings_3 = "spawn-settings.respawn-at-spawn";
            String path_spawn_settings_4 = "spawn-settings.respawn-after-death-at-spawn";
            config_defaults.addDefault(path_spawn_settings_1, false);
            config_defaults.addDefault(path_spawn_settings_2, false);
            config_defaults.addDefault(path_spawn_settings_3, false);
            config_defaults.addDefault(path_spawn_settings_4, false);


            String path_settings_1 = "settings.disable_raining";
            String path_settings_2 = "settings.disable-death-msg";
            config_defaults.addDefault(path_settings_1, false);
            config_defaults.addDefault(path_settings_2, false);

            String path_chat_1 = "chat.chat_format";
            String path_chat_2 = "chat.muted";
            String path_chat_3 = "chat.ignored_roles_on_chat-mute";

            config_defaults.addDefault(path_chat_1, "&2[player]&f: [msg]");
            config_defaults.addDefault(path_chat_2, false);

            List<Object> list = new ArrayList<>();
            list.add("Owner");
            config_defaults.addDefault(path_chat_3, list);

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

    public void create_folder() {
        if (!folder.exists()) {
            folder.mkdir();
    }
        create_config_defaults();
        create_database_defaults();
        create_discord_defaults();
    }
}
