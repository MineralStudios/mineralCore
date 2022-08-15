package de.jeezycore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat1(AsyncPlayerChatEvent e){
        e.setFormat("§8§l[§cOwner§8§l]§f "+e.getPlayer().getDisplayName()+": "+e.getMessage());

    }

}
