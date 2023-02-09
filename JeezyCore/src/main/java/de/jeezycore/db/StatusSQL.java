package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.sql.*;

public class StatusSQL {

    public String url;
    public String user;
    public String password;

    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void alreadyJoined(PlayerJoinEvent join) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            String select_sql ="UPDATE status " +
                    "SET online = true" +
                    " WHERE playerUUID = '"+ join.getPlayer().getUniqueId() + "'";;
            stm.executeUpdate(select_sql);
            con.close();
        } catch (SQLException ignored) {
        }
    }

    public void firstJoined(PlayerJoinEvent join) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            String select_sql ="INSERT INTO status" +
                    "(playerName, playerUUID, firstJoined, online) " +
                    "VALUES ('"+ join.getPlayer().getDisplayName() + "', '"+ join.getPlayer().getUniqueId() +"', " +
                    "'"+ timestamp + "', true)";
            stm.executeUpdate(select_sql);
            con.close();
        } catch (SQLException e) {
            alreadyJoined(join);
        }
    }

   public void lastSeen(PlayerQuitEvent quit) {
       try {
           this.createConnection();
           Connection con = DriverManager.getConnection(url, user, password);
           Statement stm = con.createStatement();

           Timestamp timestamp = new Timestamp(System.currentTimeMillis());

           String select_sql ="UPDATE status " +
                   "SET lastSeen = '"+ timestamp + "', online = false" +
                   " WHERE playerUUID = '"+ quit.getPlayer().getUniqueId() + "'";;
           stm.executeUpdate(select_sql);
           con.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
}