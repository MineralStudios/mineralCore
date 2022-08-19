package de.jeezycore.db;
import de.jeezycore.colors.Color;
// SQL imports
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class JeezySQL  {
public String url;
public String user;
public String password;

public String rank;
public int rankColor;

public String player;

public HashMap<String, Integer> rankData = new HashMap<String, Integer>();


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
                    " playerName VARCHAR(255), " +
                    " PRIMARY KEY ( rankName ))";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" Something went wrong when tried to connect to your database."+Color.RESET);
            System.out.println(e.getMessage());
            System.out.println(e);
        }
    }

    public void pushData(String sql, String rankName, String rankColor)  {
    this.createConnection();
        try {
            System.out.println(sql);
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, rankName);
            pstmt.setString(2, rankColor);

             pstmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e);
        }

    }


    public void displayData() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql = "SELECT * FROM jeezycore";
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                rank = rs.getString(1);
                rankColor = rs.getInt(2);
                player = rs.getString(3);
                rankData.put(rank, rankColor);
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
    }



}
