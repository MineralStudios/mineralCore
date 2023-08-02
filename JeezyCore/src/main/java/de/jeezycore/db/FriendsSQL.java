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

    String playerUUIDString;
    String playerNameStringForLists;

    int friendsLimit = 50;

    boolean friendsLimitStatus;

    public String [] friendsListArray;

    public ArrayList<String> friendsList = new ArrayList<>();

    public ArrayList<String> showFriendsList = new ArrayList<>();


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
                resultSet.close();
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
                resultSet.close();
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
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addFriendsMYSQL(Player sender, Player target) {
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
                resultSet.close();
                statement.close();
                connection.close();
                friendsList.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeFriendsMYSQL(Player sender) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            if (friendsList.isEmpty()) {
                statement.executeUpdate("DELETE FROM friends" +
                        " WHERE playerUUID = '"+sender.getUniqueId()+"'");
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

    public void friendsData(Player sender) {
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
                friendsListArrayString = null;
                friendsList.clear();
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
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void playersData(String playerName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT playerUUID FROM players WHERE playerName = '"+playerName+"'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                playerUUIDString = null;
            } else {
                do {
                    playerUUIDString = resultSet.getString(1);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayPlayersListData(Player sender, int page) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            for (int i = 0; i < friendsList.size(); i++) {
                String sql_select = "SELECT playerName FROM players WHERE playerUUID = '" + friendsList.get(i) + "'";
                resultSet = statement.executeQuery(sql_select);

                if (!resultSet.next()) {
                    playerNameStringForLists = null;
                    showFriendsList.clear();
                } else {
                    do {
                        playerNameStringForLists = resultSet.getString(1);
                        showFriendsList.add(playerNameStringForLists);
                    } while (resultSet.next());
                }
            }
            int pageEnd = (int) Math.ceil((double)showFriendsList.size() / 10);
            if (page > pageEnd) {
                page = pageEnd;
            }

            sender.sendMessage("                                               ");
            sender.sendMessage(" §9§lFriends §f§lList §7(§f"+page+" / §9§l"+pageEnd+"§7)            ");
            sender.sendMessage("                                               ");
            int resultCalculation = showFriendsList.size() - (10 * page - 10);
            for (int i = 0; i < showFriendsList.size(); i++) {
                sender.sendMessage("   §9"+showFriendsList.get((10 * page - 10) + i)+"            ");
                sender.sendMessage("                                               ");
                if (resultCalculation < 9 || i == 9) {
                    break;
                }
            }
            } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
                showFriendsList.clear();
                friendsList.clear();
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
                friendsList.clear();
            } else {
                do {
                    friendsListArrayString = resultSet.getString("friendsList");
                    friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                    friendsList.addAll(Arrays.asList(friendsListArray));
                } while (resultSet.next());
            }
            System.out.println(friendsList);
            if (friendsList.size() == friendsLimit) {
                friendsLimitStatus = true;
            } else {
                friendsLimitStatus = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
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
                friendsData(sender);
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
                addFriendsMYSQL(sender, receiver);

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
                    return;
                }

                    if (friendRequestsList.containsKey(sender.getPlayer().getUniqueId())) {
                        if (friendRequestsList.get(sender.getPlayer().getUniqueId()).contains(receiver.getUniqueId())) {
                            sender.sendMessage("§7You have §calready §7sent a friend request to §9"+playerName+"§7.");
                            return;
                        }
                    }

                friendsData(sender);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            friendsList.clear();
        }
    }

    public void removeFriends(Player sender, String playerName) {
        try {
            friendsData(sender);
            playersData(playerName);

            System.out.println(playerUUIDString);
            System.out.println(friendsList);

            if (!friendsList.contains(playerUUIDString)) {
                sender.sendMessage("§7You don't have §9"+playerName+" §7as a friend of yours.");
            } else {
                friendsList.remove(playerUUIDString);
                removeFriendsMYSQL(sender);
                sender.sendMessage("§7You §2successfully §7removed §9"+playerName+" §7from your friends list.");
            }
            friendsList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listFriends(Player sender, int page) {
        try {
            friendsData(sender);

            if (friendsList.isEmpty()) {
                sender.sendMessage("§7You don't have any §9friends §7added yet.");
                return;
            }

            displayPlayersListData(sender, page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}