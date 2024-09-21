package de.jeezycore.db;
import de.jeezycore.discord.messages.grant.RealtimeGrant;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.PermissionHandler;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static de.jeezycore.utils.ArrayStorage.playerRankNames;
import static de.jeezycore.utils.NameTag.scoreboard;

public class RanksSQL {
    public String url;
    public String user;
    public String password;

    public String rank;
    public String rankRGB;

    public String rankColor;

    public static String rankNameInformation;

    public String privateMessageColors;

    public String allPlayerInformationUUID;

    public String rankColor_second;
    public String grantPlayerUUID;

    public String grantPlayerRank;
    
    public static String [] new_perms;

    public String createRankMsg;
    
    public static HashSet<String> rankPerms = new HashSet<String>();

    public LinkedHashMap<String, String> rankData = new LinkedHashMap<String, String>();

    public static LinkedList<String> rankColorData = new LinkedList<>();

    public String getRankPerms;

    public String rankColorPerms;

    public String show_color;

    public static String permPlayerRankName;

    public static String permPlayerRankColor;

    public static String permRankPerms;

    public static String joinPermRanks;

    public static String grantingPermRanks;

    public static String unGrantingPermRanks;

    String removeRankGui_result;

    String removeRankGui_result_names;

    public static ArrayList<String> permPlayerUUIDArray = new ArrayList<String>();

    public static String getPlayerUUID;
    
    public String ban_end;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime currentTime = LocalDateTime.now();;

    private LocalDateTime updatedTime;

    String getItemPlayerUUID;


    public void createRank(String sql, String rankName, String rankRGB, String rankColor, String rankPriority)  {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rankName);
            pstmt.setString(2, rankRGB);
            pstmt.setString(3, rankColor);
            pstmt.setString(4, rankPriority);

