package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static de.jeezycore.utils.ArrayStorage.tags_in_ownership_array;

public class TagsSQL {

    public String url;
    public String user;
    public String password;

    String grant;

    String tagName;
    public static String ownerTagName;
    String tagDesign;

    String tag_players;

    public static String [] grant_new_tag;

    public static ArrayList<String> player_name_tags_array = new ArrayList<String>();

    public LinkedHashMap<String, String> tagData = new LinkedHashMap<String, String>();

    public LinkedHashMap<String, String> tagDataFullSize = new LinkedHashMap<String, String>();


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

    public void getFullDataSize() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql = "SELECT * FROM tags ORDER BY tagPriority DESC";
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                tagName = rs.getString(1);
                tagDesign = rs.getString(2);

                tagDataFullSize.put(tagName, tagDesign);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getOwnershipData(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql = "SELECT * FROM tags WHERE playerName LIKE '%"+p.getPlayer().getUniqueId()+"%'";
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                ownerTagName = rs.getString(1);
                if (ownerTagName != null) {
                    String[] get_owned_tags = ownerTagName.split(", ");
                    tags_in_ownership_array.addAll(Arrays.asList(get_owned_tags));
                    System.out.println(tags_in_ownership_array);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getData(int startCount) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql = "SELECT * FROM tags ORDER BY tagPriority DESC LIMIT "+startCount+","+1844674407;
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                tagName = rs.getString(1);
                tagDesign = rs.getString(2);

                tagData.put(tagName, tagDesign);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void unGrantTag(String tagName, String playerName, Player p) {
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
                p.sendMessage("§7This tag doesn't §4exit§7.");
                return;
            }

            if (tag_players != null) {

                grant_new_tag = tag_players.replace("]", "").replace("[", "").split(", ");

                player_name_tags_array.addAll(Arrays.asList(grant_new_tag));
            }

            if (!player_name_tags_array.contains(UUIDChecker.uuid)) {
                p.sendMessage("§7This player doesn't §4own §7that tag.");
                return;
            } else {
                player_name_tags_array.remove(UUIDChecker.uuid);
                p.sendMessage("§2Successfully §7removed the §b"+tagName+" §7tag for §9"+playerName+"§7.");
            }

            String sql2;
            if (player_name_tags_array.size() == 0) {
                sql2 = "UPDATE tags " +
                        "SET playerName = NULL" +
                        " WHERE tagName = '"+tagName+"'";
            } else {
                sql2 = "UPDATE tags " +
                        "SET playerName = '"+player_name_tags_array +
                        "' WHERE tagName = '"+tagName+"'";
            }

            stm.executeUpdate(sql2);
            player_name_tags_array.clear();

            con.close();
        } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    public void deleteTag(String tag, Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql = "DELETE FROM tags WHERE tagName = '"+tag+"'";
            stm.executeUpdate(sql);
            p.sendMessage("§2Successfully §7deleted the §9"+tag+" §7tag.");
        } catch (SQLException e) {
            p.sendMessage("§7This tag doesn't §cexist§7.");
            e.printStackTrace();
        }
    }
}