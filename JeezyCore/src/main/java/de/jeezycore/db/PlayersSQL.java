package de.jeezycore.db;

import de.jeezycore.db.cache.PlayersCache;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.sql.*;
import java.util.UUID;
import static de.jeezycore.db.hikari.HikariCP.dataSource;


public class PlayersSQL {

    String playerUUID;
    String rankColor;
    String rank;
    String tagName;
    String tagDesign;
    String chatColor;
    String playTime;
    boolean online;

    public void getAllPlayerData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            statement = connection.createStatement();
            String sql = "SELECT \n" +
                    "    players.playerName, \n" +
                    "    players.playerUUID, \n" +
                    "    players.minerals,\n" +
                    "    ranks.rankColor,\n" +
                    "    players.rank,\n" +
                    "    players.tag,\n" +
                    "    tags.tagDesign,\n" +
                    "    chatColors.color,\n" +
                    "    players.chatColor,\n" +
                    "    players.playTime,\n" +
                    "    players.firstJoined,\n" +
                    "    players.lastSeen,\n" +
                    "    players.online\n" +
                    "FROM players\n" +
                    "LEFT JOIN chatColors ON players.chatColor = chatColors.colorName\n" +
                    "LEFT JOIN ranks ON players.rank = ranks.rankName\n" +
                    "LEFT JOIN tags ON players.tag = tags.tagName\n" +
                    "WHERE NOT (\n" +
                    "    players.rank IS NULL AND\n" +
                    "    players.tag IS NULL AND\n" +
                    "    players.chatColor IS NULL AND\n" +
                    "    players.playTime IS NULL\n" +
                    ")";
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                playerUUID = null;
                rank = null;
                tagDesign = null;
                chatColor = null;
                playTime = null;
                online = false;
            } else {
                do {
                    playerUUID = resultSet.getString("playerUUID");
                    rankColor = resultSet.getString("rankColor");
                    rank = resultSet.getString("rank");
                    tagName = resultSet.getString("tag");
                    tagDesign = resultSet.getString("tagDesign");
                    chatColor = resultSet.getString("color");
                    playTime = resultSet.getString("playTime");
                    online = resultSet.getBoolean("online");

                    PlayersCache.getInstance().savePlayerData(UUID.fromString(playerUUID), rankColor, rank, tagName, tagDesign, chatColor, playTime, online);
                } while (resultSet.next());
                PlayersCache.getInstance().saveAllPlayerData();
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
}