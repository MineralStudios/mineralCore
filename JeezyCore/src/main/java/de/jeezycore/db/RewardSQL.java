package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


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

    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void tagData() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM tags";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                tagNameData = rs.getString(1);
                tagItems.add(tagNameData);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rewardClaimed(Player p, String price) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            String sql = "INSERT INTO rewards " +
                    "(UUID, playerName, claimed, time, price) " +
                    "VALUES " +
                    "('" + p.getPlayer().getUniqueId() + "', '"+p.getPlayer().getDisplayName()+"', " + "true,'" + finalTime.format(formatter) + "', '"+ price +"')";

            stm.executeUpdate(sql);
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkIfClaimed(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql = "SELECT * FROM rewards WHERE UUID = '"+p.getPlayer().getUniqueId()+"'";

            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                alreadyClaimedReward = rs.getBoolean(3);
                waitDuration = rs.getString(4);
                rewardPrice = rs.getString(5);
            }
        con.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            String sql = "DELETE FROM rewards WHERE UUID = '"+p.getPlayer().getUniqueId()+"'";

            stm.executeUpdate(sql);
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}