package de.jeezycore.db;
import de.jeezycore.colors.Color;
import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.utils.UUIDChecker;
// SQL imports
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

public static String grant_new_player;

public String createRankMsg;

public static ArrayList<String> player_name_array = new ArrayList<String>();

public LinkedHashMap<String, Integer> rankData = new LinkedHashMap<String, Integer>();


    private void createConnection() {
        url = "jdbc:mysql://localhost:3306/jeezydevelopment";
        user = "root";
        password = "";
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
                    grant_new_player = alreadyGranted.replace("]", "").replace("[", "");
                    player_name_array.add(grant_new_player);

                    System.out.println(player_name_array);

                    player_name_array.remove(UUIDChecker.uuid);

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
                grant_new_player = grantPlayer.replace("]", "").replace("[", "");

                player_name_array.add(grant_new_player);
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

        }catch (SQLException e) {
            System.out.println(e);
    }
    }

}
