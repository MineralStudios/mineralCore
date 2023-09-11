package de.jeezycore.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import net.kyori.adventure.text.Component;

public class LoginEvent {

    @Subscribe
    public void onPlayerLogin(com.velocitypowered.api.event.connection.LoginEvent event) {
        final Component component = Component.text("             §7(§2CLICK TO ACCEPT§7)                 ")
                .clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.RUN_COMMAND, "/friends accept " + event.getPlayer().getUsername()))
                .hoverEvent(Component.text("Click to accept the friend request."));

        event.getPlayer().sendMessage(Component.text("                                                                    "));
        event.getPlayer().sendMessage(Component.text(" §9§l" + event.getPlayer().getUsername() + " §7has sent you a §2friend §7request!"));
        event.getPlayer().sendMessage(Component.text("                                                                    "));
        event.getPlayer().sendMessage(component);
        event.getPlayer().sendMessage(Component.text("                                                                    "));
    }
}