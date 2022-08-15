package de.jeezycore.db;
import de.jeezycore.colors.Color;
// SQL imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class JeezySQL  {
    public void createConnection() {

        String url = "jdbc:mysql://localhost:3306/jeezydevelopment";
        String user = "root";
        String password = "";
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" connected to database."+Color.RESET);

        } catch (SQLException e) {
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" Something went wrong when tried to connect to your database."+Color.RESET);
            //System.out.println(e.getMessage());
        }
    }

}
