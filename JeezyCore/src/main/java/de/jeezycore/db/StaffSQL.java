package de.jeezycore.db;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class StaffSQL {

    public String url;
    public String user;
    public String password;

    public static String staffPlayerNames;
    public static boolean staffRank;

    public static ArrayList<String> staff = new ArrayList<>();

    public static String [] staff_array;

    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void addToStaff(String rankName, Player p) {
        try {
            JeezySQL jeezySQL = new JeezySQL();
            jeezySQL.getRankData(rankName, p);

            this.createConnection();

            getStaffRank(rankName);

            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            if (JeezySQL.permPlayerRankName == null) {
                p.sendMessage("§4This rank hasn't been created yet.");
                return;
            } else if (staffRank) {
                p.sendMessage("§7The "+ColorTranslator.colorTranslator.get(Integer.parseInt(JeezySQL.permPlayerRankColor))+JeezySQL.permPlayerRankName+" §7rank is §calready §7a staff rank.");
                return;
            }

            String sql = "UPDATE jeezycore " +
                    "SET staffRank = true WHERE rankName = '"+rankName+"'";

            stm.executeUpdate(sql);

            p.sendMessage("§bSuccessfully§7 changed the "+ColorTranslator.colorTranslator.get(Integer.parseInt(JeezySQL.permPlayerRankColor))+JeezySQL.permPlayerRankName+" §7rank to a staff rank.");
            con.close();
        } catch (SQLException e) {
        }
        JeezySQL.permPlayerRankName = null;
        staffRank = false;
    }

    public void removeFromStaff(String rankName, Player p) {
        try {
            JeezySQL jeezySQL = new JeezySQL();
            jeezySQL.getRankData(rankName, p);

            this.createConnection();

            getStaffRank(rankName);

            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            if (JeezySQL.permPlayerRankName == null) {
                p.sendMessage("§4This rank hasn't been setuped yet.");
                return;
            } else if (!staffRank) {
                p.sendMessage("§7The "+ColorTranslator.colorTranslator.get(Integer.parseInt(JeezySQL.permPlayerRankColor))+JeezySQL.permPlayerRankName+" §7rank is §calready §7removed from the staff ranks.");
                return;
            }

            String sql = "UPDATE jeezycore " +
                    "SET staffRank = false WHERE rankName = '"+rankName+"'";

            stm.executeUpdate(sql);

            p.sendMessage("§bSuccessfully§7 removed the "+ColorTranslator.colorTranslator.get(Integer.parseInt(JeezySQL.permPlayerRankColor))+JeezySQL.permPlayerRankName+" §7rank from the staff ranks.");
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        JeezySQL.permPlayerRankName = null;
        staffRank = false;
    }

    public void getStaffRank(String rankName) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM jeezycore WHERE staffRank = true AND rankName = '"+rankName+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                staffRank = rs.getBoolean(6);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getStaff() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM jeezycore WHERE staffRank = true AND playerName LIKE '%[%'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                staffPlayerNames = rs.getString(4);

               staff_array = staffPlayerNames.replace("]", "").replace("[", "").split(", ");

               staff.addAll(Arrays.asList(staff_array));

            }
            con.close();
        } catch (SQLException f) {
            f.printStackTrace();
        }
    }

    public void checkIfStaff(UUID get_UUID) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+get_UUID+"%'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                staffRank = rs.getBoolean(6);
            }
            System.out.println(staffRank);
            System.out.println(select_sql);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}