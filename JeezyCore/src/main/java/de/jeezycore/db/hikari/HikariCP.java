package de.jeezycore.db.hikari;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.jeezycore.colors.Color;
import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;

import java.sql.*;

public class HikariCP {

    private static final MemorySection db = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

    public static HikariDataSource dataSource = null;

    public void start() {
        createDB();
        createTable();
        startHikariPool();
    }

    public void startHikariPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://"+db.get("ip")+":"+db.get("mysql-port")+"/"+db.get("database"));
        config.setUsername((String) db.get("user"));
        config.setPassword((String) db.get("password"));
        config.setMaximumPoolSize(25);
        config.setConnectionTimeout(300000);
        config.setConnectionTimeout(120000);
        config.setLeakDetectionThreshold(300000);

        dataSource = new HikariDataSource(config);
    }

    private void createDB() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://"+db.get("ip")+":"+db.get("mysql-port")+"/", (String) db.get("user"), (String) db.get("password"));
            statement = connection.createStatement();
            String createDB = "CREATE DATABASE IF NOT EXISTS jeezydevelopment";
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

    public void createTable() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://"+db.get("ip")+":"+db.get("mysql-port")+"/"+db.get("database"), (String) db.get("user"), (String) db.get("password"));
            statement = connection.createStatement();
            String ranks_table = "CREATE TABLE IF NOT EXISTS ranks " +
                    " (rankName VARCHAR(255), " +
                    " rankRGB VARCHAR(50), " +
                    " rankColor VARCHAR(10), " +
                    " rankPriority INT(3), " +
                    " rankPerms longtext, " +
                    " staffRank boolean DEFAULT FALSE," +
                    " PRIMARY KEY ( rankName ))";

            String players_table = "CREATE TABLE IF NOT EXISTS players " +
                    " (playerName VARCHAR(255), " +
                    " playerUUID VARCHAR(255), " +
                    " minerals longtext, " +
                    " rank VARCHAR(255), " +
                    " tag VARCHAR(255), " +
                    " chatColor VARCHAR(255), " +
                    " firstJoined VARCHAR(255), " +
                    " lastSeen VARCHAR(255), " +
                    " online boolean DEFAULT FALSE," +
                    " PRIMARY KEY ( playerUUID ))";

            String items_table = "CREATE TABLE IF NOT EXISTS items " +
                    " (playerName VARCHAR(255), " +
                    " playerUUID VARCHAR(255), " +
                    " rankForever boolean, " +
                    " rankStart VARCHAR(255), " +
                    " rankEnd VARCHAR(255), " +
                    " ownedTags longtext, " +
                    " ownedChatColors VARCHAR(255), " +
                    " PRIMARY KEY ( playerUUID ))";

            String punishments_table = "CREATE TABLE IF NOT EXISTS punishments " +
                    " (playerName VARCHAR(255), " +
                    " UUID VARCHAR(255), " +
                    " banned_forever boolean, " +
                    " mute_forever boolean, " +
                    " ban_start longtext, " +
                    " ban_end longtext, " +
                    " ban_status boolean, " +
                    " mute_start longtext, " +
                    " mute_end longtext, " +
                    " mute_status boolean, " +
                    " ban_logs longtext, " +
                    " mute_logs longtext, "+
                    " PRIMARY KEY ( UUID ))";

            String tags_table = "CREATE TABLE IF NOT EXISTS tags " +
                    " (tagName VARCHAR(255), " +
                    " tagCategory VARCHAR(255), " +
                    " tagDesign VARCHAR(255), " +
                    " tagPriority INT(3), " +
                    " PRIMARY KEY ( tagName ))";

            String chatColors_table = "CREATE TABLE IF NOT EXISTS chatColors " +
                    " (colorName VARCHAR(255), " +
                    " color VARCHAR(50), " +
                    " colorRGB VARCHAR(50), " +
                    " colorPriority INT(3), " +
                    " PRIMARY KEY ( colorName ))";

            /*
            String reward_table = "CREATE TABLE IF NOT EXISTS rewards " +
                    " (UUID VARCHAR(255), " +
                    " playerName VARCHAR(255), " +
                    " claimed boolean, " +
                    " time longtext, " +
                    " price VARCHAR(255), " +
                    " PRIMARY KEY ( UUID ))";
             */

            /*
            String minerals_table = "CREATE TABLE IF NOT EXISTS minerals " +
                    " (ServerName VARCHAR(255), " +
                    " minerals_data longtext, " +
                    " PRIMARY KEY ( ServerName ))";

             */
            String settings_table = "CREATE TABLE IF NOT EXISTS settings " +
                    " (playerName VARCHAR(255), " +
                    " playerUUID VARCHAR(255), " +
                    " ignoredPlayerList longtext, " +
                    " msg boolean, " +
                    " PRIMARY KEY ( playerUUID ))";

            statement.executeUpdate(ranks_table);
            statement.executeUpdate(players_table);
            statement.executeUpdate(items_table);
            statement.executeUpdate(punishments_table);
            statement.executeUpdate(tags_table);
            statement.executeUpdate(chatColors_table);
            // stm.executeUpdate(reward_table);
            //stm.executeUpdate(minerals_table);
            statement.executeUpdate(settings_table);

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