package de.jeezycore.utils;


import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.MsgSQL;
import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.SettingsSQL;
import de.jeezycore.db.StaffSQL;
import de.jeezycore.events.chat.StaffChat;
import de.jeezycore.main.Main;
import org.bukkit.Sound;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

import static de.jeezycore.utils.ArrayStorage.msg_ignore_array;

public class BungeeChannelApi {

    StaffSQL staffSQL = new StaffSQL();

    RanksSQL display = new RanksSQL();

    SettingsSQL settingsSQL = new SettingsSQL();

    MsgSQL msgSQL = new MsgSQL();

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

    public void BungeeReportPlayer(Player sender, String playerName, String input) {
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

    public void bungeeMsg(Player sender, String playerName, String input) {
        api.getPlayerList("ALL")
                .whenComplete((result, error) -> {
                    if (sender.getDisplayName().equalsIgnoreCase(playerName)) {
                        sender.sendMessage("§7You §ccan't §7message yourself!");
                        return;
                    }
                    if (result.contains(playerName)) {
                        api.getUUID(playerName)
                                .whenComplete((uuid, slow) -> {

                                    String getUUID = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");

                                    settingsSQL.getSettingsData(UUID.fromString(getUUID));

                                    if (!settingsSQL.settingsMsg && settingsSQL.playerUUID != null) {
                                        sender.sendMessage("§9"+playerName+" §7has turned off his §9private §7messages.");
                                        return;
                                    }
                                    if (msg_ignore_array.contains(sender.getUniqueId().toString())) {
                                        sender.sendMessage("§9"+playerName+" §7has ignored you.");
                                        msg_ignore_array.clear();
                                        return;
                                    }

                                    display.getColorsForMessages(UUID.fromString(getUUID));
                                    String sql = "SELECT * FROM ranks WHERE rankName = '"+display.privateMessageColors+"'";
                                    display.displayChatRank(sql);
                                    sender.sendMessage("§9To§7 ("+display.rankColor.replace("&", "§")+playerName+"§7)"+"§7 "+input);

                                    display.getColorsForMessages(sender.getUniqueId());
                                    sql = "SELECT * FROM ranks WHERE rankName = '"+display.privateMessageColors+"'";
                                    display.displayChatRank(sql);

                                    api.sendMessage(playerName, "§9From§7 ("+display.rankColor.replace("&", "§")+sender.getPlayer().getDisplayName()+"§7)"+"§7 "+input);
                                    msgSQL.setup(UUID.fromString(getUUID), playerName, sender.getDisplayName(), sender.getUniqueId());
                                    this.playPrivateMessageSound(playerName);
                                });
                    } else {
                        sender.sendMessage("§9"+playerName+" §7isn't §conline§7.");
                    }
                });
    }

    public void BungeeReply(Player sender, String input) {
        api.getPlayerList("ALL")
                .whenComplete((result, error) -> {
                    msgSQL.getReplyData(sender.getUniqueId());
                    api.getUUID(msgSQL.replyToName)
                            .whenComplete((uuid, slow) -> {

                   String getUUID = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");

                    if (!result.contains(msgSQL.replyToName) || msgSQL.playerUUID == null) {
                        sender.sendMessage("§cThere is nobody to reply to.");
                        return;
                    }

                    settingsSQL.getSettingsData(UUID.fromString(getUUID));

                    if (!settingsSQL.settingsMsg && settingsSQL.playerUUID != null) {
                        sender.sendMessage("§9"+msgSQL.replyToName +" §7has turned off his §9private §7messages.");
                        return;
                    }

                    if (msg_ignore_array.contains(sender.getUniqueId().toString())) {
                        sender.sendMessage("§9"+msgSQL.replyToName +" §7has ignored you.");
                        msg_ignore_array.clear();
                        return;
                    }

                    display.getColorsForMessages(sender.getUniqueId());
                    String sql = "SELECT * FROM ranks WHERE rankName = '"+display.privateMessageColors+"'";
                    display.displayChatRank(sql);

                    api.sendMessage(msgSQL.replyToName, "§9From§7 ("+display.rankColor.replace("&", "§")+sender.getPlayer().getDisplayName()+"§7)"+"§7 "+input);


                    display.getColorsForMessages(UUID.fromString(getUUID));
                    sql = "SELECT * FROM ranks WHERE rankName = '"+display.privateMessageColors+"'";
                    display.displayChatRank(sql);

                    sender.sendMessage("§9To§7 ("+display.rankColor.replace("&", "§")+msgSQL.replyToName +"§7)"+"§7 "+input);

                    msgSQL.setup(UUID.fromString(getUUID), msgSQL.replyToName, sender.getDisplayName(), sender.getUniqueId());
                    this.playPrivateMessageSound(msgSQL.replyToName);
                });
                });
    }

    public void playPrivateMessageSound(String playerName) {
        byte[] soundByte = "Hello World".getBytes();
        api.forwardToPlayer(playerName, "BungeeCord", soundByte);

        api.registerForwardListener((channelName, player, data) -> {

                    if (settingsSQL.playerUUID == null || settingsSQL.settingsPmSound) {
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2L, 2L);
                    }
        });
    }

    public void playFriendsSound(String playerName) {
        byte[] soundByte = "Hello World".getBytes();
        api.forwardToPlayer(playerName, "BungeeCord", soundByte);

        api.registerForwardListener((channelName, player, data) -> {

            if (settingsSQL.playerUUID == null || settingsSQL.settingsFriendsSound) {
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 2L, 2L);
            }
        });
    }
}