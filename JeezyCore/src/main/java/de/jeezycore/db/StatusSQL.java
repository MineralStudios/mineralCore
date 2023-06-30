package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class StatusSQL {

    public String url;
    public String user;
    public String password;

    ArrayList<String> player_rank_usernames = new ArrayList<String>();
    ArrayList<String> player_rank_uuids = new ArrayList<String>();

    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    private void createConnectionPractice() {
        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+"practice";
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

   public void checkIfUsernameChanged(PlayerJoinEvent p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String playerName = null;

            String select_sql = "SELECT playerName FROM status WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";

            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                playerName = rs.getString(1);
            }

            if (!p.getPlayer().getDisplayName().equalsIgnoreCase(playerName)) {
                updateIfUsernameChanged(p);
                updateEloTable(p);
                updateRanksUsernames(p);
            }
            playerName = null;
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
   }

   private void updateEloTable(PlayerJoinEvent p) {
       try {
           this.createConnectionPractice();
           Connection con = DriverManager.getConnection(url, user, password);
           Statement stm = con.createStatement();

           String select_sql ="UPDATE elo " +
                   "SET PLAYER = '"+ p.getPlayer().getDisplayName() + "'"+
                   " WHERE UUID = '"+ p.getPlayer().getUniqueId() + "'";;
           stm.executeUpdate(select_sql);
           con.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

   private void updateRanksUsernames(PlayerJoinEvent p) {
       try {
           this.createConnection();
           Connection con = DriverManager.getConnection(url, user, password);

           Statement stm = con.createStatement();
           String sql = "SELECT playerName, playerUUID FROM jeezycore WHERE playerUUID LIKE '%"+ p.getPlayer().getUniqueId() +"%'";

           String grantPlayer = null;
           String grantUUID = null;
           String [] grant_new_player;
           String [] grant_new_UUIDS;

           ResultSet rs = stm.executeQuery(sql);
           while (rs.next()) {
               grantPlayer = rs.getString(1);
               grantUUID = rs.getString(2);
           }

           if (grantPlayer != null) {
               grant_new_player = grantPlayer.replace("]", "").replace("[", "").split(", ");
               grant_new_UUIDS = grantUUID.replace("]", "").replace("[", "").split(", ");

               player_rank_usernames.addAll(Arrays.asList(grant_new_player));
               player_rank_uuids.addAll(Arrays.asList(grant_new_UUIDS));
               player_rank_usernames.remove(player_rank_uuids.indexOf(p.getPlayer().getUniqueId().toString()));
               player_rank_usernames.add(p.getPlayer().getDisplayName());
           }
           String sql_update = "UPDATE jeezycore " +
                   "SET playerName = '"+player_rank_usernames +
                   "' WHERE playerUUID LIKE '%"+ p.getPlayer().getUniqueId() +"%'";
           stm.executeUpdate(sql_update);
           player_rank_usernames.clear();
           player_rank_uuids.clear();
           con.close();
       } catch (SQLException e) {
           System.out.println(e);
       }
   }

   public void updateIfUsernameChanged(PlayerJoinEvent p) {
       try {
           this.createConnection();
           Connection con = DriverManager.getConnection(url, user, password);
           Statement stm = con.createStatement();

           String select_sql ="UPDATE status " +
                   "SET playerName = '"+ p.getPlayer().getDisplayName() + "'"+
                   " WHERE playerUUID = '"+ p.getPlayer().getUniqueId() + "'";;
           stm.executeUpdate(select_sql);
           con.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
}