            pstmt.executeUpdate();
            createRankMsg = "§bSuccessfully§f created §l{colorID}{rank}§f rank.";
        } catch (SQLException e) {
            createRankMsg = "§7Rank §l{colorID}{rank}§4 §7already §4exist§7.";
            System.out.println(e);
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteRank(Player p, String rankName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql = "DELETE FROM ranks" +
                    " WHERE rankName = '"+rankName+"'";
            p.sendMessage("§fYou §2successfully §fremoved the §9"+rankName+" §frank!");
            statement.executeUpdate(sql);

        } catch (SQLException e) {
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void grantPlayer(String rankName, UUID getPlayerToGrant, HumanEntity player) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.onUnGrantingPerms(player);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql2 = "SELECT playerUUID, rank FROM players WHERE playerUUID = '"+getPlayerToGrant+"'";
            resultSet = statement.executeQuery(sql2);

            if (!resultSet.next()) {
                grantPlayerUUID = null;
                grantPlayerRank = null;
            } else {
                do {
                    grantPlayerUUID = resultSet.getString(1);
                    grantPlayerRank = resultSet.getString(2);
                } while (resultSet.next());
            }

            colorPerms(rankName);

            if (grantPlayerRank != null && grantPlayerRank.equalsIgnoreCase(rankName)) {
                player.sendMessage("§7The "+rankColorPerms.replace("&", "§")+rankName+"§7 rank has been §4already §7granted to the player.");
                return;
            }

            if (grantPlayerUUID != null) {
                String sql = "UPDATE players " +
                        "SET rank = '"+rankName +
                        "' WHERE playerUUID = '"+getPlayerToGrant+"'";

                statement.executeUpdate(sql);
            } else {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                String select_sql ="INSERT INTO players" +
                        "(playerName, playerUUID, rank, firstJoined, lastSeen, online) " +
                        "VALUES ('"+ UUIDChecker.uuidName + "', '"+ UUIDChecker.uuid +"', '"+
                        rankName+ "','"+ timestamp + "',"+"'"+timestamp+"', false)";
                statement.executeUpdate(select_sql);
            }
            player.sendMessage("You §b§lsuccessfully§f granted §l§7" + UUIDChecker.uuidName + "§f the §l" +rankColorPerms.replace("&", "§")+rankName + " §frank.");
            playerRankNames.add(UUIDChecker.uuidName);
            TabListSQL tabListSQL = new TabListSQL();
            tabListSQL.getTabListData(UUID.fromString(UUIDChecker.uuid));
            scoreboard.getTeam(TabListSQL.getTabListPriority+""+TabListSQL.getTabListRanks).addEntry(UUIDChecker.uuidName);
        } catch (SQLException e) {
            System.out.println(e);
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


    public void grantPlayerConsole(CommandSender sender, String rankName, UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.onGrantingPermsConsole(uuid, rankName);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql2 = "SELECT playerUUID, rank FROM players WHERE playerUUID = '"+uuid+"'";
            resultSet = statement.executeQuery(sql2);

            if (!resultSet.next()) {
                grantPlayerUUID = null;
                grantPlayerRank = null;
            } else {
                do {
                    grantPlayerUUID = resultSet.getString(1);
                    grantPlayerRank = resultSet.getString(2);
                } while (resultSet.next());
            }

            colorPerms(rankName);

            if (grantPlayerRank != null && grantPlayerRank.equalsIgnoreCase(rankName)) {
                sender.sendMessage("§7The "+rankColorPerms.replace("&", "§")+rankName+"§7 rank has been §4already §7granted to the player.");
                return;
            }

            if (grantPlayerUUID != null) {
                String sql = "UPDATE players " +
                        "SET rank = '"+rankName +
                        "' WHERE playerUUID = '"+UUIDChecker.uuid+"'";

                statement.executeUpdate(sql);
            } else {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                String select_sql ="INSERT INTO players" +
                        "(playerName, playerUUID, rank, firstJoined, lastSeen, online) " +
                        "VALUES ('"+ UUIDChecker.uuidName + "', '"+ UUIDChecker.uuid +"', '"+
                        rankName+ "','"+ timestamp + "',"+"'"+timestamp+"', false)";
                statement.executeUpdate(select_sql);
            }
            sender.sendMessage("You §b§lsuccessfully§f granted §l§7" + UUIDChecker.uuidName + "§f the §l" +rankColorPerms.replace("&", "§")+rankName + " §frank.");
            scoreboard.getTeam(grantPlayerRank).addEntry(UUIDChecker.uuidName);
            playerRankNames.add(UUIDChecker.uuidName);
        } catch (SQLException e) {
            System.out.println(e);
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

    public void displayData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT * FROM ranks ORDER BY rankPriority DESC";
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                rank = null;
                rankRGB = null;
                rankColor = null;
                rankData.clear();
                rankColorData.clear();
            } else {
                do {
                    rank = resultSet.getString(1);
                    rankRGB = resultSet.getString(2);
                    rankColor = resultSet.getString(3);
                    rankData.put(rank, rankRGB);
                    rankColorData.add(rankColor);
                }while(resultSet.next());
            }
        } catch (SQLException e) {
            System.out.println(e);
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

    public void getPlayerInformation(UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql = "SELECT rank FROM players WHERE playerUUID = '"+uuid+"'";

            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                rankNameInformation = null;
            } else {
                do {
                    rankNameInformation = resultSet.getString(1);
                } while (resultSet.next());
            }
        }catch (SQLException e) {
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

    public void getColorsForMessages(UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql = "SELECT rank FROM players WHERE playerUUID = '"+uuid+"'";

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                privateMessageColors = resultSet.getString(1);
            }
        }catch (SQLException e) {
            System.out.println(e);
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

    public void getAllPlayerInformation(Player p, String rankName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql = "SELECT * FROM players WHERE rank = '"+rankName+"'";

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                allPlayerInformationUUID = resultSet.getString(2);
                permPlayerUUIDArray.add(allPlayerInformationUUID);
            }
        }catch (SQLException e) {
            System.out.println(e);
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

    public void getPlayer(UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql = "SELECT rank FROM players WHERE playerUUID= '"+uuid+"'";

            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                getPlayerUUID = null;
            } else {
                do {
                    getPlayerUUID = resultSet.getString(1);
                } while (resultSet.next());
            }
        }catch (SQLException e) {
            System.out.println(e);
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

    public void displayChatRank(String sql) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                rank = null;
                rankRGB = null;
                rankColor_second = null;
                rankColor = null;
            } else {
                do {
                    rank = resultSet.getString(1);
                    rankRGB = resultSet.getString(2);
                    rankColor_second = resultSet.getString(3);
                    rankColor = resultSet.getString(3);
                }while (resultSet.next());
            }
            if (rank == null || rankColor == null || rankColor_second == null) {
                rank = "";
                rankColor_second = "§2";
                rankColor = "§2";
            }
        }catch (SQLException e) {
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

    public void colorPerms(String rank) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_color = "SELECT rankColor FROM ranks WHERE rankName = '" +rank+"'";
            resultSet = statement.executeQuery(select_color);
            while (resultSet.next()) {
                rankColorPerms = resultSet.getString(1);
            }
            if (rankColorPerms != null) {
                show_color = rankColorPerms.replace("&", "§");
            }
        }catch (SQLException e) {
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

    public void getRankData(String rank, Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT * FROM ranks WHERE rankName = '" +rank+"'";
            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                permPlayerRankName = null;
                permPlayerRankColor = null;
                permRankPerms = null;
            } else {
                do {
                    permPlayerRankName = resultSet.getString(1);
                    permPlayerRankColor = resultSet.getString(3);
                    permRankPerms = resultSet.getString(5);
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

    public void addPerms(String perm, String rank, Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            this.colorPerms(rank);
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '" +rank+"'";
            resultSet = statement.executeQuery(select_sql);
            while (resultSet.next()) {
                getRankPerms = resultSet.getString(1);
            }

            if (rankColorPerms == null) {
                p.sendMessage("§fThe rank: §b§l" + rank + " §fdoesn't exist.");
                return;
            }
            if (getRankPerms != null) {
                if (getRankPerms.contains(perm)) {
                    p.sendMessage("§fThis Perm §4already exist §ffor the §l"+show_color+rank+" §frank.");
                    return;
                }
                new_perms = getRankPerms.replace("[", "").replace("]", "").split(", ");
                rankPerms.addAll(Arrays.asList(new_perms));

            }

           rankPerms.add(perm);
           String sql = "UPDATE ranks " +
                   "SET rankPerms = '"+rankPerms +
                   "' WHERE rankName = '"+rank+"'";
            p.sendMessage("§fSuccessfully added the perm: §l§b"+perm+" §ffor the rank: §l"+show_color+rank+"§f.");
           statement.executeUpdate(sql);
           rankPerms.clear();

            this.getRankData(rank, p);
            PermissionHandler ph = new PermissionHandler();
            ph.onAddPerms(p, perm);
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


    public void removePerms(String perm, String rank, Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            this.colorPerms(rank);
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '" +rank+"'";
            resultSet = statement.executeQuery(select_sql);
            while (resultSet.next()) {
                getRankPerms = resultSet.getString(1);
            }
            if (rankColorPerms == null) {
                p.sendMessage("§fThe rank: §b§l" + rank + " §fdoesn't exist.");
                return;
            }
            if (getRankPerms == null) {
                p.sendMessage("§fThere are §cno perms§f to remove §ffor the §l"+show_color+rank+" §frank.");
                return;
            }
                new_perms = getRankPerms.replace("[", "").replace("]", "").split(", ");
                rankPerms.addAll(Arrays.asList(new_perms));

                if (rankPerms.contains(perm)) {
                    p.sendMessage("§fSuccessfully removed the perm: §l§b"+perm+" §ffor the rank: §l"+show_color+rank+"§f.");
                    rankPerms.remove(perm);
                } else {
                    p.sendMessage("§fCouldn't find the perm: §l§c"+perm+" §ffor the rank: §l"+show_color+rank+"§f.");
                    return;
                }


            if (new_perms.length == 1) {
                sql = "UPDATE ranks " +
                        "SET rankPerms = "+null +
                        " WHERE rankName = '"+rank+"'";
            } else {
                sql = "UPDATE ranks " +
                        "SET rankPerms = '"+rankPerms+
                        "' WHERE rankName = '"+rank+"'";
            }

            statement.executeUpdate(sql);
            this.getRankData(rank, p);
            PermissionHandler ph = new PermissionHandler();
            ph.onRemovePerms(p, perm);
            rankPerms.clear();
            getRankPerms = null;
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

    public void onJoinPerms(String rankName, UUID u) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '"+rankName+"'";
            resultSet = statement.executeQuery(select_sql);
            while (resultSet.next()) {
                joinPermRanks = resultSet.getString(1);
            }
            System.out.println(select_sql);
            System.out.println(u);
            System.out.println(joinPermRanks);

            PermissionHandler join = new PermissionHandler();
            join.onJoin(u);
            joinPermRanks = null;
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

    public void onGrantingPerms(HumanEntity p, String rank) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '"+rank+"'";
            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                grantingPermRanks = null;
            } else {
                do {
                    grantingPermRanks = resultSet.getString(1);
                } while (resultSet.next());
            }
            PermissionHandler onGrant = new PermissionHandler();
            onGrant.onGranting(p);
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

    public void onUnGrantingPerms(HumanEntity p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            this.getPlayer(ArrayStorage.grant_array.get(p.getUniqueId()));
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '"+getPlayerUUID+"'";

            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                unGrantingPermRanks = null;
            } else {
                do {
                    unGrantingPermRanks = resultSet.getString(1);
                } while (resultSet.next());
            }
            PermissionHandler onUnGrant = new PermissionHandler();
            onUnGrant.onUnGranting(p);
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

    public void onGrantingPermsConsole(UUID uuid, String rank) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '"+rank+"'";
            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                grantingPermRanks = null;
            } else {
                do {
                    grantingPermRanks = resultSet.getString(1);
                } while (resultSet.next());
            }
            PermissionHandler onGrant = new PermissionHandler();
            onGrant.onGrantingConsole(uuid);
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

    public void onUnGrantingPermsConsole(UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            this.getPlayer(uuid);
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '"+getPlayerUUID+"'";

            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                unGrantingPermRanks = null;
            } else {
                do {
                    unGrantingPermRanks = resultSet.getString(1);
                } while (resultSet.next());

            }
            PermissionHandler onUnGrant = new PermissionHandler();
            onUnGrant.onUnGrantingConsole(uuid);
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


    public void removeRankGui(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT playerUUID, playerName FROM players WHERE playerUUID = '"+ArrayStorage.grant_array.get(p.getUniqueId())+"'";
            resultSet = statement.executeQuery(select_sql);
            while (resultSet.next()) {
                removeRankGui_result = resultSet.getString(1);
                removeRankGui_result_names = resultSet.getString(2);
            }

            if (removeRankGui_result != null) {
                sql = "UPDATE players " +
                        "SET rank = "+null+
                        " WHERE playerUUID = '"+ArrayStorage.grant_array.get(p.getUniqueId())+"'";

                statement.executeUpdate(sql);
                RealtimeGrant unGrant_discord = new RealtimeGrant();
                unGrant_discord.realtimeChatOnUnGranting(ArrayStorage.grant_array.get(p.getUniqueId()), ArrayStorage.grant_array_names.get(p.getUniqueId()), p.getDisplayName());
            p.sendMessage("§aSuccessfully§f removed the rank from player §b§l" + ArrayStorage.grant_array_names.get(p.getUniqueId()));
            scoreboard.getTeam("ZDefault").addEntry(UUIDChecker.uuidName);
            playerRankNames.remove(UUIDChecker.uuidName);
            } else {
                p.sendMessage("§4§lThis player doesn't have a rank!");
            }
            ArrayStorage.grant_array.remove(p.getPlayer().getUniqueId());
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

    public void removeRankConsole(CommandSender sender, String playerName, UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql;
        try {
            this.onUnGrantingPermsConsole(UUID.fromString(UUIDChecker.uuid));
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT playerUUID, playerName FROM players WHERE playerUUID = '"+uuid+"'";
            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                removeRankGui_result = null;
                removeRankGui_result_names = null;
            } else {
                do {
                    removeRankGui_result = resultSet.getString(1);
                    removeRankGui_result_names = resultSet.getString(2);
                } while (resultSet.next());
            }

            if (removeRankGui_result != null) {
                sql = "UPDATE players " +
                        "SET rank = "+null+
                        " WHERE playerUUID = '"+uuid+"'";

               String sql_reset_rank_time = "UPDATE items " +
                        "SET rankStart = "+null+
                        " , rankEnd = "+null+
                        " WHERE playerUUID = '"+uuid+"'";

                statement.executeUpdate(sql);
                statement.executeUpdate(sql_reset_rank_time);
                sender.sendMessage("§aSuccessfully§7 removed the rank from player §9§l" + playerName);
                //NametagEdit.getApi().setPrefix(UUIDChecker.uuidName, "§2");
                playerRankNames.remove(UUIDChecker.uuidName);
            } else {
                sender.sendMessage("§4§lThis player doesn't have a rank!");
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

    public void setRankDuration(CommandSender sender, String username, String time, UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.rankMonthlyDurationTimeCheck(time);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql = null;
            String sql_select = "SELECT playerUUID FROM items WHERE playerUUID = '"+uuid+"'";

            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                getItemPlayerUUID = null;
            } else {
                do {
                    getItemPlayerUUID = resultSet.getString(1);
                } while (resultSet.next());
            }
            if (getItemPlayerUUID != null) {
                sql = "UPDATE items " +
                        "SET rankStart = '" + currentTime.format(formatter) + "', rankEnd = '" + updatedTime.format(formatter) + "'" +
                        " WHERE playerUUID = '" + uuid + "'";
            } else {
                sql = "INSERT INTO items " +
                        "(playerName, playerUUID, rankForever, rankStart, rankEnd) " +
                        "VALUES ('"+UUIDChecker.uuidName+"', '"+UUIDChecker.uuid+"', NULL, '"+currentTime.format(formatter)+"', '"+updatedTime.format(formatter)+"')";
            }
            System.out.println(sql);
            statement.executeUpdate(sql);
            sender.sendMessage("§7You §asuccessfully §7granted " + username + " §7for §9§l" + time + "§7.");
        } catch (Exception e) {
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

    private void rankRemoveAutomatically(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql_reset_item_duration = "UPDATE items " +
                    "SET rankStart = NULL, rankEnd = NULL" +
                    " WHERE playerUUID = '" + p.getUniqueId() + "'";

            String sql_reset_rank = "UPDATE players "+
                    "SET rank = NULL"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";

            String sql_reset_tag = "UPDATE players "+
                    "SET tag = NULL"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";

            String sql_reset_chatColor = "UPDATE players "+
                    "SET chatColor = NULL"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";

            statement.executeUpdate(sql_reset_item_duration);
            statement.executeUpdate(sql_reset_rank);
            statement.executeUpdate(sql_reset_tag);
            statement.executeUpdate(sql_reset_chatColor);
        } catch (Exception e) {
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


    public void rankMonthlyDurationTimeCheck(String time) {
        if (time.contains("d")) {
            updatedTime = currentTime.plusDays(Integer.parseInt(time.replace("d", "")));
        } else if (time.contains("h")) {
            updatedTime = currentTime.plusHours(Integer.parseInt(time.replace("h", "")));
        } else if (time.contains("m")) {
            updatedTime = currentTime.plusMinutes(Integer.parseInt(time.replace("m", "")));
        } else if (time.contains("s")) {
            updatedTime = currentTime.plusSeconds(Integer.parseInt(time.replace("s", "")));
        }
    }

    public void rankMonthlyDurationCalculator(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT * FROM items WHERE playerUUID = '"+p.getUniqueId()+"'";
            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                ban_end = null;
            } else {
                do {
                    ban_end = resultSet.getString(5);
                } while (resultSet.next());
            }

            if (ban_end == null) return;

        LocalDateTime dateTime_start = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        LocalDateTime dateTime_end = LocalDateTime.parse(ban_end, formatter);

        LocalDateTime fromTemp = LocalDateTime.from(dateTime_start);
        long years = fromTemp.until(dateTime_end, ChronoUnit.YEARS);
        fromTemp = fromTemp.plusYears(years);

        long months = fromTemp.until(dateTime_end, ChronoUnit.MONTHS);
        fromTemp = fromTemp.plusMonths(months);

        long days = fromTemp.until(dateTime_end, ChronoUnit.DAYS);
        fromTemp = fromTemp.plusDays(days);

        long hours = fromTemp.until(dateTime_end, ChronoUnit.HOURS);
        fromTemp = fromTemp.plusHours(hours);

        long minutes = fromTemp.until(dateTime_end, ChronoUnit.MINUTES);
        fromTemp = fromTemp.plusMinutes(minutes);

        long seconds = fromTemp.until(dateTime_end, ChronoUnit.SECONDS);
        fromTemp = fromTemp.plusSeconds(seconds);

        long millis = fromTemp.until(dateTime_end, ChronoUnit.MILLIS);

        if (seconds < 0) {
            rankRemoveAutomatically(p.getPlayer());
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
}