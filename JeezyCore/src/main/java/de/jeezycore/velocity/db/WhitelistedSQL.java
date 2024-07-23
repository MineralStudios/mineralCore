package de.jeezycore.velocity.db;

import de.jeezycore.velocity.utils.ArrayStorage;
import java.sql.*;

import static de.jeezycore.utils.ArrayStorage.friendsList;
import static de.jeezycore.velocity.db.hikari.HikariCP.dataSource;

public class WhitelistedSQL {

    public static Boolean whitelisted;
    public static Boolean isWhitelisted;
    public static Integer maxPlayerCount;

    public void enableWhitelisted() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdateMsg = "UPDATE velocity " +
                    "SET status = true"+
                    " WHERE actionName = 'whitelisted'";
            statement.executeUpdate(sqlUpdateMsg);
            whitelisted = true;
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


    public void disableWhitelisted() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdateMsg = "UPDATE velocity " +
                    "SET status = false"+
                    " WHERE actionName = 'whitelisted'";
            statement.executeUpdate(sqlUpdateMsg);
            whitelisted = false;
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


    public void getWhitelistedData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT status FROM velocity WHERE actionName = 'whitelisted'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                whitelisted = false;
            } else {
                do {
                    whitelisted = resultSet.getBoolean(1);
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

    public void add(String sql, String playerUUID, Boolean status) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, playerUUID);
            pstmt.setBoolean(2, status);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void remove(String sql, String playerUUID) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, playerUUID);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void isPlayerWhitelisted(String playerUUID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT whitelisted FROM whitelisted WHERE playerUUID = '"+playerUUID+"'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                isWhitelisted = false;
            } else {
                do {
                    isWhitelisted = resultSet.getBoolean(1);
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

    public void getWhitelistedPlayers() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT playerUUID FROM whitelisted";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
            } else {
                do {
                    ArrayStorage.whitelistedPlayerUUID.add(resultSet.getString(1));
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


    public void updateMaxPlayerCount(String sql, Integer getMaxPlayerCount) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, getMaxPlayerCount);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public void getMaxPlayerCount() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT maxPlayerCount FROM maxPlayers WHERE actionName = 'maxPlayerCount'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                maxPlayerCount = 0;
            } else {
                do {
                    maxPlayerCount = resultSet.getInt(1);
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

}