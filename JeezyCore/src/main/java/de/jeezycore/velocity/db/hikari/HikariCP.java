package de.jeezycore.velocity.db.hikari;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.jeezycore.colors.Color;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static de.jeezycore.velocity.config.JeezyConfig.toml;

public class HikariCP {


    public static HikariDataSource dataSource = null;


    public void start() {
        startHikariPool();
        createDB();
        createTable();
    }

    public void startHikariPool() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mariadb://"+toml.getString("ip")+":"+toml.getLong("mysql-port").toString()+"/"+toml.getString("database"));
        config.setDriverClassName(org.mariadb.jdbc.Driver.class.getName());
        config.setUsername(toml.getString("user"));
        config.setPassword(toml.getString("password"));
        config.setMaximumPoolSize(25);
        config.setConnectionTimeout(300000);
        config.setLeakDetectionThreshold(300000);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void createDB() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            String createDB = "CREATE DATABASE IF NOT EXISTS mineralvelocity";
            statement.executeUpdate(createDB);
        } catch (Exception e) {
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

    public void createDefaultInserts() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            String maintenance_tableInserts = "INSERT INTO velocity (actionName) VALUES ('maintenance')";
            String whitelist_tableInsert = "INSERT INTO velocity (actionName) VALUES ('whitelisted')";

            statement.executeUpdate(maintenance_tableInserts);
            statement.executeUpdate(whitelist_tableInsert);
        } catch (SQLException e) {
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTable() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            String maintenance_table = "CREATE TABLE IF NOT EXISTS velocity " +
                    " (actionName VARCHAR(255), " +
                    " status boolean DEFAULT FALSE, " +
                    " PRIMARY KEY ( actionName ))";



            statement.executeUpdate(maintenance_table);
            this.createDefaultInserts();

            if (connection.isValid(20)) {
                System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" connected to database."+Color.RESET);
                System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment] "+Color.GREEN_BOLD+"Successfully"+Color.CYAN+" created"+Color.YELLOW_BOLD+" DB & Tables"+Color.CYAN+"."+Color.RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.RED_BOLD+" Something went wrong when tried to connect to your database."+Color.RESET);
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