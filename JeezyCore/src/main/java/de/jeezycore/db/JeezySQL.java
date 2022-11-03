package de.jeezycore.db;
import de.jeezycore.colors.Color;
import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.discord.chat.RealtimeChat;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.PermissionHandler;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
// SQL imports
import java.io.File;
import java.sql.*;
import java.util.*;

public class JeezySQL  {
    public String url;
    public String user;
    public String password;

    public String rank;
    public int rankColor;
    public String grantPlayer;

    public String alreadyGranted;

    public static String [] grant_new_player;

    public static String [] new_perms;

    public String createRankMsg;

    public static ArrayList<String> player_name_array = new ArrayList<String>();

    public static HashSet<String> rankPerms = new HashSet<String>();

    public LinkedHashMap<String, Integer> rankData = new LinkedHashMap<String, Integer>();

    public String getRankPerms;

    public String rankColorPerms;

    public String show_color;

    public static String permPlayerName;

    public static String permRankPerms;

    public static String joinPermRanks;

    public static String grantingPermRanks;

    public static String unGrantingPermRanks;

    String removeRankGui_result;

    public static String [] removeRankGui_arr;

    public static ArrayList<String> removeRankGui_list = new ArrayList<String>();

    private void createConnection() {

        File file = new File("/home/jeffrey/IdeaProjects/JeezyCore/JeezyCore/src/main/java/database.yml");
        FileConfiguration db = YamlConfiguration.loadConfiguration(file);
        MemorySection mc = (MemorySection) db.get("MYSQL");

        url = "jdbc:mysql://localhost:3306/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void createTable() {
        try {
            this.createConnection();

            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String jeezyCore_table = "CREATE TABLE IF NOT EXISTS jeezycore " +
                    " (rankName VARCHAR(255), " +
                    " rankColor INT(2), " +
                    " rankPriority INT(3), " +
                    " playerName longtext, " +
                    " rankPerms longtext, " +
                    " PRIMARY KEY ( rankName ))";
            String punishments_table = "CREATE TABLE IF NOT EXISTS punishments " +
                    " (UUID VARCHAR(255), " +
                    " banned_forever boolean, " +
                    " ban_start longtext, " +
                    " ban_end longtext, " +
                    " mute_time longtext, " +
                    " punishment_log longtext, "+
                    " PRIMARY KEY ( UUID ))";
            stm.executeUpdate(jeezyCore_table);
            stm.executeUpdate(punishments_table);
            stm.close();
            if (con.isValid(20)) {
                System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" connected to database."+Color.RESET);
                System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" created"+Color.YELLOW_BOLD+" tables"+Color.CYAN+"."+Color.RESET);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" Something went wrong when tried to connect to your database."+Color.RESET);
        }
    }

    public void pushData(String sql, String rankName, String rankColor, String rankPriority)  {
        this.createConnection();
        try {
            System.out.println(sql);
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, rankName);
            pstmt.setString(2, rankColor);
            pstmt.setString(3, rankPriority);

            pstmt.executeUpdate();
            createRankMsg = "§bSuccessfully§f created §l{colorID}{rank}§f rank.";
            con.close();
        } catch (SQLException e) {
            createRankMsg = "Rank §l{colorID}{rank}§4 already exist.";
            System.out.println(e);
        }
    }

    private void alreadyGranted(UUID getWhoExecuted) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql_already_g = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ ArrayStorage.grant_array.get(getWhoExecuted)+"%'";
            ResultSet rs = stm.executeQuery(sql_already_g);
            while (rs.next()) {
                alreadyGranted = rs.getString(4);
            }
            if (alreadyGranted != null) {
                if (alreadyGranted.contains(UUIDChecker.uuid)) {
                    System.out.println("getting executed");
                    grant_new_player = alreadyGranted.replace("]", "").replace("[", "").split(", ");

                    player_name_array.addAll(Arrays.asList(grant_new_player));

                    player_name_array.remove(ArrayStorage.grant_array.get(getWhoExecuted).toString());


                    System.out.println(player_name_array);



                    String sql_already_g2;
                    if (player_name_array.size() == 0) {
                        sql_already_g2 = "UPDATE jeezycore " +
                                "SET playerName = NULL" +
                                " WHERE playerName LIKE '%" + ArrayStorage.grant_array.get(getWhoExecuted) + "%'";
                    } else {
                        sql_already_g2 = "UPDATE jeezycore " +
                                "SET playerName = '" + player_name_array +
                                "' WHERE playerName LIKE '%" + ArrayStorage.grant_array.get(getWhoExecuted) + "%'";
                    }
                    System.out.println(sql_already_g);
                    System.out.println(sql_already_g2);
                    stm.executeUpdate(sql_already_g2);

                    player_name_array.clear();
                }
            }
        con.close();
        }catch (SQLException e) {
            System.out.println(e);
        }

    }

    public void grantPlayer(String rankName, UUID getWhoExecuted) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            this.alreadyGranted(getWhoExecuted);

            Statement stm = con.createStatement();
            String sql2 = "SELECT playerName FROM jeezycore WHERE rankName = '"+rankName+"'";
            ResultSet rs = stm.executeQuery(sql2);
            while (rs.next()) {
                grantPlayer = rs.getString(1);
            }

            if (grantPlayer != null) {
                if (grantPlayer.contains(UUIDChecker.uuid)) return;
                grant_new_player = grantPlayer.replace("]", "").replace("[", "").split(", ");

                player_name_array.addAll(Arrays.asList(grant_new_player));

            }
            player_name_array.add(String.valueOf(ArrayStorage.grant_array.get(getWhoExecuted)));

            String sql = "UPDATE jeezycore " +
                    "SET playerName = '"+player_name_array +
                    "' WHERE rankName = '"+rankName+"'";
            System.out.println(sql);
            stm.executeUpdate(sql);
            player_name_array.clear();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void displayData() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql = "SELECT * FROM jeezycore ORDER BY rankPriority DESC";
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                rank = rs.getString(1);
                rankColor = rs.getInt(2);
                rankData.put(rank, rankColor);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void displayChatRank(String sql) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                rank = rs.getString(1);
                rankColor = rs.getInt(2);
            }
        con.close();
        }catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void colorPerms(String rank) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_color = "SELECT rankColor FROM jeezycore WHERE rankName = '" +rank+"'";
            ResultSet rs = stm.executeQuery(select_color);
            while (rs.next()) {
                rankColorPerms = rs.getString(1);
            }
            System.out.println(select_color);
            System.out.println(rankColorPerms);
            if (rankColorPerms != null) {
                show_color = ColorTranslator.colorTranslator.get(Integer.parseInt(rankColorPerms));
            }

            con.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void getRankData(String rank, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM jeezycore WHERE rankName = '" +rank+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                permPlayerName = rs.getString(4);
                permRankPerms = rs.getString(5);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addPerms(String perm, String rank, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            this.colorPerms(rank);
            String select_sql = "SELECT rankPerms FROM jeezycore WHERE rankName = '" +rank+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                getRankPerms = rs.getString(1);
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
           String sql = "UPDATE jeezycore " +
                   "SET rankPerms = '"+rankPerms +
                   "' WHERE rankName = '"+rank+"'";
            p.sendMessage("§fSuccessfully added the perm: §l§b"+perm+" §ffor the rank: §l"+show_color+rank+"§f.");
           stm.executeUpdate(sql);
           rankPerms.clear();
       con.close();
       this.getRankData(rank, p);
            PermissionHandler ph = new PermissionHandler();
            ph.onAddPerms(p, perm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removePerms(String perm, String rank, Player p) {
        String sql;
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            this.colorPerms(rank);
            String select_sql = "SELECT rankPerms FROM jeezycore WHERE rankName = '" +rank+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                getRankPerms = rs.getString(1);
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
                sql = "UPDATE jeezycore " +
                        "SET rankPerms = "+null +
                        " WHERE rankName = '"+rank+"'";
            } else {
                sql = "UPDATE jeezycore " +
                        "SET rankPerms = '"+rankPerms+
                        "' WHERE rankName = '"+rank+"'";
            }

            stm.executeUpdate(sql);
            con.close();
            this.getRankData(rank, p);
            PermissionHandler ph = new PermissionHandler();
            ph.onRemovePerms(p, perm);
            rankPerms.clear();
            getRankPerms = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onJoinPerms(UUID u) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT rankPerms FROM jeezycore WHERE playerName LIKE '%"+ u.toString() +"%'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                joinPermRanks = rs.getString(1);
            }
            System.out.println(select_sql);
            System.out.println(u);
            System.out.println(joinPermRanks);

            PermissionHandler join = new PermissionHandler();
            join.onJoin(u);
            rs.close();
            con.close();
            joinPermRanks = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onGrantingPerms(HumanEntity p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT rankPerms FROM jeezycore WHERE playerName LIKE '%"+ ArrayStorage.grant_array.get(p.getUniqueId()).toString() +"%'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                grantingPermRanks = rs.getString(1);
            }

            PermissionHandler onGrant = new PermissionHandler();
            onGrant.onGranting(p);
            rs.close();
            con.close();
            grantingPermRanks = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onUnGrantingPerms(HumanEntity p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT rankPerms FROM jeezycore WHERE playerName LIKE '%"+ ArrayStorage.grant_array.get(p.getUniqueId()).toString() +"%'";

            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                unGrantingPermRanks = rs.getString(1);
            }

            System.out.println(unGrantingPermRanks);
            PermissionHandler onUnGrant = new PermissionHandler();
            onUnGrant.onUnGranting(p);
            rs.close();
            con.close();
            unGrantingPermRanks = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removeRankGui(Player p) {
        String sql;
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT playerName FROM jeezycore WHERE playerName LIKE '%"+ ArrayStorage.grant_array.get(p.getUniqueId()) +"%'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                removeRankGui_result = rs.getString(1);
            }

            if (removeRankGui_result != null) {
                removeRankGui_arr = removeRankGui_result.replace("[", "").replace("]", "").split(", ");
                removeRankGui_list.addAll(Arrays.asList(removeRankGui_arr));


            removeRankGui_list.remove(ArrayStorage.grant_array.get(p.getUniqueId()));
            if (removeRankGui_arr.length == 1) {
                sql = "UPDATE jeezycore " +
                        "SET playerName = "+null +
                        " WHERE playerName LIKE '%"+ ArrayStorage.grant_array.get(p.getUniqueId()) +"%'";
            } else {
                sql = "UPDATE jeezycore " +
                        "SET playerName = '"+removeRankGui_list+
                        "' WHERE playerName LIKE '%"+ ArrayStorage.grant_array.get(p.getUniqueId()) +"%'";
            }
                stm.executeUpdate(sql);
                RealtimeChat unGrant_discord = new RealtimeChat();
                unGrant_discord.realtimeChatOnUnGranting(ArrayStorage.grant_array.get(p.getUniqueId()), ArrayStorage.grant_array_names.get(p.getUniqueId()), p.getDisplayName());
            p.sendMessage("§aSuccessfully§f removed the rank from player §b§l" + ArrayStorage.grant_array_names.get(p.getUniqueId()));
            } else {
                p.sendMessage("§4§lThis player doesn't have a rank!");
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}