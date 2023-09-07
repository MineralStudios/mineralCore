package de.jeezycore.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static de.jeezycore.db.hikari.HikariCP.dataSource;

public class MsgSQL {

    public String url;
    public String user;
    public String password;

    public String playerUUID;
    public String playerName;
    public String replyToName;
    public String replyToUUID;


    public void setup(UUID uuid, String playerName, String senderName, UUID senderUUID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM msg WHERE playerUUID = '"+uuid+"'";
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                playerUUID = null;
            } else {
                do {
                    playerUUID = resultSet.getString("playerUUID");
                } while (resultSet.next());
            }

            if (playerUUID == null) {
                String sql_insert ="INSERT INTO msg" +
                        "(playerName, playerUUID, replyToName, replyToUUID) " +
                        "VALUES ('"+playerName+"', '"+uuid+"', '" +
                        senderName+"', '"+senderUUID+"')";

                statement.executeUpdate(sql_insert);
            } else {
                pushMsg(uuid, senderName, senderUUID);
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


    private void pushMsg(UUID uuid, String playerName, UUID playerNameUUID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdateMsg = "UPDATE msg " +
                    "SET replyToName = '"+playerName+
                    "', replyToUUID = '"+playerNameUUID+
                    "' WHERE playerUUID = '"+uuid+"'";
            statement.executeUpdate(sqlUpdateMsg);
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

    public void getReplyData(UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM msg WHERE playerUUID = '"+uuid+"'";
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                playerUUID = null;
            } else {
                do {
                    playerName = resultSet.getString("playerName");
                    playerUUID = resultSet.getString("playerUUID");
                    replyToName = resultSet.getString("replyToName");
                    replyToUUID = resultSet.getString("replyToUUID");
                } while (resultSet.next());
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

    public void quit(UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdateMsg = "DELETE FROM msg " +
                    "WHERE playerUUID = '"+uuid+"' OR replyToUUID = '"+uuid+"'";
            statement.executeUpdate(sqlUpdateMsg);
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