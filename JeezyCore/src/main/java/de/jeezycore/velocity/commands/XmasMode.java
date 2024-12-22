package de.jeezycore.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import de.jeezycore.velocity.db.XmasModeSQL;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;


public class XmasMode implements SimpleCommand {
    private final ProxyServer server;
    private final Logger logger;
    private final XmasModeSQL xmasModeSQL = new XmasModeSQL();

    public XmasMode(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();
        // Get the arguments after the command alias
        String[] args = invocation.arguments();
        String str = String.join(",", args);

        switch (str) {
            case "on":
                source.sendMessage(Component.text("[Xmas Mode] successfully turned on", NamedTextColor.DARK_PURPLE));
                xmasModeSQL.enableXmasMode();
                break;
            case "off":
                source.sendMessage(Component.text("[Xmas Mode] successfully turned off", NamedTextColor.DARK_PURPLE));
                xmasModeSQL.disableXmasMode();
                break;
            default:
                source.sendMessage(Component.text("[Xmas Mode] (on / off)", NamedTextColor.DARK_PURPLE));
                break;
        }
    }

    // This method allows you to control who can execute the command.
    // If the executor does not have the required permission,
    // the execution of the command and the control of its autocompletion
    // will be sent directly to the server on which the sender is located

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("xmasMode.*");
    }


    // With this method you can control the suggestions to send
    // to the CommandSource according to the arguments
    // it has already written or other requirements you need
    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of();
    }

    // Here you can offer argument suggestions in the same way as the previous method,
    // but asynchronously. It is recommended to use this method instead of the previous one
    // especially in cases where you make a more extensive logic to provide the suggestions
    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(List.of());
    }
}
