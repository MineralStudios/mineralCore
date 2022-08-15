package de.jeezycore.db;
import de.jeezycore.colors.Color;
// SQL imports
import java.sql.*;

public class JeezySQL  {
public String url;
public String user;
public String password;

    private void createConnection() {

        url = "jdbc:mysql://localhost:3306/jeezydevelopment";
        user = "root";
        password = "";
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" connected to database."+Color.RESET);
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" created"+Color.YELLOW_BOLD+" jeezyCore"+Color.CYAN+" table."+Color.RESET);
        } catch (SQLException e) {
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" Something went wrong when tried to connect to your database."+Color.RESET);
            System.out.println(e.getMessage());
        }

    }

    public void createTable() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS JeezyCore " +
                    " (rankName VARCHAR(255), " +
                    " rankColor VARCHAR(255), " +
                    " playerName VARCHAR(255), " +
                    " PRIMARY KEY ( rankName ))";
            stm.executeUpdate(sql);
        } catch (SQLException e){
            System.out.println(e);
        }
    }

    public void pushData(String sql, String rankName, String rankColor, String playerName)  {
    this.createConnection();
        try {
            System.out.println(sql);
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, rankName);
            pstmt.setString(2, rankColor);
            pstmt.setString(3, playerName);

            pstmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e);
        }

    }

}
