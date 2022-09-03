package de.jeezycore.events;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.JeezySQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class ChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat1(AsyncPlayerChatEvent e) {
        JeezySQL display = new JeezySQL();
        String sql = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ e.getPlayer().getUniqueId().toString().replace("-", "") +"%'";
        display.displayChatRank(sql);
        System.out.println(display.rank);
        System.out.println(display.rankColor);
        System.out.println(e.getPlayer().getUniqueId());

            if (display.rank == null) return;

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("C:\\Users\\Lassd\\IdeaProjects\\JeezyDevelopment\\JeezyCore\\src\\config.json"));
            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            JSONObject jsonObject = (JSONObject) obj;
            // A JSON array. JSONObject supports java.util.List interface.
            Boolean chat_muted = (Boolean) jsonObject.get("chat_muted");

            if (!chat_muted) {
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
