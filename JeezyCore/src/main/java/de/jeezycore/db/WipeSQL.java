package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

import static de.jeezycore.db.hikari.HikariCP.dataSource;

public class WipeSQL {

    public String url;
    public String user;
    public String password;
    

    public void wipeBans(String username, Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            UUIDChecker uc = new UUIDChecker();
            uc.check(username);

            String select_sql ="UPDATE punishments " +
                    "SET banned_forever = 0, ban_start = NULL, ban_end = NULL, ban_status = 0, ban_logs = NULL"+
                    " WHERE UUID = '"+ UUIDChecker.uuid+"'";;
            statement.executeUpdate(select_sql);

            p.sendMessage("§7You §2successfully §7wiped all the §4bans §7from §b"+UUIDChecker.uuidName+"§7.");
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

    public void wipeMutes(String username, Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            UUIDChecker uc = new UUIDChecker();
            uc.check(username);

            String select_sql ="UPDATE punishments " +
                    "SET mute_forever = 0, mute_start = NULL, mute_end = NULL, mute_status = 0, mute_logs = NULL"+
                    " WHERE UUID = '"+ UUIDChecker.uuid+"'";;
            statement.executeUpdate(select_sql);

            p.sendMessage("§7You §2successfully §7wiped all the §9mutes §7from §b"+UUIDChecker.uuidName+"§7.");
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

    public void wipeAll(Player p) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String select_sql ="DELETE FROM punishments";
            statement.executeUpdate(select_sql);

            p.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §7You §2successfully §9wiped§7 all the players §cpunishments §7on your §9server§7.");
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