package de.jeezycore.events.chat;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.RanksSQL;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class IgnoreChat implements Listener {
    RanksSQL display = new RanksSQL();
    @EventHandler
    public void PlayerIgnoreChat(AsyncPlayerChatEvent e) {
        try {
            MemorySection mc = (MemorySection) JeezyConfig.config_defaults.get("chat");
            boolean chat_muted = mc.getBoolean("muted");
            List<String> ignored_roles = (List<String>) mc.getList("ignored_roles_on_chat-mute");

            if (!chat_muted || ignored_roles.contains(display.rank)) {
                System.out.println("Chat is enabled");
            } else {
                System.out.println("Chat is disabled!");
                e.getPlayer().sendMessage("§4§lChat has been disabled.");
                e.setCancelled(true);
            }
        } catch (Exception f) {
            f.printStackTrace();
        }
    }

}
