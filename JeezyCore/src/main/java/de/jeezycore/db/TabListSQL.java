package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;

import static de.jeezycore.utils.ArrayStorage.tab_name_list_array;

public class TabListSQL {
    public String url;
    public String user;
    public String password;
    String rankColor;

    private void createConnection() {
        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");
        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }


    public void getTabListData(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql = "SELECT * FROM jeezycore WHERE playerUUID LIKE '%"+p.getPlayer().getUniqueId()+"%'";
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                rankColor = rs.getString(3);

                if (rankColor == null) {
                return;
                }

                tab_name_list_array.put(p, rankColor);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


}