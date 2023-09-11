package de.jeezycore.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import de.jeezycore.velocity.events.CommandExecuteEvent;
import de.jeezycore.velocity.events.LoginEvent;
import java.util.logging.Logger;

@Plugin(id = "mineralcore", name = "MineralCore", version = "1.0",
        url = "https://mineral.gg", description = "A core that manages the server.", authors = {"MineralStudios"})
public class Main {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public Main(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        logger.info("Successfully launched MineralCore!");
    }
    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new LoginEvent());
        server.getEventManager().register(this, new CommandExecuteEvent());
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("bungeecord:main"));
    }
}