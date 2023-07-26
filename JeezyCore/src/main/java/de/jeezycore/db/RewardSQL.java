package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static de.jeezycore.utils.ArrayStorage.tagItems;

public class RewardSQL {
    String url;
    String user;
    String password;
    String tagNameData;
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime finalTime = currentTime.plusDays(1);
    public static String waitDuration;
    public static boolean alreadyClaimedReward;
    public static String rewardPrice;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    

    public void tagData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT * FROM tags";
            resultSet = statement.executeQuery(select_sql);
            while (resultSet.next()) {
                tagNameData = resultSet.getString(1);
                tagItems.add(tagNameData);
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

    public void rewardClaimed(Player p, String price) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql = "INSERT INTO rewards " +
                    "(UUID, playerName, claimed, time, price) " +
                    "VALUES " +
                    "('" + p.getPlayer().getUniqueId() + "', '"+p.getPlayer().getDisplayName()+"', " + "true,'" + finalTime.format(formatter) + "', '"+ price +"')";

            statement.executeUpdate(sql);
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

    public void checkIfClaimed(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT * FROM rewards WHERE UUID = '"+p.getPlayer().getUniqueId()+"'";

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                alreadyClaimedReward = resultSet.getBoolean(3);
                waitDuration = resultSet.getString(4);
                rewardPrice = resultSet.getString(5);
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

    public void nextRewardPeriod(Player p) {
        LocalDateTime dateTime_start = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        LocalDateTime dateTime_end = LocalDateTime.parse(waitDuration, formatter);

        LocalDateTime fromTemp = LocalDateTime.from(dateTime_start);
        long years = fromTemp.until(dateTime_end, ChronoUnit.YEARS);
        fromTemp = fromTemp.plusYears(years);

        long months = fromTemp.until(dateTime_end, ChronoUnit.MONTHS);
        fromTemp = fromTemp.plusMonths(months);

        long days = fromTemp.until(dateTime_end, ChronoUnit.DAYS);
        fromTemp = fromTemp.plusDays(days);

        long hours = fromTemp.until(dateTime_end, ChronoUnit.HOURS);
        fromTemp = fromTemp.plusHours(hours);

        long minutes = fromTemp.until(dateTime_end, ChronoUnit.MINUTES);
        fromTemp = fromTemp.plusMinutes(minutes);

        long seconds = fromTemp.until(dateTime_end, ChronoUnit.SECONDS);
        fromTemp = fromTemp.plusSeconds(seconds);

        long millis = fromTemp.until(dateTime_end, ChronoUnit.MILLIS);

        if (seconds < 0) {
            removeReward(p);
            return;
        }
        p.sendMessage("§7§lNext Reward: " + "§9Day(s): §7[§b"+days+"§7] | "+"§9Hour(s): §7[§b"+hours+"§7] | "+"§9Minutes: §7[§b"+minutes+"§7] | " +"§9Seconds: §7[§b"+seconds+"§7];");
    }

    public void removeReward(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql = "DELETE FROM rewards WHERE UUID = '"+p.getPlayer().getUniqueId()+"'";

            statement.executeUpdate(sql);
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