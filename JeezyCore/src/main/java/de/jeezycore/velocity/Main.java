package de.jeezycore.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.jeezycore.velocity.commands.Maintenance;
import de.jeezycore.velocity.config.JeezyConfig;
import de.jeezycore.velocity.db.MaintenanceSQL;
import de.jeezycore.velocity.db.hikari.HikariCP;
import de.jeezycore.velocity.events.CommandExecuteListener;
import de.jeezycore.velocity.events.PingEventListener;

import java.io.IOException;
import java.util.logging.Logger;

@Plugin(
        id = "jeezycore",
        name = "JeezyCore",
        version = "1.0",
        url = "https://mineral.gg",
        description = "A powerful core.",
        authors = {"MineralStudio", "Jeffrey"}
)
public class Main {

    private final ProxyServer server;
    private final Logger logger;
    JeezyConfig jeezyConfig = new JeezyConfig();
    HikariCP hikariCP = new HikariCP();
    MaintenanceSQL maintenanceSQL = new MaintenanceSQL();

    @Inject
    public Main(ProxyServer server, Logger logger) throws IOException {
        this.server = server;
        this.logger = logger;

        logger.info("Successfully launched MineralVelocity.");
    }


    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        jeezyConfig.createData();
        hikariCP.start();
        maintenanceSQL.getMaintenanceData();
        this.registerEvents();
        this.registerCommands();
    }



    public void registerEvents () {
        EventManager eventManager = server.getEventManager();
        eventManager.register(this, new PingEventListener());
        eventManager.register(this, new CommandExecuteListener());
    }

    public void registerCommands () {
        CommandManager commandManager = server.getCommandManager();
        // Here you can add meta for the command, as aliases and the plugin to which it belongs (RECOMMENDED)
        CommandMeta commandMeta = commandManager.metaBuilder("maintenance")
                // This will create a new alias for the command "/test"
                // with the same arguments and functionality
               // .aliases("otherAlias", "anotherAlias")
                .plugin(this)
                .build();

        // You can replace this with "new EchoCommand()" or "new TestCommand()"
        // SimpleCommand simpleCommand = new TestCommand();
        // RawCommand rawCommand = new EchoCommand();
        // The registration is done in the same way, since all 3 interfaces implement "Command"
        SimpleCommand simpleCommand = new Maintenance(server, logger);

        // Finally, you can register the command
        commandManager.register(commandMeta, simpleCommand);
    }
}
