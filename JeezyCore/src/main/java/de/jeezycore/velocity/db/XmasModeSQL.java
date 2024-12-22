package de.jeezycore.velocity.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static de.jeezycore.velocity.db.hikari.HikariCP.dataSource;

public class XmasModeSQL {

    public static Boolean xmasMode;

    public void enableXmasMode() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdateMsg = "UPDATE xmas " +
                    "SET status = true"+
                    " WHERE actionName = 'xmas'";
            statement.executeUpdate(sqlUpdateMsg);
            xmasMode = true;
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


    public void disableXmasMode() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdateMsg = "UPDATE xmas " +
                    "SET status = false"+
                    " WHERE actionName = 'xmas'";
            statement.executeUpdate(sqlUpdateMsg);
            xmasMode = false;
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

    public void getXmasData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT status FROM xmas WHERE actionName = 'xmas'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                xmasMode = false;
            } else {
                do {
                    xmasMode = resultSet.getBoolean(1);
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
