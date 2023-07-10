package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class SettingsSQL {

    String url;
    String user;
    String password;

    String settingsAvailable;

    public boolean settingsMsg;

    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    private void setup(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM settings WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";
            ResultSet rs = stm.executeQuery(sql_select);
            if (!rs.next()) {
                settingsAvailable = null;
            } else {
                do {
                    settingsAvailable = rs.getString(2);
                } while (rs.next());
            }
            if (settingsAvailable == null) {
                String sql_settings_available ="INSERT INTO settings" +
                        "(playerName, playerUUID, ignoredPlayerList, msg) " +
                        "VALUES ('"+p.getDisplayName()+"', '"+p.getUniqueId()+"', " +
                        "NULL, false)";
                stm.executeUpdate(sql_settings_available);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSettingsData(UUID receiver) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM settings WHERE playerUUID = '"+receiver+"'";
            ResultSet rs = stm.executeQuery(sql_select);

            while (rs.next()) {
                settingsMsg = rs.getBoolean(4);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void disableMsg(Player p) {
        try {
            this.setup(p);
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sqlUpdateMsg = "UPDATE settings " +
                    "SET msg = false"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";
            stm.executeUpdate(sqlUpdateMsg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void enableMsg(Player p) {
        try {
            this.setup(p);
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sqlUpdateMsg = "UPDATE settings " +
                    "SET msg = true"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";
            stm.executeUpdate(sqlUpdateMsg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
