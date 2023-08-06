package de.jeezycore.utils;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.StaffSQL;
import de.jeezycore.main.Main;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class BungeeChannelApi {

    StaffSQL staffSQL = new StaffSQL();

    RanksSQL display = new RanksSQL();

    MemorySection hubConfig = (MemorySection) JeezyConfig.config_defaults.get("hub");


    io.github.leonardosnt.bungeechannelapi.BungeeChannelApi api = io.github.leonardosnt.bungeechannelapi.BungeeChannelApi.of(Main.getPlugin(Main.class));

    public void sendStaffMessages(AsyncPlayerChatEvent e) {
        api.getServer()
                .whenComplete((server, errorServer) -> {
        api.getPlayerList("ALL")
                .whenComplete((result, error) -> {

                    staffSQL.getStaff();
                    display.getPlayerInformation(e.getPlayer());
                    String sql = "SELECT * FROM ranks WHERE rankName = '"+display.rankNameInformation+"'";
                    display.displayChatRank(sql);

                    for (int i = 0; i < StaffSQL.staff.size(); i++) {
                        String new_message = e.getMessage().replace("@", "").trim();
                        try {
                            if (!result.contains(StaffSQL.staff.get(i))) {
                                continue;
                            }
                            api.sendMessage(StaffSQL.staff.get(i), "§7[§9Staff§7-§fChat§7] §7(§9"+server+"§7) "+display.rankColor.replace("&", "§")+e.getPlayer().getDisplayName()+"§f: "+new_message);
                        } catch (Exception f) {
                        }
                    }
                 });
                });
    }


    public void sendToHub(Player p) {
        api.connect(p, hubConfig.getString("serverName"));
    }
}