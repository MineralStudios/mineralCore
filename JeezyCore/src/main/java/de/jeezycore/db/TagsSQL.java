package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class TagsSQL {

    public String url;
    public String user;
    public String password;

    String alreadyGranted;

    String grant;

    String tag_players;

    public static String [] grant_new_tag;

    public static ArrayList<String> player_name_tags_array = new ArrayList<String>();


    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void pushData(String sql, Player p, String tagName, String tagDesign, String tagPriority) {
        this.createConnection();
        try {
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, tagName);
            pstmt.setString(2, tagDesign);
            pstmt.setString(3, tagPriority);

            pstmt.executeUpdate();

            p.sendMessage("§2Successfully §7created the tag §b§l"+tagName+".");

            con.close();
        } catch (SQLException e) {
            p.sendMessage("§7The tag §b§l"+tagName+"§7 has been §calready §7created!");
            System.out.println(e);
        }
    }

    public void grantTag(Player p, String tagName, String playerName) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);

            Statement stm = con.createStatement();
            String sql = "SELECT * FROM tags WHERE tagName = '"+tagName+"'";
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                grant = rs.getString(1);
                tag_players = rs.getString(4);
            }


            if (grant == null) {
                p.sendMessage("§4This rank hasn't been created yet.");
                return;
            }

            if (tag_players != null) {

                grant_new_tag = tag_players.replace("]", "").replace("[", "").split(", ");

                player_name_tags_array.addAll(Arrays.asList(grant_new_tag));

            }

            System.out.println(player_name_tags_array);

            if (player_name_tags_array.contains(UUIDChecker.uuid)) {
                p.sendMessage("§7Tag has been §calready §7unlocked for §b"+playerName+"§7.");
            } else {
                player_name_tags_array.add(UUIDChecker.uuid);
                p.sendMessage("§2Successfully §7unlocked the §b"+tagName+" §7tag for §9"+playerName+"§7.");
            }

                String sql2 = "UPDATE tags " +
                        "SET playerName = '"+player_name_tags_array +
                        "' WHERE tagName = '"+tagName+"'";

                stm.executeUpdate(sql2);
                player_name_tags_array.clear();


            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}