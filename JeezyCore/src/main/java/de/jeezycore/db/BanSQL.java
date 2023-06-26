package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.discord.messages.ban.RealtimeBan;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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

    public static boolean ban_status;

    public static String punishment_UUID;

    private String ban_logs;

    private final ArrayList<String> ban_logsArray = new ArrayList<>();

    private final LocalDateTime currentTime = LocalDateTime.now();;

    private LocalDateTime updatedTime;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String ban_start;
    public String ban_end;

    private JSONObject json_o = new JSONObject();

    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void ban(String username, String input, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            RealtimeBan discord = new RealtimeBan();
            UUIDChecker uc = new UUIDChecker();
            uc.check(username);
            banData(UUID.fromString(UUIDChecker.uuid));

            json_o.put("banned by", p.getPlayer().getDisplayName());
            json_o.put("ban_start", currentTime.format(formatter));
            json_o.put("ban_end", "forever");
            json_o.put("reason", input);
            ArrayStorage.ban_logs.add(json_o);

            try {
                Bukkit.getServer().getPlayer(UUID.fromString(UUIDChecker.uuid)).kickPlayer("§4You are permanently banned from JeezyDevelopment.\n\n" +
                        "§6Reason: §c" + input + "\n\n" +
                        "§7If you feel this ban has been unjustified, appeal on our discord at\n §bjeezydevelopment.com§7.");
            } catch (Exception e) {

            }

            String sql = "INSERT INTO punishments " +
                    "(UUID, banned_forever, ban_start, ban_end, ban_status, ban_logs) " +
                    "VALUES " +
                    "('" + UUIDChecker.uuid + "', true, " + "NULL, NULL, true, '" + ArrayStorage.ban_logs + "')";

            System.out.println(sql);
            if (punishment_UUID == null && ban_logs == null) {
                stm.executeUpdate(sql);
            } else {
                banUpdate(username, input, p);
            }
            ArrayStorage.ban_logs.clear();
            discord.realtimeChatOnBan(UUID.fromString(UUIDChecker.uuid), username, p.getDisplayName(), input);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void banConsole(String username, String input, CommandSender sender) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            RealtimeBan discord = new RealtimeBan();
            UUIDChecker uc = new UUIDChecker();
            uc.check(username);
            banData(UUID.fromString(UUIDChecker.uuid));

            json_o.put("banned by", "Console");
            json_o.put("ban_start", currentTime.format(formatter));
            json_o.put("ban_end", "forever");
            json_o.put("reason", input);
            ArrayStorage.ban_logs.add(json_o);

            try {
                Bukkit.getServer().getPlayer(UUID.fromString(UUIDChecker.uuid)).kickPlayer("§4You are permanently banned from JeezyDevelopment.\n\n" +
                        "§6Reason: §c" + input + "\n\n" +
                        "§7If you feel this ban has been unjustified, appeal on our discord at\n §bjeezydevelopment.com§7.");
            } catch (Exception e) {

            }

            String sql = "INSERT INTO punishments " +
                    "(UUID, banned_forever, ban_start, ban_end, ban_status, ban_logs) " +
                    "VALUES " +
                    "('" + UUIDChecker.uuid + "', true, " + "NULL, NULL, true, '" + ArrayStorage.ban_logs + "')";

            System.out.println(sql);
            if (punishment_UUID == null && ban_logs == null) {
                stm.executeUpdate(sql);
            } else {
                banUpdateConsole(username, input, sender);
            }
            ArrayStorage.ban_logs.clear();
            discord.realtimeChatOnBan(UUID.fromString(UUIDChecker.uuid), username, "Console", input);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void banUpdateConsole(String username, String input, CommandSender sender) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);
            banData(UUID.fromString(UUIDChecker.uuid));


            json_o.put("banned by", "Console");
            json_o.put("ban_start", currentTime.format(formatter));
            json_o.put("ban_end", "forever");
            json_o.put("reason", input);
            ArrayStorage.ban_logs.add(json_o);

            if (ban_forever) {
                sender.sendMessage("§b" + username + " §7has been already §4banned§7.");
                return;
            } else {
                sender.sendMessage("§7You §asuccessfully §7banned §b" + username + "§7.");
            }

            String sql = "UPDATE punishments " +
                    "SET banned_forever = true, ban_status = true" +
                    " WHERE UUID = '" + UUIDChecker.uuid + "'";
            stm.executeUpdate(sql);
            stm.close();

            ban_logsUpdate(username);

        } catch (Exception e) {
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


            json_o.put("banned by", p.getPlayer().getDisplayName());
            json_o.put("ban_start", currentTime.format(formatter));
            json_o.put("ban_end", "forever");
            json_o.put("reason", input);
            ArrayStorage.ban_logs.add(json_o);

            if (ban_forever) {
                p.sendMessage("§b" + username + " §7has been already §4banned§7.");
                return;
            } else {
                p.sendMessage("§7You §asuccessfully §7banned §b" + username + "§7.");
            }

            String sql = "UPDATE punishments " +
                    "SET banned_forever = true, ban_status = true" +
                    " WHERE UUID = '" + UUIDChecker.uuid + "'";
            stm.executeUpdate(sql);
            stm.close();

            ban_logsUpdate(username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tempBanDurationCalculate(Player p) {
        System.out.println(ban_start);
        System.out.println(ban_end);

        LocalDateTime dateTime_start = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
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

        if (seconds < 0) {
            tempBanAutomaticUnban(p.getPlayer());
            return;
        }

        p.kickPlayer("§4You are temporarily banned from §bJeezyDevelopment.\n\n" +
                "§cBan duration: " + "§cDay(s): §7[§b"+days+"§7] | "+"§cHour(s): §7[§b"+hours+"§7] | "+"§cMinutes: §7[§b"+minutes+"§7] | " +"§cSeconds: §7[§b"+seconds+"§7];\n\n" +
                "§7If you feel this ban is unjustified, appeal on our discord at\n §bjeezydevelopment.com§7.");
    }

    private void tempBanAutomaticUnban(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(p.getPlayer().getDisplayName());

            String sql = "UPDATE punishments " +
                    "SET banned_forever = false, ban_start = NULL, ban_end = NULL, ban_status = false"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";

            stm.executeUpdate(sql);
            stm.close();
        } catch (Exception e) {
        }
    }


    public void tempBanCalculate(String time) {
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

            RealtimeBan discord = new RealtimeBan();
            UUIDChecker uc = new UUIDChecker();
            uc.check(username);
            tempBanCalculate(time);
            banData(UUID.fromString(UUIDChecker.uuid));

            json_o.put("banned by", p.getPlayer().getDisplayName());
            json_o.put("ban_start", currentTime.format(formatter));
            json_o.put("ban_end", updatedTime.format(formatter));
            json_o.put("reason", reason);
            ArrayStorage.ban_logs.add(json_o);

            try {
                Bukkit.getServer().getPlayer(UUID.fromString(UUIDChecker.uuid)).kickPlayer("§4You are temporarily banned from §bJeezyDevelopment.\n\n" +
                        "§aBan time: §4"+time+"\n\n" +
                        "§6Reason: §c" +reason+"\n\n"+
                        "§7If you feel this ban has been unjustified, appeal on our discord at\n §bjeezydevelopment.com§7.");
            } catch (Exception e) {

            }

            String sql = "INSERT INTO punishments " +
                    "(UUID, banned_forever, ban_start, ban_end, ban_status, ban_logs) " +
                    "VALUES " +
                    "('"+UUIDChecker.uuid+"', false,'"+currentTime.format(formatter)+"', '"+updatedTime.format(formatter)+"', false, "+"'"+ArrayStorage.ban_logs+"')";

            if (punishment_UUID == null && ban_logs == null) {
                stm.executeUpdate(sql);
                p.sendMessage("§7You §asuccessfully §7banned §b"+username+" §7for §c"+time+"§7.");
            } else {
                tempBanUpdate(username, time, reason, p);
            }
            ArrayStorage.ban_logs.clear();
            discord.realtimeChatOnTempBan(UUID.fromString(UUIDChecker.uuid), username, p.getDisplayName(), ban_end, reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tempBanUpdate(String username, String time, String reason , Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);

            banData(UUID.fromString(UUIDChecker.uuid));

            json_o.put("banned by", p.getPlayer().getDisplayName());
            json_o.put("ban_start", currentTime.format(formatter));
            json_o.put("ban_end", updatedTime.format(formatter));
            json_o.put("reason", reason);
            ArrayStorage.ban_logs.add(json_o);

            if (ban_forever) {
                p.sendMessage("§b"+username+" §7has been already §4banned.");
                return;
            } else {
                p.sendMessage("§7You §asuccessfully §7banned §b"+username+" §7for §c"+time+"§7.");
            }

            String sql = "UPDATE punishments " +
                    "SET banned_forever = false, ban_start = '"+currentTime.format(formatter)+"', ban_end = '"+updatedTime.format(formatter)+"', ban_status = true"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";
            stm.executeUpdate(sql);
            stm.close();

            ban_logsUpdate(username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void unban(String username, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            RealtimeBan discord = new RealtimeBan();
            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);
            unbanData(UUID.fromString(UUIDChecker.uuid));

            if (punishment_UUID == null || !ban_status) {
                p.sendMessage("§7The player §b"+username+" §7isn't §4banned§7.");
                return;
            } else {
                p.sendMessage("§7You §asuccessfully §7unbanned §b"+username+"§7.");
            }

            String sql = "UPDATE punishments " +
                    "SET banned_forever = false, ban_start = NULL, ban_end = NULL, ban_status = false"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";
            stm.executeUpdate(sql);
            stm.close();
            discord.realtimeChatOnUnban(UUID.fromString(UUIDChecker.uuid), username, p.getDisplayName());
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
                ban_start = rs.getString(4);
                ban_end = rs.getString(5);
                ban_status = rs.getBoolean(6);
                ban_logs = rs.getString(10);
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
                ban_status = rs.getBoolean(6);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ban_logsUpdate(String username) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);

            if (ban_logs != null) {
                banData(UUID.fromString(UUIDChecker.uuid));
                String replace_ban_logs = ban_logs.replace("[", "").replace("]", "");

                ban_logsArray.add(replace_ban_logs);
            }

            ban_logsArray.add(json_o.toString());

            String sql = "UPDATE punishments " +
                    "SET ban_logs = '"+ban_logsArray+"'"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";
            stm.executeUpdate(sql);
            stm.close();
            ban_logsArray.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}