package de.jeezycore.db;

import de.jeezycore.main.Main;
import de.jeezycore.utils.BungeeChannelApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static de.jeezycore.utils.ArrayStorage.*;

public class FriendsSQL {

    public UUID playerUUID;

    String friendsListArrayString;

    String friendsRequestsListArrayString;

    String playerUUIDString;
    String playerNameStringForLists;

    int friendsLimit = 50;

    boolean friendsLimitStatus;

    public String [] friendsListArray;

    public String [] friendsListRequestArray;

    RanksSQL ranksSQL = new RanksSQL();

    SettingsSQL settingsSQL = new SettingsSQL();

    BungeeChannelApi bungeeChannelApi = new BungeeChannelApi();

    io.github.leonardosnt.bungeechannelapi.BungeeChannelApi api = io.github.leonardosnt.bungeechannelapi.BungeeChannelApi.of(Main.getPlugin(Main.class));


    public void getAllFriendsData(Player p) {
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
                friendsListData.clear();
            } else {
                do {
                    try {
                        friendsListArrayString = resultSet.getString(1);
                        friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                        friendsListData.addAll(Arrays.asList(friendsListArray));
                    } catch (Exception e) {
                    }
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
                friendsList.clear();
            } else {
                do {
                    try {
                        friendsListArrayString = resultSet.getString(1);
                        friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                        friendsList.addAll(Arrays.asList(friendsListArray));
                    } catch (Exception e) {

                    }

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
                    try {
                        playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
                        friendsOnJoinMessageArray.add(playerUUID);
                    } catch (Exception e) {

                    }

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
                        Bukkit.getPlayer(friendsOnJoinMessageArray.get(i)).sendMessage("§7§l[§9FRIENDS§7§l] "+ranksSQL.rankColor.replace("&", "§") + " "+p.getPlayer().getDisplayName()+" §7just came §2online§7!");
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
                    try {
                        playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
                        friendsOnJoinMessageArray.add(playerUUID);
                    } catch (Exception e) {

                    }

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
                        Bukkit.getPlayer(friendsOnJoinMessageArray.get(i)).sendMessage("§7§l[§9FRIENDS§7§l] "+ranksSQL.rankColor.replace("&", "§") + " "+p.getPlayer().getDisplayName()+" §7just went §coffline§7!");
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

    private void addFriendsMysqlSender(String senderName, UUID senderUUID, Player receiver) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
                connection = dataSource.getConnection();
                statement = connection.createStatement();
                String sql_select = "SELECT * FROM friends WHERE playerUUID = '"+senderUUID+"'";
                resultSet = statement.executeQuery(sql_select);

                if (!resultSet.next()) {
                    playerUUID = null;
                    friendsListArrayString = null;
                    friendsListArray = null;
                    friendsList.clear();
                } else {
                    do {
                        try {
                            playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
                            friendsListArrayString = resultSet.getString("friendsList");
                            friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                            friendsList.addAll(Arrays.asList(friendsListArray));
                        } catch (Exception e) {

                        }

                    } while (resultSet.next());
                }
                friendsList.add(receiver.getUniqueId().toString());
                if (playerUUID == null) {
                    statement.executeUpdate("INSERT INTO friends" +
                            "(playerName, playerUUID, friendsList) " +
                            "VALUES ('"+senderName+"', '"+senderUUID+"', '"+friendsList+"')");
                } else {
                    statement.executeUpdate("UPDATE friends " +
                            "SET friendsList = '"+friendsList+
                            "' WHERE playerUUID = '"+senderUUID+"'");
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

    private void addFriendsMysqlReceiver(String senderName, UUID senderUUID, Player receiver) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM friends WHERE playerUUID = '"+receiver.getUniqueId()+"'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                playerUUID = null;
                friendsListArrayString = null;
                friendsListArray = null;
                friendsList.clear();
            } else {
                do {
                    try {
                        playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
                        friendsListArrayString = resultSet.getString("friendsList");
                        friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                        friendsList.addAll(Arrays.asList(friendsListArray));
                    } catch (Exception e) {
                    }
                } while (resultSet.next());
            }

            friendsList.add(senderUUID.toString());
            if (playerUUID == null) {
                statement.executeUpdate("INSERT INTO friends" +
                        "(playerName, playerUUID, friendsList) " +
                        "VALUES ('"+receiver.getDisplayName()+"', '"+receiver.getUniqueId()+"', '"+friendsList+"')");
            } else {
                statement.executeUpdate("UPDATE friends " +
                        "SET friendsList = '"+friendsList+
                        "' WHERE playerUUID = '"+receiver.getUniqueId()+"'");
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

    public void friendsSwitcherMYSQL(Player sender, String switcher, String message) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            settingsSQL.setup(sender);
            settingsSQL.getSettingsData(sender.getUniqueId());
            connection = dataSource.getConnection();
            statement = connection.createStatement();


            String sqlUpdateMsg = "UPDATE settings " +
                    "SET friendsRequests = "+switcher+
                    " WHERE playerUUID = '"+sender.getUniqueId()+"'";
            statement.executeUpdate(sqlUpdateMsg);

            sender.sendMessage("§7§l[§9FRIENDS§7§l] §7You §7successfully "+message+" §9friends §7requests.");
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

    private void removeFriendsMYSQL(UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            if (friendsList.isEmpty()) {
                statement.executeUpdate("DELETE FROM friends" +
                        " WHERE playerUUID = '"+uuid+"'");
            } else {
                statement.executeUpdate("UPDATE friends " +
                        "SET friendsList = '"+friendsList+
                        "' WHERE playerUUID = '"+uuid+"'");
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

    private void pushFriendsRequest(String receiverUsername, UUID receiver, Player sender) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM friends WHERE playerUUID = '"+receiver+"'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                playerUUID = null;
                friendsRequestsListArrayString = null;
                friendsListRequestArray = null;
                friendRequestsArrayList.clear();
            } else {
                do {
                    playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
                    friendsRequestsListArrayString = resultSet.getString("friendsRequests");
                    friendsListRequestArray = friendsRequestsListArrayString.replace("[", "").replace("]", "").split(", ");
                    friendRequestsArrayList.addAll(Arrays.asList(friendsListRequestArray));
                } while (resultSet.next());
            }
            friendRequestsArrayList.add(sender.getUniqueId().toString());
            if (playerUUID == null) {
                statement.executeUpdate("INSERT INTO friends" +
                        "(playerName, playerUUID, friendsRequests) " +
                        "VALUES ('"+receiverUsername+"', '"+receiver+"', '"+friendRequestsArrayList+"')");
            } else {
                statement.executeUpdate("UPDATE friends " +
                        "SET friendsRequests = '"+friendRequestsArrayList+
                        "' WHERE playerUUID = '"+receiver+"'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
                friendRequestsArrayList.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void friendsData(UUID senderUUID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM friends WHERE playerUUID = '"+senderUUID+"'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                friendsListArray = null;
                friendsListRequestArray = null;
                friendsList.clear();
            } else {
                do {
                    try {
                        friendsListArrayString = resultSet.getString("friendsList");
                        friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                        friendsList.addAll(Arrays.asList(friendsListArray));
                    } catch (Exception e) {
                    }
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

    public void friendsDataRequestList(UUID senderUUID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM friends WHERE playerUUID = '"+senderUUID+"'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                friendsRequestsListArrayString = null;
                friendsListRequestArray = null;
                friendRequestsArrayList.clear();
            } else {
                do {
                    try {
                        friendsRequestsListArrayString = resultSet.getString("friendsRequests");
                        friendsListRequestArray = friendsRequestsListArrayString.replace("[", "").replace("]", "").split(", ");
                        friendRequestsArrayList.addAll(Arrays.asList(friendsListRequestArray));
                    } catch (Exception e) {
                    }
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
            for (int i = 0; i < resultCalculation; i++) {
                sender.sendMessage("   §9"+showFriendsList.get((10 * page - 10) + i)+"            ");
                sender.sendMessage("                                               ");
                if (i == 9) {
                    break;
                }
            }
            System.out.println(showFriendsList);
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
                    try {
                        friendsListArrayString = resultSet.getString("friendsList");
                        friendsListArray = friendsListArrayString.replace("[", "").replace("]", "").split(", ");
                        friendsList.addAll(Arrays.asList(friendsListArray));
                    } catch (Exception e) {

                    }

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
            bungeeChannelApi.getPlayerStatus(target);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    if (bungeeChannelApi.isPlayerOnline) {
                        friendsData(bungeeChannelApi.getUUID);

                        if (friendsList.contains(receiver.getUniqueId().toString())) {
                            receiver.sendMessage("§7§l[§9FRIENDS§7§l] §7You §calready §7accepted §9"+target+"`s §7friend request!");
                            return;
                        }

                        /*
                        if (friendsAddList.containsKey(bungeeChannelApi.getUUID)) {
                            if (!friendsAddList.get(bungeeChannelApi.getUUID).contains(receiver.getUniqueId())) {
                                receiver.sendMessage("§7§l[§9FRIENDS§7§l] §7The friend request §cexpired§7!");
                                return;
                            }
                        }
                         */
                        friendsList.clear();
                        addFriendsMysqlSender(target, bungeeChannelApi.getUUID, receiver);
                        addFriendsMysqlReceiver(target, bungeeChannelApi.getUUID, receiver);

                        api.sendMessage(target, "§7§l[§9FRIENDS§7§l] §9§l"+receiver.getDisplayName()+" §7successfully §2accepted §7your friend request!");

                        receiver.sendMessage("§7§l[§9FRIENDS§7§l] §7You §2accepted §9§l"+target+"`s §7friend request!");

                        /*
                        friendRequestsList.get(bungeeChannelApi.getUUID).remove(receiver.getUniqueId().toString());
                        friendRequestsArrayList.clear();
                         */

                    } else {
                        receiver.sendMessage("§7§l[§9FRIENDS§7§l] §7The friend request §ccan't §7be accepted anymore!");
                    }

                }
            }, 300L);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFriends(Player sender, String playerName) {
        try {
            bungeeChannelApi.getPlayerStatus(playerName);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (bungeeChannelApi.isPlayerOnline) {
                        checkFriendsLimit(sender);
                        if (friendsLimitStatus) {
                            sender.sendMessage("§7§l[§9FRIENDS§7§l] §7You have §calready §7reached the friends limit of §9"+friendsLimit+" §7friends.");
                            return;
                        }
                        friendsData(bungeeChannelApi.getUUID);

                        if (friendsList.contains(bungeeChannelApi.getUUID.toString())) {
                            sender.sendMessage("§7§l[§9FRIENDS§7§l] §7You §calready §7have §9"+playerName+" §7as a friend.");
                            return;
                        }
                        settingsSQL.getSettingsData(bungeeChannelApi.getUUID);
                        if (!settingsSQL.friendsRequests && settingsSQL.playerUUID != null) {
                            sender.sendMessage("§7§l[§9FRIENDS§7§l] §9"+playerName + " §7has turned §coff §7friends requests.");
                            return;

                        }

                        friendsDataRequestList(bungeeChannelApi.getUUID);
                        if (friendRequestsArrayList.contains(sender.getUniqueId().toString())) {
                            sender.sendMessage("§7§l[§9FRIENDS§7§l] §7You have §calready §7sent a §7friend request!");
                            return;
                        }
                        friendRequestsArrayList.clear();
                        pushFriendsRequest(playerName, bungeeChannelApi.getUUID, sender);

                        /*
                        friendRequestsArrayList.add(bungeeChannelApi.getUUID.toString());
                        friendRequestsList.put(sender.getPlayer().getUniqueId(), friendRequestsArrayList);

                        friendsAddArrayList.add(bungeeChannelApi.getUUID);
                        friendsAddList.put(sender.getPlayer().getUniqueId(), friendsAddArrayList);
                         */

                        sender.sendMessage("§7You §2successfully §7sent a friend request to §9"+playerName+"§7.");

                        bungeeChannelApi.sendFriendRequest(sender, sender.getDisplayName(), playerName);

                        bungeeChannelApi.playFriendsSound(playerName);

                        /*
                        timeRemoverForFriendsAdd(sender, bungeeChannelApi.getUUID);
                        timeRemoverForFriendsRequests(sender, bungeeChannelApi.getUUID);
                         */

                    } else {
                        sender.sendMessage("§7The player §c"+playerName+" §7isn't online.");
                    }
                }
            }, 300L);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            friendsList.clear();
        }
    }

    public void removeFriendsBothParties(Player sender, String playerName) {
        try {
                friendsList.clear();
                removeFriendsExecutor(sender, playerName);
                removeFriendsReceiver(sender, playerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFriendsExecutor(Player sender, String playerName) {
        try {
            playersData(playerName);
            friendsData(sender.getUniqueId());

            if (!friendsList.contains(playerUUIDString)) {
                sender.sendMessage("§7§l[§9FRIENDS§7§l] §7You don't have §9"+playerName+" §7as a friend of yours.");
                return;
            }

            System.out.println("EXECUTOR");
            System.out.println(friendsList);

            friendsList.remove(playerUUIDString);
            removeFriendsMYSQL(sender.getUniqueId());


            sender.sendMessage("§7§l[§9FRIENDS§7§l] §7You §2successfully §7removed §9"+playerName+" §7from your friends list.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFriendsReceiver(Player sender, String playerName) {
        try {
            playersData(playerName);
            friendsData(UUID.fromString(playerUUIDString));

            System.out.println("RECEIVER");
            System.out.println(friendsList);

                friendsList.remove(sender.getUniqueId().toString());
                removeFriendsMYSQL(UUID.fromString(playerUUIDString));

                Player ps = Bukkit.getPlayer(UUID.fromString(playerUUIDString));

                if (ps != null) {
                    ps.sendMessage("§7§l[§9FRIENDS§7§l] §9§l"+sender.getDisplayName() + " §7has §cended §7the friendship!");
                }
        } catch (Exception e) {
        }
    }

    public void listFriends(Player sender, int page) {
        try {
            friendsData(sender.getUniqueId());

            if (friendsList.isEmpty()) {
                sender.sendMessage("§7§l[§9FRIENDS§7§l] §7You don't have any §9friends §7added yet.");
                return;
            }

            displayPlayersListData(sender, page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void timeRemoverForFriendsAdd(Player sender, UUID receiver) {
        Timer time = new Timer();
        TimerTask RemoveAddList = new TimerTask() {
            @Override
            public void run() {
                try {
                    friendsAddList.get(sender.getPlayer().getUniqueId()).remove(receiver);
                    System.out.println("can't accept anymore");
                    time.purge();
                    time.cancel();
                } catch (Exception e) {
                }
            }
        };
        time.schedule(RemoveAddList, 10000);
    }

    private void timeRemoverForFriendsRequests(Player sender, UUID receiver) {
        Timer time = new Timer();
        TimerTask RemoveAddList = new TimerTask() {
            @Override
            public void run() {
                try {
                    friendRequestsList.get(sender.getPlayer().getUniqueId()).remove(receiver);
                    System.out.println("You can send a friend request again");
                    time.purge();
                    time.cancel();
                } catch (Exception e) {
                }
            }
        };
        time.schedule(RemoveAddList, 60000);
    }

}