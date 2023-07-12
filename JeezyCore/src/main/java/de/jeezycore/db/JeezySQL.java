package de.jeezycore.db;
import de.jeezycore.colors.Color;
import de.jeezycore.config.JeezyConfig;
import de.jeezycore.discord.messages.grant.RealtimeGrant;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.PermissionHandler;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
// SQL imports
import java.sql.*;
import java.util.*;

public class JeezySQL  {
    public String url;
    public String user;
    public String password;

    public String rank;
    public String rankRGB;

    public String rankColor;

    public String rankNameInformation;

    public String privateMessageColors;

    public String allPlayerInformationUUID;

    public String rankColor_second;
    public String grantPlayerUUID;

    public String grantPlayerRank;

    public String grantPlayer_2;


    public static String [] grant_new_player;
    public static String [] grant_new_player_2;

    public static String [] new_perms;

    public String createRankMsg;

    public static ArrayList<String> player_name_array = new ArrayList<String>();

    public static ArrayList<String> player_name_array_2 = new ArrayList<String>();

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


    private void createConnection() {


        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    private void createDB() {
        try {

            MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

            Connection con = DriverManager.getConnection("jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/", (String) mc.get("user"), (String) mc.get("password"));
            Statement stm = con.createStatement();
            String jeezyDB = "CREATE DATABASE IF NOT EXISTS jeezydevelopment";
            stm.executeUpdate(jeezyDB);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try {
            createDB();
            this.createConnection();

            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String ranks_table = "CREATE TABLE IF NOT EXISTS ranks " +
                    " (rankName VARCHAR(255), " +
                    " rankRGB VARCHAR(50), " +
                    " rankColor VARCHAR(10), " +
                    " rankPriority INT(3), " +
                    " rankPerms longtext, " +
                    " staffRank boolean DEFAULT FALSE," +
                    " PRIMARY KEY ( rankName ))";

            String players_table = "CREATE TABLE IF NOT EXISTS players " +
                    " (playerName VARCHAR(255), " +
                    " playerUUID VARCHAR(255), " +
                    " minerals longtext, " +
                    " rank VARCHAR(255), " +
                    " tag VARCHAR(255), " +
                    " chatColor VARCHAR(255), " +
                    " firstJoined VARCHAR(255), " +
                    " lastSeen VARCHAR(255), " +
                    " online boolean DEFAULT FALSE," +
                    " PRIMARY KEY ( playerUUID ))";

            String items_table = "CREATE TABLE IF NOT EXISTS items " +
                    " (playerName VARCHAR(255), " +
                    " playerUUID VARCHAR(255), " +
                    " ownedTags longtext, " +
                    " ownedChatColors VARCHAR(255), " +
                    " PRIMARY KEY ( playerUUID ))";

            String punishments_table = "CREATE TABLE IF NOT EXISTS punishments " +
                    " (playerName VARCHAR(255), " +
                    " UUID VARCHAR(255), " +
                    " banned_forever boolean, " +
                    " mute_forever boolean, " +
                    " ban_start longtext, " +
                    " ban_end longtext, " +
                    " ban_status boolean, " +
                    " mute_start longtext, " +
                    " mute_end longtext, " +
                    " mute_status boolean, " +
                    " ban_logs longtext, " +
                    " mute_logs longtext, "+
                    " PRIMARY KEY ( UUID ))";

            String tags_table = "CREATE TABLE IF NOT EXISTS tags " +
                    " (tagName VARCHAR(255), " +
                    " tagCategory VARCHAR(255), " +
                    " tagDesign VARCHAR(255), " +
                    " tagPriority INT(3), " +
                    " PRIMARY KEY ( tagName ))";

            String reward_table = "CREATE TABLE IF NOT EXISTS rewards " +
                    " (UUID VARCHAR(255), " +
                    " playerName VARCHAR(255), " +
                    " claimed boolean, " +
                    " time longtext, " +
                    " price VARCHAR(255), " +
                    " PRIMARY KEY ( UUID ))";

            String minerals_table = "CREATE TABLE IF NOT EXISTS minerals " +
                    " (ServerName VARCHAR(255), " +
                    " minerals_data longtext, " +
                    " PRIMARY KEY ( ServerName ))";

            String settings_table = "CREATE TABLE IF NOT EXISTS settings " +
                    " (playerName VARCHAR(255), " +
                    " playerUUID VARCHAR(255), " +
                    " ignoredPlayerList longtext, " +
                    " msg boolean, " +
                    " PRIMARY KEY ( playerUUID ))";

            stm.executeUpdate(ranks_table);
            stm.executeUpdate(players_table);
            stm.executeUpdate(items_table);
            stm.executeUpdate(punishments_table);
            stm.executeUpdate(tags_table);
            stm.executeUpdate(reward_table);
            stm.executeUpdate(minerals_table);
            stm.executeUpdate(settings_table);

            stm.close();
            if (con.isValid(20)) {
                System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" connected to database."+Color.RESET);
                System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" created"+Color.YELLOW_BOLD+" DB & Tables"+Color.CYAN+"."+Color.RESET);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" Something went wrong when tried to connect to your database."+Color.RESET);
        }
    }

