package de.jeezycore.db;
import de.jeezycore.colors.Color;
import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.utils.PermissionHandler;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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

    public static String player;

    public String grantPlayer;

    public String alreadyGranted;

    public static String [] grant_new_player;

    public static String [] new_perms;

    public String createRankMsg;

    public static ArrayList<String> player_name_array = new ArrayList<String>();

    public static ArrayList<String> rankPerms = new ArrayList<String>();

    public LinkedHashMap<String, Integer> rankData = new LinkedHashMap<String, Integer>();

    public String getRankPerms;

    public String rankColorPerms;

    public String show_color;

    public static String permPlayerName;

    public static String permRankPerms;

    private void createConnection() {

        File file = new File("C:\\Users\\Lassd\\IdeaProjects\\JeezyDevelopment\\JeezyCore\\src\\database.yml");
        FileConfiguration db = YamlConfiguration.loadConfiguration(file);
        MemorySection mc = (MemorySection) db.get("MYSQL");

        url = "jdbc:mysql://localhost:3306/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void createTable() {
        try {
            this.createConnection();

            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" connected to database."+Color.RESET);
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" created"+Color.YELLOW_BOLD+" jeezyCore"+Color.CYAN+" table."+Color.RESET);

            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS JeezyCore " +
                    " (rankName VARCHAR(255), " +
                    " rankColor INT(2), " +
                    " rankPriority INT(3), " +
                    " playerName longtext, " +
                    " rankPerms longtext, " +
                    " PRIMARY KEY ( rankName ))";
            stm.executeUpdate(sql);
            con.close();
        } catch (SQLException e) {
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" Something went wrong when tried to connect to your database."+Color.RESET);
            System.out.println(e.getMessage());
            System.out.println(e);
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

    private void alreadyGranted() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql_already_g = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ UUIDChecker.uuid +"%'";
            ResultSet rs = stm.executeQuery(sql_already_g);
            while (rs.next()) {
                alreadyGranted = rs.getString(4);
            }
            if (alreadyGranted != null) {
                if (alreadyGranted.contains(UUIDChecker.uuid)) {
                    System.out.println("getting executed");
                    grant_new_player = alreadyGranted.replace("]", "").replace("[", "").split(", ");

                    player_name_array.addAll(Arrays.asList(grant_new_player));

                    player_name_array.remove(UUIDChecker.uuid);


                    System.out.println(player_name_array);



                    String sql_already_g2;
                    if (player_name_array.size() == 0) {
                        sql_already_g2 = "UPDATE jeezycore " +
                                "SET playerName = NULL" +
                                " WHERE playerName LIKE '%" + UUIDChecker.uuid + "%'";
                    } else {
                        sql_already_g2 = "UPDATE jeezycore " +
                                "SET playerName = '" + player_name_array +
                                "' WHERE playerName LIKE '%" + UUIDChecker.uuid + "%'";
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

    public void grantPlayer(String rankName) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            this.alreadyGranted();

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
            player_name_array.add(player);

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

    private void setPerms(String rank, Player p) {
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
                new_perms = getRankPerms.replace("[", "").replace("]", "").split(",");
                rankPerms.addAll(Arrays.asList(new_perms));
            }
           rankPerms.add(perm);
           String sql = "UPDATE jeezycore " +
                   "SET rankPerms = '"+rankPerms +
                   "' WHERE rankName = '"+rank+"'";
            p.sendMessage("§fSuccessfully added the perm: §l§b"+perm+" §ffor the rank: §l"+show_color+rank+"§f.");
            PermissionHandler ph = new PermissionHandler();
            ph.onAddPerms(p, perm);
           stm.executeUpdate(sql);
           rankPerms.clear();
       con.close();
       this.setPerms(rank, p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}