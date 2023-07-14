package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;

public class ChatColorSQL {

    String url;
    String user;
    String password;

    String chatColorName;


    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }


    public void create(Player p, String colorName, String color, String colorRGB, int colorPriority) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM chatColors WHERE colorName = '"+colorName+"'";
            ResultSet rs = stm.executeQuery(sql_select);

            if (!rs.next()) {
                chatColorName = null;
            } else {
                do {
                    chatColorName = rs.getString(1);
                } while (rs.next());
            }

            if (chatColorName == null) {
                String sql_chatColor_insert ="INSERT INTO chatColors" +
                        "(colorName, color, colorRGB, colorPriority) " +
                        "VALUES ('"+colorName+"', '"+color+"', " +
                        "'"+colorRGB+"', '"+colorPriority+"')";
                stm.executeUpdate(sql_chatColor_insert);
                p.sendMessage("§7You §2successfully §7created the chatColor "+color+colorName+"§7.");
            } else {
                p.sendMessage("§7The chatColor "+color+colorName+" §calready §7exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}