package de.jeezycore.db;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.sql.*;

import static de.jeezycore.db.hikari.HikariCP.dataSource;


public class PlayersSQL {

    public String url;
    public String user;
    public String password;


    public void alreadyJoined(PlayerJoinEvent join) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String select_sql ="UPDATE players " +
                    "SET online = true" +
                    " WHERE playerUUID = '"+ join.getPlayer().getUniqueId() + "'";;
            statement.executeUpdate(select_sql);
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void firstJoined(PlayerJoinEvent join) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            String select_sql ="INSERT INTO players" +
                    "(playerName, playerUUID, firstJoined, online) " +
                    "VALUES ('"+ join.getPlayer().getDisplayName() + "', '"+ join.getPlayer().getUniqueId() +"', "+
                    "'"+ timestamp + "', true)";
            statement.executeUpdate(select_sql);
        } catch (SQLException e) {
            alreadyJoined(join);
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

   public void lastSeen(PlayerQuitEvent quit) {
       Connection connection = null;
       Statement statement = null;
       ResultSet resultSet = null;
       try {
           connection = dataSource.getConnection();
           statement = connection.createStatement();

           Timestamp timestamp = new Timestamp(System.currentTimeMillis());

           String select_sql ="UPDATE players " +
                   "SET lastSeen = '"+ timestamp + "', online = false" +
                   " WHERE playerUUID = '"+ quit.getPlayer().getUniqueId() + "'";;
           statement.executeUpdate(select_sql);
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           try {
               statement.close();
               connection.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
   }

   public void checkIfUsernameChanged(PlayerJoinEvent p) {
       Connection connection = null;
       Statement statement = null;
       ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String playerName;

            String select_sql = "SELECT playerName FROM players WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";

            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                playerName = null;
            }  else {
           do {
               playerName = resultSet.getString(1);
           } while (resultSet.next());
            }
            if (!p.getPlayer().getDisplayName().equalsIgnoreCase(playerName)) {
                updatePlayersTable(p);
                updatePlayTimeTable(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
   }

   public void updatePlayersTable(PlayerJoinEvent p) {
       Connection connection = null;
       Statement statement = null;
       ResultSet resultSet = null;
       try {
           connection = dataSource.getConnection();
           statement = connection.createStatement();

           String select_sql ="UPDATE players " +
                   "SET playerName = '"+ p.getPlayer().getDisplayName() + "'"+
                   " WHERE playerUUID = '"+ p.getPlayer().getUniqueId() + "'";;
           statement.executeUpdate(select_sql);
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           try {
               statement.close();
               connection.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
   }

    public void updatePlayTimeTable(PlayerJoinEvent p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String select_sql ="UPDATE playtime " +
                    "SET playerName = '"+ p.getPlayer().getDisplayName() + "'"+
                    " WHERE playerUUID = '"+ p.getPlayer().getUniqueId() + "'";;
            statement.executeUpdate(select_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}