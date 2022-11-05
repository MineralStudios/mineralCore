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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

public class MuteSQL {

    public String url;
    public String user;
    public String password;

    public static boolean mute_forever;

    public static boolean mute_status;

    public static String punishment_UUID;

    private String mute_logs;

    private final ArrayList<String> mute_logsArray = new ArrayList<>();

    private LocalDateTime currentTime;

    private LocalDateTime updatedTime;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String mute_start;
    public String mute_end;

    private JSONObject json_o = new JSONObject();

    private void createConnection() {
        File file = new File("/home/jeffrey/IdeaProjects/JeezyCore/JeezyCore/src/main/java/database.yml");
        FileConfiguration db = YamlConfiguration.loadConfiguration(file);
        MemorySection mc = (MemorySection) db.get("MYSQL");

        url = "jdbc:mysql://localhost:3306/" + mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void mute(String username, String input, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker uc = new UUIDChecker();
            uc.check(username);
            muteData(UUID.fromString(UUIDChecker.uuid));

            json_o.put("muted by", p.getPlayer().getDisplayName());
            json_o.put("time", "forever");
            json_o.put("reason", input);
            ArrayStorage.mute_logs.add(json_o);

            String sql = "INSERT INTO punishments " +
                    "(UUID, mute_forever, mute_start, mute_end, mute_status, mute_logs) " +
                    "VALUES " +
                    "('" + UUIDChecker.uuid + "', true, " + "NULL, NULL, true, '"+ArrayStorage.mute_logs + "')";

            p.sendMessage("§7You §asuccessfully §7muted §b" + username + "§7.");

            System.out.println(sql);
            if (punishment_UUID == null && mute_logs == null) {
                stm.executeUpdate(sql);
            } else {
                muteUpdate(username, input, p);
            }
            ArrayStorage.mute_logs.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void muteUpdate(String username, String input, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);
            muteData(UUID.fromString(UUIDChecker.uuid));

            json_o.put("muted by", p.getPlayer().getDisplayName());
            json_o.put("time", "forever");
            json_o.put("reason", input);
            ArrayStorage.mute_logs.add(json_o);

            if (mute_forever) {
                p.sendMessage("§b" + username + " §7has been already §4muted§7.");
                return;
            } else {
                p.sendMessage("§7You §asuccessfully §7muted §b" + username + "§7.");
            }

            String sql = "UPDATE punishments " +
                    "SET mute_forever = true, mute_status = true" +
                    " WHERE UUID = '" + UUIDChecker.uuid + "'";
            stm.executeUpdate(sql);
            stm.close();

            mute_logsUpdate(username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tempMuteDurationCalculate(Player p) {
        System.out.println(mute_start);
        System.out.println(mute_end);

        LocalDateTime dateTime_start = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        LocalDateTime dateTime_end = LocalDateTime.parse(mute_end, formatter);

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
            tempMuteAutomaticUnban(p.getPlayer());
            return;
        }

        p.sendMessage("§4You are currently muted.\n"+
                "§cMute duration: " + "§cDay(s): §7[§b"+days+"§7] | "+"§cHour(s): §7[§b"+hours+"§7] | "+"§cMinutes: §7[§b"+minutes+"§7] | " +"§cSeconds: §7[§b"+seconds+"§7];");
    }

    private void tempMuteAutomaticUnban(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(p.getPlayer().getDisplayName());

            String sql = "UPDATE punishments " +
                    "SET mute_forever = false, mute_start = NULL, mute_end = NULL, mute_status = false"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";

            stm.executeUpdate(sql);
            stm.close();
        } catch (Exception e) {
        }
    }


    public void tempMuteCalculate(String time) {
        currentTime = LocalDateTime.now();

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

    public void tempMute(String username, String time, String reason , Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker uc = new UUIDChecker();
            uc.check(username);
            tempMuteCalculate(time);
            muteData(UUID.fromString(UUIDChecker.uuid));

            json_o.put("muted by", p.getPlayer().getDisplayName());
            json_o.put("mute_start", currentTime.format(formatter));
            json_o.put("mute_end", updatedTime.format(formatter));
            json_o.put("reason", reason);
            ArrayStorage.mute_logs.add(json_o);

            String sql = "INSERT INTO punishments " +
                    "(UUID, mute_forever, mute_start, mute_end, mute_status, mute_logs) " +
                    "VALUES " +
                    "('"+UUIDChecker.uuid+"', false,'"+currentTime.format(formatter)+"', '"+updatedTime.format(formatter)+"', true, "+"'"+ArrayStorage.mute_logs+"')";

            if (punishment_UUID == null && mute_logs == null) {
                stm.executeUpdate(sql);
                p.sendMessage("§7You §asuccessfully §7muted §b"+username+" for §c"+time+"§7.");
            } else {
                tempMuteUpdate(username, time, reason, p);
            }
            ArrayStorage.mute_logs.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tempMuteUpdate(String username, String time, String reason , Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);

            muteData(UUID.fromString(UUIDChecker.uuid));

            json_o.put("muted by", p.getPlayer().getDisplayName());
            json_o.put("mute_start", currentTime.format(formatter));
            json_o.put("mute_end", updatedTime.format(formatter));
            json_o.put("reason", reason);
            ArrayStorage.mute_logs.add(json_o);

            if (mute_forever) {
                p.sendMessage("§b"+username+" §7has been already §4muted.");
                return;
            } else {
                p.sendMessage("§7You §asuccessfully §7muted §b"+username+" for §c"+time+"§7.");
            }

            String sql = "UPDATE punishments " +
                    "SET mute_forever = false, mute_start = '"+currentTime.format(formatter)+"', mute_end = '"+updatedTime.format(formatter)+"', mute_status = true"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";
            stm.executeUpdate(sql);
            stm.close();

            mute_logsUpdate(username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void unMute(String username, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);
            unMuteData(UUID.fromString(UUIDChecker.uuid));

            if (punishment_UUID == null || !mute_status) {
                p.sendMessage("§7The player §b"+username+" §7isn't §4muted§7.");
                return;
            } else {
                p.sendMessage("§7You §asuccessfully §7unmuted §b"+username+"§7.");
            }

            String sql = "UPDATE punishments " +
                    "SET mute_forever = false, mute_start = NULL, mute_end = NULL, mute_status = false"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";
            stm.executeUpdate(sql);
            stm.close();

        } catch (Exception e) {
        }
    }

    public void muteData(UUID get_UUID) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM punishments WHERE UUID = '" +get_UUID.toString()+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                punishment_UUID = rs.getString(1);
                mute_forever = rs.getBoolean(3);
                mute_start = rs.getString(7);
                mute_end = rs.getString(8);
                mute_status = rs.getBoolean(9);
                mute_logs = rs.getString(11);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unMuteData(UUID get_UUID) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM punishments WHERE UUID = '" + get_UUID.toString() + "'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                punishment_UUID = rs.getString(1);
                mute_forever = rs.getBoolean(3);
                mute_status = rs.getBoolean(9);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mute_logsUpdate(String username) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            UUIDChecker check_UUID = new UUIDChecker();
            check_UUID.check(username);

            if (mute_logs != null) {
                muteData(UUID.fromString(UUIDChecker.uuid));
                String replace_mute_logs = mute_logs.replace("[", "").replace("]", "");

                mute_logsArray.add(replace_mute_logs);
            }

            mute_logsArray.add(json_o.toString());

            String sql = "UPDATE punishments " +
                    "SET mute_logs = '"+mute_logsArray+"'"+
                    " WHERE UUID = '"+UUIDChecker.uuid+"'";
            stm.executeUpdate(sql);
            stm.close();
            mute_logsArray.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
