package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class WipeSQL {

    public String url;
    public String user;
    public String password;

    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void wipeBans(String username, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker uc = new UUIDChecker();
            uc.check(username);

            String select_sql ="UPDATE punishments " +
                    "SET banned_forever = 0, ban_start = NULL, ban_end = NULL, ban_status = 0, ban_logs = NULL"+
                    " WHERE UUID = '"+ UUIDChecker.uuid+"'";;
            stm.executeUpdate(select_sql);

            p.sendMessage("§7You §2successfully §7wiped all the §4bans §7from §b"+UUIDChecker.uuidName+"§7.");

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void wipeMutes(String username, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker uc = new UUIDChecker();
            uc.check(username);

            String select_sql ="UPDATE punishments " +
                    "SET mute_forever = 0, mute_start = NULL, mute_end = NULL, mute_status = 0, mute_logs = NULL"+
                    " WHERE UUID = '"+ UUIDChecker.uuid+"'";;
            stm.executeUpdate(select_sql);

            p.sendMessage("§7You §2successfully §7wiped all the §9mutes §7from §b"+UUIDChecker.uuidName+"§7.");

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void wipeAll(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            String select_sql ="DELETE FROM punishments";
            stm.executeUpdate(select_sql);

            p.sendMessage("§7You §2successfully §7wiped all the players §cpunishments §7on your §bServer§7.");

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
