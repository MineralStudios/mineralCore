package de.jeezycore.velocity.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import de.jeezycore.velocity.db.MaintenanceSQL;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

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

        if (MaintenanceSQL.maintenance) {
            builder.version(new ServerPing.Version(event.getConnection().getProtocolVersion().getProtocol() + 1, toml.getString("maintenance_display")));
            builder.description(motDMaintenance);
        } else {
            builder.maximumPlayers(0);
            builder.description(motD);
        }
        event.setPing(builder.build());
    }
}