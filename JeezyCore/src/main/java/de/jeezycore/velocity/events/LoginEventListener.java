package de.jeezycore.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import de.jeezycore.velocity.db.MaintenanceSQL;
import de.jeezycore.velocity.db.WhitelistedSQL;
import net.kyori.adventure.text.Component;
import java.util.logging.Logger;
import static de.jeezycore.velocity.config.JeezyConfig.toml;
import static de.jeezycore.velocity.db.WhitelistedSQL.maxPlayerCount;

public class LoginEventListener {

    private final ProxyServer server;
    private final Logger logger;
    private final WhitelistedSQL whitelistedSQL = new WhitelistedSQL();


    public LoginEventListener(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onPlayerJoin(ServerPreConnectEvent event) {

        if (maxPlayerCount != 0 && server.getPlayerCount() > maxPlayerCount) {
            event.setResult(ServerPreConnectEvent.ServerResult.denied());
            event.getPlayer().disconnect(Component.text(toml.getString("max_players_kick_message")));
            return;
        }

        if (MaintenanceSQL.maintenance || WhitelistedSQL.whitelisted) {
            whitelistedSQL.isPlayerWhitelisted(event.getPlayer().getUniqueId().toString());

            if (MaintenanceSQL.maintenance && !event.getPlayer().hasPermission("maintenance.bypass")) {
                event.setResult(ServerPreConnectEvent.ServerResult.denied());
                event.getPlayer().disconnect(Component.text(toml.getString("onMaintenance_message")));
            }

            if (WhitelistedSQL.whitelisted && !WhitelistedSQL.isWhitelisted) {
                event.setResult(ServerPreConnectEvent.ServerResult.denied());
                event.getPlayer().disconnect(Component.text(toml.getString("onWhitelisted_message")));
            }
        }
    }
}