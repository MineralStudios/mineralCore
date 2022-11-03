package de.jeezycore.db;

import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class BanSQL {
    public String url;
    public String user;
    public String password;

    public static boolean ban_forever;

    public static String punishment_UUID;

    private String punishment_logs;

    private final ArrayList<String> punishment_logArray = new ArrayList<>();

    private LocalDateTime currentTime;

    private LocalDateTime updatedTime;

    private DateTimeFormatter formatter;

    private String ban_start;
    private String ban_end;

    private void createConnection() {
        File file = new File("/home/jeffrey/IdeaProjects/JeezyCore/JeezyCore/src/main/java/database.yml");
        FileConfiguration db = YamlConfiguration.loadConfiguration(file);
        MemorySection mc = (MemorySection) db.get("MYSQL");

        url = "jdbc:mysql://localhost:3306/" + mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void ban(String username, String input, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker uc = new UUIDChecker();
            uc.check(username);
            banData(UUID.fromString(UUIDChecker.uuid));
            JSONObject json_o = new JSONObject();

            json_o.put("banned by", p.getPlayer().getDisplayName());
            json_o.put("time", "forever");
            json_o.put("reason", input);
            ArrayStorage.punishment_log.add(json_o);

            try {
                Bukkit.getServer().getPlayer(UUID.fromString(UUIDChecker.uuid)).kickPlayer("§4You are permanently banned from JeezyDevelopment.\n\n" +
                        "§6Reason: §c" + input + "§7.\n\n" +
                        "§7If you feel this ban has been unjustified, appeal on our discord at\n §bjeezydevelopment.com§7.");
            } catch (Exception e) {

            }


            String sql = "INSERT INTO punishments " +
                    "(UUID, banned_forever, ban_start, ban_end mute_time, punishment_log) " +
                    "VALUES " +
                    "('" + UUIDChecker.uuid + "', true, " + "NULL, NULL, NULL, '" + ArrayStorage.punishment_log + "')";

            System.out.println(sql);
            if (punishment_logs == null || punishment_UUID == null) {
                stm.executeUpdate(sql);
            } else {
                banUpdate(username, input, p);
            }
            ArrayStorage.punishment_log.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void banUpdate(String username, String input, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);

            banData(UUID.fromString(UUIDChecker.uuid));

            if (ban_forever) {
                p.sendMessage("§b" + username + " §7has been already §4banned.");
                return;
            } else {
                p.sendMessage("§7You §asuccessfully §7banned §b" + username + ".");
            }

            String sql = "UPDATE punishments " +
                    "SET banned_forever = true" +
                    " WHERE UUID = '" + UUIDChecker.uuid + "'";
            stm.executeUpdate(sql);
            stm.close();

            punishment_logUpdate(username, input, p);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tempBanDurationCalculate() {
        System.out.println(ban_start);
        System.out.println(ban_end);

        LocalDateTime dateTime_start = LocalDateTime.parse(ban_start, formatter);
        LocalDateTime dateTime_end = LocalDateTime.parse(ban_end, formatter);

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

        System.out.println("From = " + dateTime_start);
        System.out.println("To   = " + dateTime_end);
        System.out.printf("The difference is %s years, %s months, %s days, " +
                        "%s hours, %s minutes, %s seconds, %s millis",
                years, months, days, hours, minutes, seconds, millis);
        // System.out.println("Day(s): ["+diffInDays+"]; "+"Hour(s): ["+diffInHours+"]; "+"Minutes: ["+diffInMinutes+"];");

    }


    public void tempBanCalculate(String time) {
        currentTime = LocalDateTime.now();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (time.contains("d")) {
            updatedTime = currentTime.plusDays(Integer.parseInt(time.replace("d", "")));
        } else if (time.contains("h")) {
            updatedTime = currentTime.plusHours(Integer.parseInt(time.replace("h", "")));
        } else if (time.contains("m")) {
            updatedTime = currentTime.plusMinutes(Integer.parseInt(time.replace("m", "")));
        } else if (time.contains("s")) {
            updatedTime = currentTime.plusSeconds(Integer.parseInt(time.replace("s", "")));
        }
    }

    public void tempBan(String username, String time, String reason , Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker uc = new UUIDChecker();
            uc.check(username);
            tempBanCalculate(time);
            banData(UUID.fromString(UUIDChecker.uuid));
            JSONObject json_o = new JSONObject();

            json_o.put("banned by", p.getPlayer().getDisplayName());
            json_o.put("ban_start", currentTime.format(formatter));
            json_o.put("ban_end", updatedTime.format(formatter));
            json_o.put("reason", reason);
            ArrayStorage.punishment_log.add(json_o);

            try {
                Bukkit.getServer().getPlayer(UUID.fromString(UUIDChecker.uuid)).kickPlayer("§4You are temporarily banned from §bJeezyDevelopment.\n\n" +
                        "§aBan time: §4"+time+"\n\n" +
                        "§6Reason: §c" +reason+"§7.\n\n"+
                        "§7If you feel this ban has been unjustified, appeal on our discord at\n §bjeezydevelopment.com§7.");
            } catch (Exception e) {

            }

            String sql = "INSERT INTO punishments " +
                    "(UUID, banned_forever, ban_start, ban_end, mute_time, punishment_log) " +
                    "VALUES " +
                    "('"+UUIDChecker.uuid+"', false,'"+currentTime.format(formatter)+"', '"+updatedTime.format(formatter)+"', NULL, "+"'"+ArrayStorage.punishment_log+"')";



            if (punishment_logs == null || punishment_UUID == null) {
                stm.executeUpdate(sql);
            } else {
                tempBanUpdate(username, reason, p);
            }

            ArrayStorage.punishment_log.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tempBanUpdate(String username, String input, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);

            banData(UUID.fromString(UUIDChecker.uuid));

            if (ban_forever) {
                p.sendMessage("§b"+username+" §7has been already §4banned.");
                return;
            } else {
                p.sendMessage("§7You §asuccessfully §7banned §b"+username+".");
            }

            String sql = "UPDATE punishments " +
                    "SET banned_forever = false, ban_start = '"+currentTime.format(formatter)+"', ban_end = '"+updatedTime.format(formatter)+"'"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";
            stm.executeUpdate(sql);
            stm.close();

            punishment_logUpdate(username, input, p);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void unban(String username, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);

            unbanData(UUID.fromString(UUIDChecker.uuid));

            if (punishment_UUID == null || !ban_forever) {
                p.sendMessage("§7The player §b"+username+" §7isn't §4banned.");
                return;
            } else {
                p.sendMessage("§7You §asuccessfully §7unbanned §b"+username);
            }

            String sql = "UPDATE punishments " +
                    "SET banned_forever = false"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";
            stm.executeUpdate(sql);
            stm.close();

        } catch (Exception e) {
        }
    }

    public void banData(UUID get_UUID) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM punishments WHERE UUID = '" +get_UUID.toString()+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                punishment_UUID = rs.getString(1);
                ban_forever = rs.getBoolean(2);
                ban_start = rs.getString(3);
                ban_end = rs.getString(4);
                punishment_logs = rs.getString(6);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unbanData(UUID get_UUID) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM punishments WHERE UUID = '" + get_UUID.toString() + "'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                punishment_UUID = rs.getString(1);
                ban_forever = rs.getBoolean(2);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void punishment_logUpdate(String username, String input, Player p) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);
            JSONObject json_o = new JSONObject();

            banData(UUID.fromString(UUIDChecker.uuid));
            String replace_punishment_logs = punishment_logs.replace("[", "").replace("]", "");

            punishment_logArray.add(replace_punishment_logs);

            json_o.put("banned by", p.getPlayer().getDisplayName());
            json_o.put("time", "forever");
            json_o.put("reason", input);

            punishment_logArray.add(json_o.toString());

            System.out.println(json_o);
            System.out.println(replace_punishment_logs);
            System.out.println(punishment_logArray);

            String sql = "UPDATE punishments " +
                    "SET punishment_log = '"+punishment_logArray+"'"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";
            stm.executeUpdate(sql);
            stm.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}