package de.jeezycore.db;

import de.jeezycore.utils.ArrayStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static java.time.temporal.ChronoUnit.MINUTES;

public class PlayTimeSQL {

    String playTime;

    String playTimeStart;

    String playTimeEnd;

    int playTimeResult;



    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void playTimeJoin(PlayerJoinEvent join) {
        ArrayStorage.playTimeHashMap.put(join.getPlayer().getUniqueId(), LocalDateTime.now());
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String select_sql ="INSERT INTO playtime" +
                    "(playerName, playerUUID, playtime_start) " +
                    "VALUES ('"+ join.getPlayer().getDisplayName() + "', '"+ join.getPlayer().getUniqueId() +"', "+
                    "'"+ LocalDateTime.now().format(formatter) + "')";
            statement.executeUpdate(select_sql);
        } catch (SQLException e) {
            alreadyGotPlayTime(join);
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void alreadyGotPlayTime(PlayerJoinEvent join) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String select_sql ="UPDATE playtime " +
                    "SET playtime_start = '"+LocalDateTime.now().format(formatter)+
                    "' WHERE playerUUID = '"+ join.getPlayer().getUniqueId() + "'";
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

    public void playTimeQuit(PlayerQuitEvent quit) {
        queryPlaytime(quit.getPlayer().getUniqueId());
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();


            if (playTime != null) {
                playTimeResult = getPlayTime() + Integer.parseInt(playTime);
            } else {
                playTimeResult = getPlayTime();
            }

            String select_sql ="UPDATE playtime " +
                    "SET playTime = '"+playTimeResult+
                    "', playtime_end = '"+LocalDateTime.now().format(formatter)+
                    "' WHERE playerUUID = '"+ quit.getPlayer().getUniqueId() + "'";
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

    private void queryPlaytime(UUID get_UUID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT * FROM playtime WHERE playerUUID = '"+get_UUID+"'";
            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                playTime = null;
                playTimeStart = null;
            } else {
                do {
                    playTime = resultSet.getString("playTime");
                    playTimeStart = resultSet.getString("playtime_start");
                    playTimeEnd = resultSet.getString("playtime_end");
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


    private int getPlayTime() {
        LocalDateTime dateTime_start = LocalDateTime.parse(playTimeStart, formatter);
        LocalDateTime dateTime_end = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        return (int) ChronoUnit.SECONDS.between(dateTime_start, dateTime_end);
    }


    private void playTimeCalculation(Player p) {

        LocalDateTime dateTime_start = LocalDateTime.parse(playTimeStart, formatter);
        LocalDateTime dateTime_end = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);

        LocalDateTime fromTemp = LocalDateTime.from(dateTime_start);

        if (playTime != null) {
            fromTemp = LocalDateTime.from(ArrayStorage.playTimeHashMap.get(p.getUniqueId())).minusSeconds(Long.parseLong(playTime));

        }

        long hours = fromTemp.until(dateTime_end, ChronoUnit.HOURS);
        fromTemp = fromTemp.plusHours(hours);

        long minutes = fromTemp.until(dateTime_end, MINUTES);
        fromTemp = fromTemp.plusMinutes(minutes);

        long seconds = fromTemp.until(dateTime_end, ChronoUnit.SECONDS);
        fromTemp = fromTemp.plusSeconds(seconds);


        p.sendMessage("§7[§9PlayTime§7]"+" §fHour§7(§9s§7): §7[§9"+hours+"§7] §f| "+"§fMinute§7(§9s§7): §7[§9"+minutes+"§7] §f| " +"§fSecond§7(§9s§7): §7[§9"+seconds+"§7]");

    }

    public void showPlayTime(Player p) {
        queryPlaytime(p.getUniqueId());
        playTimeCalculation(p);
    }
}