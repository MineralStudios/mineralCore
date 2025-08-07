package de.jeezycore.velocity.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import de.jeezycore.velocity.db.MaintenanceSQL;
import de.jeezycore.velocity.db.WhitelistedSQL;
import de.jeezycore.velocity.db.XmasModeSQL;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.IOException;
import java.nio.file.Paths;

import static de.jeezycore.velocity.config.JeezyConfig.toml;

public class PingEventListener {


    @Subscribe(order = PostOrder.FIRST)
    public void onProxyPing(final ProxyPingEvent event) {

         ServerPing ping = event.getPing();
         ServerPing.Builder builder = ping.asBuilder();


        Component motDMaintenance = MiniMessage.miniMessage().deserialize(
                toml.getString("motd_maintenance")
        );

        Component motD = MiniMessage.miniMessage().deserialize(
                toml.getString("motd")
        );

        Component motDWhitelist = MiniMessage.miniMessage().deserialize(
                toml.getString("whitelisted_motd")
        );

        if (XmasModeSQL.xmasMode) {
            if (toml.getBoolean("xmas_logo_change")) {
                try {
                    builder.favicon(Favicon.create((Paths.get("plugins/jeezycore/server-logo-xmas.png"))));
                } catch (IOException e) {
                    System.out.println("Can't find xmas logo");
                }
            }
        }

        if (MaintenanceSQL.maintenance) {
            builder.version(new ServerPing.Version(event.getConnection().getProtocolVersion().getProtocol() + 1, toml.getString("maintenance_display")));
            builder.description(motDMaintenance);
            if (toml.getBoolean("maintenance_logo_change")) {
                try {
                    builder.favicon(Favicon.create((Paths.get("plugins/jeezycore/server-logo-maintenance.png"))));
                } catch (IOException e) {
                    System.out.println("Can't find maintenance logo");
                }
            }
        } else {
            if (WhitelistedSQL.whitelisted) {
                builder.version(new ServerPing.Version(event.getConnection().getProtocolVersion().getProtocol() + 1, "The End"));
                builder.description(motDWhitelist);
            } else {
                builder.maximumPlayers(0);
                builder.description(motD);
            }
        }
        event.setPing(builder.build());
    }
}