package de.jeezycore.velocity.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static de.jeezycore.velocity.db.hikari.HikariCP.dataSource;

public class WhitelistedSQL {

    public static Boolean whitelisted;


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

}
