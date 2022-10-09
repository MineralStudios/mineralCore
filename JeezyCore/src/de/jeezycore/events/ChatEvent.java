package de.jeezycore.events;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.JeezySQL;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat1(AsyncPlayerChatEvent e) {
        JeezySQL display = new JeezySQL();
        String sql = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ e.getPlayer().getUniqueId().toString() +"%'";
        display.displayChatRank(sql);
        System.out.println(display.rank);
        System.out.println(display.rankColor);
        System.out.println(e.getPlayer().getUniqueId());

            if (display.rank == null) return;


        try {
            File file = new File("/Users/jeffreyejeanye/Desktop/JeezyCore/JeezyCore/src/config.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            MemorySection mc = (MemorySection) config.get("chat");
            boolean chat_muted = mc.getBoolean("muted");
            List<String> ignored_roles = (List<String>) mc.getList("ignored_roles_on_chat-mute");



            System.out.println(ignored_roles);


             if (!chat_muted || ignored_roles.contains(display.rank)) {
                System.out.println("Chat is enabled");
            } else {
               System.out.println("Chat is disabled!");
                e.getPlayer().sendMessage("§4§lChat has been disabled.");
                e.setCancelled(true);
                return;
            }


        } catch (Exception f) {
            f.printStackTrace();
        }

            String show_color = ColorTranslator.colorTranslator.get(display.rankColor);
            System.out.println(show_color);
            e.setFormat("§7§l["+show_color+""+display.rank+"§7§l]§f "+e.getPlayer().getDisplayName()+": "+e.getMessage().replace("%", "%%"));


    }

}
