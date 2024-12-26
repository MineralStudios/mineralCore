package de.jeezycore.discord;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.discord.db.HikariCP;
import de.jeezycore.discord.commands.MineralSlashCommands;
import de.jeezycore.discord.events.InteractionEvent;
import de.jeezycore.discord.events.MessageComponentCreate;
import de.jeezycore.discord.events.MessageCreate;
import org.bukkit.configuration.MemorySection;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class JeezyBot {

    public static DiscordApi api;
    private final HikariCP hikariCP = new HikariCP();
    private final MineralSlashCommands mineralSlashCommands = new MineralSlashCommands();
    private final MemorySection discord_support = (MemorySection) JeezyConfig.discord_defaults.get("discord-support");
    private final MemorySection secret = (MemorySection) JeezyConfig.discord_defaults.get("discord-bot-secret");

    public void start() {
        boolean activated = (boolean) discord_support.get("activated");
        String token = (String) secret.get("token");

        if (!activated) {
            return;
        }

         api = new DiscordApiBuilder()
                .setToken(token)
                .setAllIntents()
                .login().join();

        //BotStatus botStatus = new BotStatus();
        //botStatus.set();

        // Register EventListener
        api.addListener(new MessageCreate());
        api.addListener(new InteractionEvent());
        api.addListener(new MessageComponentCreate());

        // Register Commands
        mineralSlashCommands.initializeCommands(api);

        // Initialize SQL Connection
        System.out.println("HERRERE MINERALBOT");
        hikariCP.start();
    }
}