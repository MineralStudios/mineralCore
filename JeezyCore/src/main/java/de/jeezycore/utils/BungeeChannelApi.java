package de.jeezycore.utils;


import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.StaffSQL;
import de.jeezycore.events.chat.StaffChat;
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

    public void sendPlayerToServer(Player sender, String playerName, String serverName) {
        api.getPlayerList("ALL")
                .whenComplete((allPlayers, error) -> {
        api.getServers()
                .whenComplete((servers, errorServer) -> {

                    if (allPlayers.contains(playerName)) {
                        if (servers.contains(serverName)) {
                            api.connectOther(playerName, serverName);
                        } else {
                            sender.sendMessage("§7The server: §9"+serverName+" §7doesn't §cexist!");
                        }
                    } else {
                        sender.sendMessage("§c"+playerName+" §7isn't online!");
                    }
                });
                });
    }

    public void kickPlayer(String playerName, String message) {
        try {
            api.kickPlayer(playerName, message);
        } catch (Exception e) {
        }
    }

    public void broadcastMessage(String input) {
        api.getPlayerList("ALL")
                .whenComplete((result, error) -> {
                    for (String playerName : result) {
                        api.sendMessage(playerName, input);
                    }
                });
    }

    public void reportPlayer(Player sender, String playerName, String input) {
        StaffChat staffChat = new StaffChat();
        api.getServer()
                .whenComplete((server, errorServer) -> {
                    api.getPlayerList("ALL")
                            .whenComplete((result, error) -> {

                    if (sender.getDisplayName().equalsIgnoreCase(playerName)) {
                        sender.sendMessage("§7You can't §creport §7yourself!");
                        return;
                    }

                    if (result.contains(playerName)) {
                        String report_msg = "§7§l[§4Report§7§l] "+ "§7(§9"+server+"§7)"+ " ("+"§a"+sender.getDisplayName()+"§6§l -> §7"+"§c"+playerName+"§7§l) §c§lReason§f§l: §7"+input;

                        staffChat.reportChat(sender, report_msg, api, result);

                        sender.sendMessage("§7You §a§lsuccessfully §7reported "+"§9"+playerName+"§7.\n" +
                                "§7§l► §9Thanks §7for §9§lcaring §7about our §9§lcommunity§7. §7§l[§4§l❤§7§l]");

                    } else {
                        sender.sendMessage("§7The §9player §7isn't currently §conline§7.");
                    }
                            });
                });
    }
}