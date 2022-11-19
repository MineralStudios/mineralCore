package de.jeezycore.db;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;

public class StaffSQL {

    public String url;
    public String user;
    public String password;

    public static String staffRank;

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

            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            if (JeezySQL.permPlayerRankName == null) {
                p.sendMessage("§4This rank hasn't been created yet.");
                return;
            }

            String sql = "INSERT INTO staff (rankName) VALUES ('"+rankName+"')";

            stm.executeUpdate(sql);

            p.sendMessage("§bSuccessfully§7 changed the "+ColorTranslator.colorTranslator.get(Integer.parseInt(JeezySQL.permPlayerRankColor))+JeezySQL.permPlayerRankName+" §7rank to a staff rank.");
            con.close();
        } catch (SQLException e) {
            p.sendMessage("§7The "+ColorTranslator.colorTranslator.get(Integer.parseInt(JeezySQL.permPlayerRankColor))+JeezySQL.permPlayerRankName+" §7rank is §calready §7a staff rank.");
            System.out.println(e);
        }
        JeezySQL.permPlayerRankName = null;
    }

    public void removeFromStaff(String rankName, Player p) {
        try {
            JeezySQL jeezySQL = new JeezySQL();
            jeezySQL.getRankData(rankName, p);

            this.createConnection();

            getData(rankName);

            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            if (JeezySQL.permPlayerRankName == null || staffRank == null) {
                p.sendMessage("§4This rank isn't a staff rank.");
                return;
            }

            String sql = "DELETE FROM staff WHERE rankName = '"+rankName+"'";

            stm.executeUpdate(sql);

            p.sendMessage("§bSuccessfully§7 removed the "+ColorTranslator.colorTranslator.get(Integer.parseInt(JeezySQL.permPlayerRankColor))+JeezySQL.permPlayerRankName+" §7rank from the staff ranks.");
            con.close();
        } catch (SQLException e) {
            p.sendMessage("§7The "+ColorTranslator.colorTranslator.get(Integer.parseInt(JeezySQL.permPlayerRankColor))+JeezySQL.permPlayerRankName+" §7rank is §calready §7removed from the staff ranks.");
            System.out.println(e);
        }
        JeezySQL.permPlayerRankName = null;
        staffRank = null;
    }

    public void getData(String rankName) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM staff WHERE rankName = '" +rankName+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                staffRank = rs.getString(1);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}