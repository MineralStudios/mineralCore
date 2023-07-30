package de.jeezycore.db;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static de.jeezycore.db.hikari.HikariCP.dataSource;

public class FriendsSQL {

    String playerName;
    UUID playerUUID;

    String friendsListArrayString;

    String [] friendsListArray;

    ArrayList<String> friendsList = new ArrayList<>();


    private void pushMYSQL(Player p, Player playerName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
                connection = dataSource.getConnection();
                statement = connection.createStatement();
                String sql_select = "SELECT * FROM friends WHERE playerUUID = '"+p.getUniqueId()+"'";
                resultSet = statement.executeQuery(sql_select);

                if (!resultSet.next()) {
                    playerUUID = null;
                    friendsListArrayString = null;
                } else {
                    do {
                        playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
                        friendsListArrayString = resultSet.getString("friendsList");
                        friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                        friendsList.addAll(Arrays.asList(friendsListArray));
                    } while (resultSet.next());
                }
                friendsList.add(playerName.getUniqueId().toString());
                if (playerUUID == null) {
                    statement.executeUpdate("INSERT INTO friends" +
                            "(playerName, playerUUID, friendsList) " +
                            "VALUES ('"+p.getDisplayName()+"', '"+p.getUniqueId()+"', '"+friendsList+"')");
                } else {
                    statement.executeUpdate("UPDATE friends " +
                            "SET friendsList = '"+friendsList+
                            "' WHERE playerUUID = '"+p.getUniqueId()+"'");
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
                friendsList.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void acceptFriends(Player sender, String playerName) {
        try {
            Player ps = Bukkit.getPlayer(playerName);
            pushMYSQL(ps, sender);
            sender.sendMessage("§9§l"+playerName+" §2successfully §7accepted accepted your friend request!");
            ps.sendMessage("§7You added §9"+sender.getDisplayName()+" §7to your friend list!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFriends(Player p, String playerName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Player ps = Bukkit.getPlayer(playerName);
            if (ps != null) {
                connection = dataSource.getConnection();
                statement = connection.createStatement();
                String sql_select = "SELECT * FROM players WHERE playerUUID = '"+ps.getUniqueId()+"'";
                resultSet = statement.executeQuery(sql_select);

                if (!resultSet.next()) {
                    playerUUID = null;
                } else {
                    do {
                        playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
                    } while (resultSet.next());
                }
                if (playerUUID == null) {
                    p.sendMessage("§7The player §c"+playerName+" §7has never been §cseen on the §9Mineral §fNetwork§7.");
                } else {
                    p.sendMessage("§7You §2successfully §7sent a friend request to §9"+playerName+"§7.");

                    TextComponent message = new TextComponent("§7(§2CLICK TO ACCEPT§7)");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends accept "+playerName));
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept the friend request.").create()));

                    System.out.println(message);

                    ps.sendMessage(" §9§l"+p.getDisplayName()+" §7has sent you a §2friend §7request!");
                    ps.sendMessage(message);
                }
            } else {
                p.sendMessage("§7The player §c"+playerName+" §7isn't online.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}