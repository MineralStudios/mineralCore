package de.jeezycore.discord;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.discord.activity.BotStatus;
import de.jeezycore.discord.events.MessageCreate;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.File;

public class JeezyBot {

    public static DiscordApi api;

    public void start() {

        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-bot-secret");
        String token = (String) discord.get("token");

         api = new DiscordApiBuilder()
                .setToken(token)
                .setAllIntents()
                .login().join();

        BotStatus botStatus = new BotStatus();
        botStatus.set();

        // Register EventListener
        api.addListener(new MessageCreate());
    }
    }
