package de.jeezycore.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.jeezycore.velocity.db.WhitelistedSQL;
import de.jeezycore.velocity.utils.ArrayStorage;
import de.jeezycore.velocity.utils.UUIDCheckerVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import static de.jeezycore.velocity.config.JeezyConfig.toml;

public final class Whitelist implements SimpleCommand {

    private final ProxyServer server;
    private final Logger logger;
    private final WhitelistedSQL whitelistedSQL = new WhitelistedSQL();

    public Whitelist(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Override
    public void execute(final Invocation invocation) {
        UUIDCheckerVelocity uuidCheckerVelocity = new UUIDCheckerVelocity(server, logger);

        CommandSource source = invocation.source();
        // Get the arguments after the command alias
        String[] args = invocation.arguments();
        String str = String.join(",", args);


        if (args.length > 0) {
            switch (args[0]) {
                case "on":
                    source.sendMessage(Component.text("[Whitelist] successfully turned on", NamedTextColor.DARK_AQUA));
                    whitelistedSQL.enableWhitelisted();
                    whitelistedSQL.getWhitelistedPlayers();
                    List<Player> allPlayers = (List<Player>) server.getAllPlayers();
                    for (Player ps : allPlayers) {
                        if (!ArrayStorage.whitelistedPlayerUUID.contains(ps.getUniqueId().toString())) {
                            ps.disconnect(Component.text(toml.getString("whitelisted_kick_message")));
                        }
                    }
                    ArrayStorage.whitelistedPlayerUUID.clear();
                    break;
                case "off":
                    source.sendMessage(Component.text("[Whitelist] successfully turned off", NamedTextColor.DARK_AQUA));
                    whitelistedSQL.disableWhitelisted();
                    break;
                case "add":
                    String inputAdd = "INSERT INTO whitelisted " +
                            "(playerUUID, whitelisted) " +
                            "VALUES " +
                            "(?, ?)";
                    uuidCheckerVelocity.check(args[1]);
                    whitelistedSQL.add(inputAdd, UUIDCheckerVelocity.uuid, true);
                    source.sendMessage(Component.text("You successfully added "+UUIDCheckerVelocity.uuidName+" to the whitelist!" , NamedTextColor.BLUE));
                    break;
                case "remove":
                    String inputRemove = "DELETE FROM whitelisted " +
                            " WHERE playerUUID=? ";
                    uuidCheckerVelocity.check(args[1]);
                    whitelistedSQL.remove(inputRemove, UUIDCheckerVelocity.uuid);
                    if (server.getPlayer(UUIDCheckerVelocity.uuidName).isPresent()) {
                        server.getPlayer(UUIDCheckerVelocity.uuidName).get().disconnect(Component.text(toml.getString("whitelisted_access_removed_if_active")));
                     }
                    source.sendMessage(Component.text("You successfully removed "+UUIDCheckerVelocity.uuidName+" from the whitelist!", NamedTextColor.DARK_RED));
                    break;
                default:
                    source.sendMessage(Component.text("[Whitelist] (on / off)", NamedTextColor.DARK_AQUA));
                    break;
            }
        } else {
            source.sendMessage(Component.text("[Whitelist] (on / off / add / remove)", NamedTextColor.DARK_AQUA));
        }

    }

    // This method allows you to control who can execute the command.
    // If the executor does not have the required permission,
    // the execution of the command and the control of its autocompletion
    // will be sent directly to the server on which the sender is located

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("whitelist.*");
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
