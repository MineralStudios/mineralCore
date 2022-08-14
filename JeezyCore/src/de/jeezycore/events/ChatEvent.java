package de.jeezycore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat1(AsyncPlayerChatEvent e){
        e.setFormat("§c[Owner]§f "+e.getPlayer().getDisplayName()+": "+e.getMessage());

    }

}
