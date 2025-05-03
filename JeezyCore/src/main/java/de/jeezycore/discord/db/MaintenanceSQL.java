package de.jeezycore.discord.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static de.jeezycore.discord.db.HikariCP.dataSource;

public class MaintenanceSQL {

   public static Boolean maintenance;


    public void enableMaintenance() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdateMsg = "UPDATE velocity " +
                    "SET status = true"+
                    " WHERE actionName = 'maintenance'";
            statement.executeUpdate(sqlUpdateMsg);
            maintenance = true;
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


    public void disableMaintenance() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdateMsg = "UPDATE velocity " +
                    "SET status = false"+
                    " WHERE actionName = 'maintenance'";
            statement.executeUpdate(sqlUpdateMsg);
            maintenance = false;
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


    public void getMaintenanceData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT status FROM velocity WHERE actionName = 'maintenance'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                maintenance = false;
            } else {
                do {
                    maintenance = resultSet.getBoolean(1);
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