package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import java.sql.*;
import java.util.UUID;

public class LogsSQL {

    public String url;
    public String user;
    public String password;

    public static String ban_log;
    public static String mute_log;

    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void punishment_log(UUID userUUID) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String select_sql = "SELECT * FROM punishments WHERE UUID = '" +userUUID+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                ban_log = rs.getString(11);
                mute_log = rs.getString(12);
            }
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshData() {
        ban_log = null;
        mute_log = null;
    }

}
