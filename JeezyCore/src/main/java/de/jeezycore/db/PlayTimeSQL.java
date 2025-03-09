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

public class PlayTimeSQL {

    String playTime;

    int playTimeResult;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void playTimeJoin(PlayerJoinEvent join) {
        ArrayStorage.playTimeHashMap.put(join.getPlayer().getUniqueId(), LocalDateTime.now());
    }

    public void playTimeQuit(PlayerQuitEvent quit) {
        queryPlaytime(quit.getPlayer().getUniqueId());
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();


            if (playTime != null) {
                playTimeResult = getPlayTime(quit.getPlayer().getUniqueId()) + Integer.parseInt(playTime);
            } else {
                playTimeResult = getPlayTime(quit.getPlayer().getUniqueId());
            }

            String select_sql ="UPDATE players " +
                    "SET playTime = '"+playTimeResult+
                    "' WHERE playerUUID = '"+ quit.getPlayer().getUniqueId() + "'";
            statement.executeUpdate(select_sql);
            ArrayStorage.playTimeHashMap.remove(quit.getPlayer().getUniqueId());
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
            String select_sql = "SELECT * FROM players WHERE playerUUID = '"+get_UUID+"'";
            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                playTime = null;
            } else {
                do {
                    playTime = resultSet.getString("playTime");
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


    private int getPlayTime(UUID uuid) {
        LocalDateTime dateTime_start = LocalDateTime.parse(ArrayStorage.playTimeHashMap.get(uuid).format(formatter), formatter);
        LocalDateTime dateTime_end = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        return (int) ChronoUnit.SECONDS.between(dateTime_start, dateTime_end);
    }


    private void playTimeCalculation(Player p) {

        LocalDateTime startTime = ArrayStorage.playTimeHashMap.get(p.getUniqueId());
        LocalDateTime endTime = LocalDateTime.now();

        if (playTime != null) {
            startTime = startTime.minusSeconds(Long.parseLong(playTime));
        }

        long hours = startTime.until(endTime, ChronoUnit.HOURS);
        long minutes = startTime.until(endTime, ChronoUnit.MINUTES) % 60;
        long seconds = startTime.until(endTime, ChronoUnit.SECONDS) % 60;

        p.sendMessage(String.format("§7[§9PlayTime§7] §fHour§7(§9s§7): §7[§9%d§7] §f| §fMinute§7(§9s§7): §7[§9%d§7] §f| §fSecond§7(§9s§7): §7[§9%d§7]",
                hours, minutes, seconds));

    }

    public void showPlayTime(Player p) {
        queryPlaytime(p.getUniqueId());
        playTimeCalculation(p);
    }
}