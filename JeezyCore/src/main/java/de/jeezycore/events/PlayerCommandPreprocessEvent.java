package de.jeezycore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerCommandPreprocessEvent implements Listener {

    @EventHandler
    public void onCommandExecute(org.bukkit.event.player.PlayerCommandPreprocessEvent e) {
       if (e.getMessage().contains("/help")) {
           e.setCancelled(true);
           e.getPlayer().sendMessage(new String[] {
                   "\n",
                   "\n",
                   " §9§lMineral §f§lPractice",
                   "\n",
                   " §71v1s, 2v2s, PvPBots, Duels, Parties, Events",
                   "\n",
                   " §9§l♦ §fTo play,§9§l right click with your sword",
                   "\n",
                   " §9§l♦ §fTo duel someone,§9§l /duel [player]",
                   "\n",
                   " §9§l♦ §fTo edit your kit,§9§l right click with your book",
           });
       }
    }
}