    public void pushData(String sql, String rankName, String rankRGB, String rankColor, String rankPriority)  {
        this.createConnection();
        try {
            System.out.println(sql);
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, rankName);
            pstmt.setString(2, rankRGB);
            pstmt.setString(3, rankColor);
            pstmt.setString(4, rankPriority);

            pstmt.executeUpdate();
            createRankMsg = "§bSuccessfully§f created §l{colorID}{rank}§f rank.";
            con.close();
        } catch (SQLException e) {
            createRankMsg = "§7Rank §l{colorID}{rank}§4 §7already §4exist§7.";
            System.out.println(e);
        }
    }

    public void grantPlayer(String rankName, UUID getPlayerToGrant, HumanEntity player) {
        try {
            this.createConnection();
            this.onUnGrantingPerms(player);
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql2 = "SELECT playerUUID, rank FROM players WHERE playerUUID = '"+getPlayerToGrant+"'";
            ResultSet rs = stm.executeQuery(sql2);

            if (!rs.next()) {
                grantPlayerUUID = null;
                grantPlayerRank = null;
            } else {
                do {
                    grantPlayerUUID = rs.getString(1);
                    grantPlayerRank = rs.getString(2);
                } while (rs.next());
            }

            colorPerms(rankName);

            if (grantPlayerRank != null && grantPlayerRank.equalsIgnoreCase(rankName)) {
                player.sendMessage("§7The "+rankColorPerms.replace("&", "§")+rankName+"§7 rank has been §4already §7granted to the player.");
                con.close();
                return;
            }

            if (grantPlayerUUID != null) {
                String sql = "UPDATE players " +
                        "SET rank = '"+rankName +
                        "' WHERE playerUUID = '"+getPlayerToGrant+"'";

                stm.executeUpdate(sql);
            } else {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                String select_sql ="INSERT INTO players" +
                        "(playerName, playerUUID, rank, firstJoined, lastSeen, online) " +
                        "VALUES ('"+ UUIDChecker.uuidName + "', '"+ UUIDChecker.uuid +"', '"+
                        rankName+ "','"+ timestamp + "',"+"'"+timestamp+"', false)";
                stm.executeUpdate(select_sql);
            }
            player.sendMessage("You §b§lsuccessfully§f granted §l§7" + UUIDChecker.uuidName + "§f the §l" +rankColorPerms.replace("&", "§")+rankName + " §frank.");
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    public void grantPlayerConsole(CommandSender sender, String rankName, UUID uuid) {
        try {
            this.createConnection();
            this.onGrantingPermsConsole(uuid, rankName);
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql2 = "SELECT playerUUID, rank FROM players WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            ResultSet rs = stm.executeQuery(sql2);

            if (!rs.next()) {
                grantPlayerUUID = null;
                grantPlayerRank = null;
            } else {
                do {
                    grantPlayerUUID = rs.getString(1);
                    grantPlayerRank = rs.getString(2);
                } while (rs.next());
            }

            colorPerms(rankName);

            if (grantPlayerRank != null && grantPlayerRank.equalsIgnoreCase(rankName)) {
                sender.sendMessage("§7The "+rankColorPerms.replace("&", "§")+rankName+"§7 rank has been §4already §7granted to the player.");
                con.close();
                return;
            }

            if (grantPlayerUUID != null) {
                String sql = "UPDATE players " +
                        "SET rank = '"+rankName +
                        "' WHERE playerUUID = '"+UUIDChecker.uuid+"'";

                stm.executeUpdate(sql);
            } else {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                String select_sql ="INSERT INTO players" +
                        "(playerName, playerUUID, rank, firstJoined, lastSeen, online) " +
                        "VALUES ('"+ UUIDChecker.uuidName + "', '"+ UUIDChecker.uuid +"', '"+
                        rankName+ "','"+ timestamp + "',"+"'"+timestamp+"', false)";
                stm.executeUpdate(select_sql);
            }
            sender.sendMessage("You §b§lsuccessfully§f granted §l§7" + UUIDChecker.uuidName + "§f the §l" +rankColorPerms.replace("&", "§")+rankName + " §frank.");
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
            String sql = "SELECT * FROM ranks ORDER BY rankPriority DESC";
            ResultSet rs = stm.executeQuery(sql);

            if (!rs.next()) {
                rank = null;
                rankRGB = null;
                rankColor = null;
                rankData.clear();
                rankColorData.clear();
            } else {
                do {
                    rank = rs.getString(1);
                    rankRGB = rs.getString(2);
                    rankColor = rs.getString(3);
                    rankData.put(rank, rankRGB);
                    rankColorData.add(rankColor);
                }while(rs.next());
            }
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void getPlayerInformation(Player p) {

        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            String sql = "SELECT rank FROM players WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";

            ResultSet rs = stm.executeQuery(sql);

            if (!rs.next()) {
                rankNameInformation = null;
            } else {
                do {
                    rankNameInformation = rs.getString(1);
                } while (rs.next());
            }
            con.close();
        }catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void getColorsForMessages(UUID uuid) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            String sql = "SELECT rank FROM players WHERE playerUUID = '"+uuid+"'";

            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                privateMessageColors = rs.getString(1);
            }

            con.close();
        }catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void getAllPlayerInformation(Player p, String rankName) {

        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            String sql = "SELECT * FROM players WHERE rank = '"+rankName+"'";

            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                allPlayerInformationUUID = rs.getString(2);

                permPlayerUUIDArray.add(allPlayerInformationUUID);

            }

            con.close();
        }catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void getPlayer(UUID uuid) {

        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            String sql = "SELECT rank FROM players WHERE playerUUID= '"+uuid+"'";

            ResultSet rs = stm.executeQuery(sql);

            if (!rs.next()) {
                getPlayerUUID = null;
            } else {
                do {
                    getPlayerUUID = rs.getString(1);
                } while (rs.next());
            }
            con.close();
        }catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void displayChatRank(String sql) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (!rs.next()) {
                rank = null;
                rankRGB = null;
                rankColor_second = null;
                rankColor = null;
            } else {
                do {
                    rank = rs.getString(1);
                    rankRGB = rs.getString(2);
                    rankColor_second = rs.getString(3);
                    rankColor = rs.getString(3);
                }while (rs.next());
            }
            if (rank == null || rankColor == null || rankColor_second == null) {
                rank = "";
                rankColor_second = "§2";
                rankColor = "§2";
            }
        con.close();
        }catch (SQLException e) {
            System.out.println("HERE ERROR");
            System.out.println(e);
        }
    }

    public void colorPerms(String rank) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_color = "SELECT rankColor FROM ranks WHERE rankName = '" +rank+"'";
            ResultSet rs = stm.executeQuery(select_color);
            while (rs.next()) {
                rankColorPerms = rs.getString(1);
            }
            System.out.println(select_color);
            System.out.println(rankColorPerms);
            if (rankColorPerms != null) {
                show_color = rankColorPerms.replace("&", "§");
            }

            con.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getRankData(String rank, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM ranks WHERE rankName = '" +rank+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                permPlayerRankName = rs.getString(1);
                permPlayerRankColor = rs.getString(3);
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
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '" +rank+"'";
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
           String sql = "UPDATE ranks " +
                   "SET rankPerms = '"+rankPerms +
                   "' WHERE rankName = '"+rank+"'";
            p.sendMessage("§fSuccessfully added the perm: §l§b"+perm+" §ffor the rank: §l"+show_color+rank+"§f.");
           stm.executeUpdate(sql);
           rankPerms.clear();

            this.getRankData(rank, p);
            PermissionHandler ph = new PermissionHandler();
            ph.onAddPerms(p, perm);

            con.close();
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
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '" +rank+"'";
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
                sql = "UPDATE ranks " +
                        "SET rankPerms = "+null +
                        " WHERE rankName = '"+rank+"'";
            } else {
                sql = "UPDATE ranks " +
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

    public void onJoinPerms(String rankName, UUID u) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '"+rankName+"'";
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

    public void onGrantingPerms(HumanEntity p, String rank) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '"+rank+"'";
            ResultSet rs = stm.executeQuery(select_sql);

            if (!rs.next()) {
                grantingPermRanks = null;
            } else {
                do {
                    grantingPermRanks = rs.getString(1);
                } while (rs.next());
            }
            PermissionHandler onGrant = new PermissionHandler();
            onGrant.onGranting(p);
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onUnGrantingPerms(HumanEntity p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            this.getPlayer(p.getUniqueId());
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '"+getPlayerUUID+"'";

            ResultSet rs = stm.executeQuery(select_sql);

            if (!rs.next()) {
                unGrantingPermRanks = null;
            } else {
                do {
                    unGrantingPermRanks = rs.getString(1);
                } while (rs.next());
            }
            PermissionHandler onUnGrant = new PermissionHandler();
            onUnGrant.onUnGranting(p);
            rs.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onGrantingPermsConsole(UUID uuid, String rank) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '"+rank+"'";
            ResultSet rs = stm.executeQuery(select_sql);

            if (!rs.next()) {
                grantingPermRanks = null;
            } else {
                do {
                    grantingPermRanks = rs.getString(1);
                } while (rs.next());
            }
            PermissionHandler onGrant = new PermissionHandler();
            onGrant.onGrantingConsole(uuid);
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onUnGrantingPermsConsole(UUID uuid) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            this.getPlayer(uuid);
            String select_sql = "SELECT rankPerms FROM ranks WHERE rankName = '"+getPlayerUUID+"'";

            ResultSet rs = stm.executeQuery(select_sql);

            if (!rs.next()) {
                unGrantingPermRanks = null;
            } else {
                do {
                    unGrantingPermRanks = rs.getString(1);
                } while (rs.next());

            }
            PermissionHandler onUnGrant = new PermissionHandler();
            onUnGrant.onUnGrantingConsole(uuid);
            rs.close();
            con.close();

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
            String select_sql = "SELECT playerUUID, playerName FROM players WHERE playerUUID = '"+ArrayStorage.grant_array.get(p.getUniqueId())+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                removeRankGui_result = rs.getString(1);
                removeRankGui_result_names = rs.getString(2);
            }

            if (removeRankGui_result != null) {
                sql = "UPDATE players " +
                        "SET rank = "+null+
                        " WHERE playerUUID = '"+ArrayStorage.grant_array.get(p.getUniqueId())+"'";

                stm.executeUpdate(sql);
                RealtimeGrant unGrant_discord = new RealtimeGrant();
                unGrant_discord.realtimeChatOnUnGranting(ArrayStorage.grant_array.get(p.getUniqueId()), ArrayStorage.grant_array_names.get(p.getUniqueId()), p.getDisplayName());
            p.sendMessage("§aSuccessfully§f removed the rank from player §b§l" + ArrayStorage.grant_array_names.get(p.getUniqueId()));
            } else {
                p.sendMessage("§4§lThis player doesn't have a rank!");
            }
            ArrayStorage.grant_array.remove(p.getPlayer().getUniqueId());
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeRankConsole(CommandSender sender, String playerName, UUID uuid) {
        String sql;
        try {
            this.createConnection();
            this.onUnGrantingPermsConsole(UUID.fromString(UUIDChecker.uuid));
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT playerUUID, playerName FROM players WHERE playerUUID = '"+uuid+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                removeRankGui_result = rs.getString(1);
                removeRankGui_result_names = rs.getString(2);
            }

            if (removeRankGui_result != null) {
                sql = "UPDATE players " +
                        "SET rank = "+null+
                        " WHERE playerUUID = '"+uuid+"'";

                stm.executeUpdate(sql);
                sender.sendMessage("§aSuccessfully§7 removed the rank from player §9§l" + playerName);
            } else {
                sender.sendMessage("§4§lThis player doesn't have a rank!");
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}