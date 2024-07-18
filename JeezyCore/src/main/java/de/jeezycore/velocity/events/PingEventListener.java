package de.jeezycore.velocity.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import de.jeezycore.velocity.db.MaintenanceSQL;

public class PingEventListener {



    @Subscribe(order = PostOrder.FIRST)
    public void onProxyPing(final ProxyPingEvent event) {

        if (MaintenanceSQL.maintenance) {
            final ServerPing ping = event.getPing();
            final ServerPing.Builder builder = ping.asBuilder();

            builder.version(new ServerPing.Version(0, "Whitelisted"));

            event.setPing(builder.build());
        }
    }
}