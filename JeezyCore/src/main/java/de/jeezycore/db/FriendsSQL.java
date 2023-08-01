package de.jeezycore.db;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static de.jeezycore.utils.ArrayStorage.friendsOnJoinMessageArray;

public class FriendsSQL {

    UUID playerUUID;

    String friendsListArrayString;

    int friendsLimit = 2;

    boolean friendsLimitStatus;

    public String [] friendsListArray;

    public ArrayList<String> friendsList = new ArrayList<>();


    public ArrayList<UUID> friendRequestsArrayList = new ArrayList<>();
    HashMap<UUID, ArrayList<UUID>> friendRequestsList = new HashMap<>();

    RanksSQL ranksSQL = new RanksSQL();

    public void getAllFriendsOnJoin(PlayerJoinEvent p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT friendsList FROM friends WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                playerUUID = null;
                friendsListArray = null;
            } else {
                do {
                    friendsListArrayString = resultSet.getString(1);
                    friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                    friendsList.addAll(Arrays.asList(friendsListArray));
                } while (resultSet.next());

                if (friendsList.isEmpty()) return;
                int countPlayersOnline = 0;
                for (int i = 0; i < friendsList.size(); i++) {

                    try {
                        if (!Bukkit.getPlayer(UUID.fromString(friendsList.get(i))).isOnline()) {
                            continue;
                        }
                        countPlayersOnline += 1;
                    } catch (Exception f) {
                    }
                }
                p.getPlayer().sendMessage(" §7§l[§9FRIENDS§7§l] §9§l"+countPlayersOnline+" §f§l/ §9§l"+friendsList.size()+" §7friends are §2online.");
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

    public void sendFriendOnlineMessage(PlayerJoinEvent p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM friends WHERE friendsList LIKE '%"+p.getPlayer().getUniqueId()+"%'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                playerUUID = null;
            } else {
                do {
                    playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
                    friendsOnJoinMessageArray.add(playerUUID);
                } while (resultSet.next());

                if (playerUUID == null) return;

                ranksSQL.getPlayerInformation(p.getPlayer());
                String sql = "SELECT * FROM ranks WHERE rankName = '"+ranksSQL.rankNameInformation+"'";
                ranksSQL.displayChatRank(sql);

                for (int i = 0; i < friendsOnJoinMessageArray.size(); i++) {
                    try {
                        if (!Bukkit.getPlayer(friendsOnJoinMessageArray.get(i)).isOnline()) {
                            continue;
                        }
                        Bukkit.getPlayer(friendsOnJoinMessageArray.get(i)).sendMessage(" §7§l[§9FRIENDS§7§l] "+ranksSQL.rankColor.replace("&", "§") + " "+p.getPlayer().getDisplayName()+" §7just came §2online§7!");
                    } catch (Exception f) {
                    }
                }
                friendsOnJoinMessageArray.clear();
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

    public void sendFriendOfflineMessage(PlayerQuitEvent p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM friends WHERE friendsList LIKE '%"+p.getPlayer().getUniqueId()+"%'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                playerUUID = null;
            } else {
                do {
                    playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
                    friendsOnJoinMessageArray.add(playerUUID);
                } while (resultSet.next());

                if (playerUUID == null) return;

                ranksSQL.getPlayerInformation(p.getPlayer());
                String sql = "SELECT * FROM ranks WHERE rankName = '"+ranksSQL.rankNameInformation+"'";
                ranksSQL.displayChatRank(sql);

                for (int i = 0; i < friendsOnJoinMessageArray.size(); i++) {
                    try {
                        if (!Bukkit.getPlayer(friendsOnJoinMessageArray.get(i)).isOnline()) {
                            continue;
                        }
                        Bukkit.getPlayer(friendsOnJoinMessageArray.get(i)).sendMessage(" §7§l[§9FRIENDS§7§l] "+ranksSQL.rankColor.replace("&", "§") + " "+p.getPlayer().getDisplayName()+" §7just went §coffline§7!");
                    } catch (Exception f) {
                    }
                }
                friendsOnJoinMessageArray.clear();
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

    private void pushMYSQL(Player sender, Player target) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
                connection = dataSource.getConnection();
                statement = connection.createStatement();
                String sql_select = "SELECT * FROM friends WHERE playerUUID = '"+sender.getUniqueId()+"'";
                resultSet = statement.executeQuery(sql_select);

                if (!resultSet.next()) {
                    playerUUID = null;
                    friendsListArrayString = null;
                    friendsListArray = null;
                } else {
                    do {
                        playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
                        friendsListArrayString = resultSet.getString("friendsList");
                        friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                        friendsList.addAll(Arrays.asList(friendsListArray));
                    } while (resultSet.next());
                }
                friendsList.add(target.getUniqueId().toString());
                if (playerUUID == null) {
                    statement.executeUpdate("INSERT INTO friends" +
                            "(playerName, playerUUID, friendsList) " +
                            "VALUES ('"+sender.getDisplayName()+"', '"+sender.getUniqueId()+"', '"+friendsList+"')");
                } else {
                    statement.executeUpdate("UPDATE friends " +
                            "SET friendsList = '"+friendsList+
                            "' WHERE playerUUID = '"+sender.getUniqueId()+"'");
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

    public void checkIfAlreadyFriends(Player sender) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM friends WHERE playerUUID = '"+sender.getUniqueId()+"'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                friendsListArray = null;
            } else {
                do {
                    friendsListArrayString = resultSet.getString("friendsList");
                    friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                    friendsList.addAll(Arrays.asList(friendsListArray));
                } while (resultSet.next());
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

    public void checkFriendsLimit(Player sender) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM friends WHERE playerUUID = '" + sender.getUniqueId() + "'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                friendsListArrayString = null;
                friendsListArray = null;
            } else {
                do {
                    friendsListArrayString = resultSet.getString("friendsList");
                    friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                    friendsList.addAll(Arrays.asList(friendsListArray));
                } while (resultSet.next());
            }

            if (friendsList.size() == friendsLimit) {
                friendsLimitStatus = true;
            } else {
                friendsLimitStatus = false;
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


    public void acceptFriends(Player receiver, String target) {
        try {
            Player sender = Bukkit.getPlayerExact(target);
            if (sender != null) {
                checkIfAlreadyFriends(sender);
                if (friendsList.contains(receiver.getUniqueId().toString())) {
                    receiver.sendMessage("§7You §calready §7accepted §9"+target+"`s §7friend request!");
                    return;
                }
                try {
                    if (!friendRequestsList.get(sender.getPlayer().getUniqueId()).contains(receiver.getUniqueId())) {}
                } catch (Exception e) {
                    receiver.sendMessage("§7You haven't §cgotten §7a friend request from §9"+target+" §7yet!");
                    return;
                }

                friendsList.clear();
                pushMYSQL(sender, receiver);

                sender.sendMessage("§9§l"+receiver.getDisplayName()+" §7successfully §2accepted §7your friend request!");
                receiver.sendMessage("§7You §2accepted §9§l"+target+"`s §7friend request!");

            } else {
                receiver.sendMessage("§7The player you are trying to §caccept §7the friend request from isn't §2online §7anymore!");
                return;
            }
            friendRequestsList.get(sender.getPlayer().getUniqueId()).remove(receiver.getUniqueId());
            friendRequestsArrayList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFriends(Player sender, String playerName) {
        try {
            Player receiver = Bukkit.getPlayerExact(playerName);
            if (receiver != null) {
                checkFriendsLimit(sender);
                if (friendsLimitStatus) {
                    sender.sendMessage("§7You have §calready §7reached the friends limit of §9"+friendsLimit+" §7friends.");
                    friendsList.clear();
                    return;
                }

                    if (friendRequestsList.containsKey(sender.getPlayer().getUniqueId())) {
                        if (friendRequestsList.get(sender.getPlayer().getUniqueId()).contains(receiver.getUniqueId())) {
                            sender.sendMessage("§7You have §calready §7sent a friend request to §9"+playerName+"§7.");
                            return;
                        }
                    }

                checkIfAlreadyFriends(sender);
                if (friendsList.contains(receiver.getUniqueId().toString())) {
                    sender.sendMessage("§7You §calready §7have §9"+receiver.getDisplayName()+" §7as a friend.");
                    return;
                }

                friendRequestsArrayList.add(receiver.getUniqueId());
                friendRequestsList.put(sender.getPlayer().getUniqueId(), friendRequestsArrayList);

                sender.sendMessage("§7You §2successfully §7sent a friend request to §9"+playerName+"§7.");

                    TextComponent message = new TextComponent("             §7(§2CLICK TO ACCEPT§7)                 ");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends accept "+sender.getDisplayName()));
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept the friend request.").create()));

                    receiver.sendMessage("                                                                    ");
                    receiver.sendMessage(" §9§l"+sender.getDisplayName()+" §7has sent you a §2friend §7request!");
                    receiver.sendMessage("                                                                    ");
                    receiver.sendMessage(message);
                    receiver.sendMessage("                                                                    ");

                } else {
                sender.sendMessage("§7The player §c"+playerName+" §7isn't online.");
            }
            friendsList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}