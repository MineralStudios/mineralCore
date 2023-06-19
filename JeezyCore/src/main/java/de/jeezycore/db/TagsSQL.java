package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.UUID;
import static de.jeezycore.utils.ArrayStorage.set_current_tag_array;
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

    String current_tag;

    public static String tag_in_chat;

    public static String tag_exist_format;
    public static String tag_exist_name;

    public static String [] grant_new_tag;

    public static String [] already_set_tag;

    public static ArrayList<String> player_name_tags_array = new ArrayList<String>();

    public LinkedHashMap<String, String> tagData = new LinkedHashMap<String, String>();

    public LinkedHashMap<String, String> tagDataFullSize = new LinkedHashMap<String, String>();


    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void pushData(String sql, Player p, String tagName, String tagCategory, String tagDesign, String tagPriority) {
        this.createConnection();
        try {
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, tagName);
            pstmt.setString(2, tagCategory);
            pstmt.setString(3, tagDesign);
            pstmt.setString(4, tagPriority);

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
                tagDesign = rs.getString(3);

                tagDataFullSize.put(tagName, tagDesign);
            }
            con.close();
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
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void alreadySetCurrentTag(String tagName, UUID get_uuid) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql_already_g = "SELECT * FROM tags WHERE currentTag LIKE '%"+get_uuid+"%'";
            ResultSet rs = stm.executeQuery(sql_already_g);
            while (rs.next()) {
                this.tagName = rs.getString(1);
                current_tag = rs.getString(6);
            }
            if (current_tag != null) {

                    System.out.println("getting executed");
                    already_set_tag = current_tag.replace("]", "").replace("[", "").split(", ");

                    set_current_tag_array.addAll(Arrays.asList(already_set_tag));

                    set_current_tag_array.remove(get_uuid.toString());

                    System.out.println(set_current_tag_array);

                    String sql_already_g2;
                    if (set_current_tag_array.size() == 0) {
                        sql_already_g2 = "UPDATE tags " +
                                "SET currentTag = NULL" +
                                " WHERE tagName = '"+this.tagName+"'";
                    } else {
                        sql_already_g2 = "UPDATE tags " +
                                "SET currentTag = '" + set_current_tag_array +
                                "' WHERE tagName = '"+this.tagName+"'";
                    }
                    System.out.println(sql_already_g);
                    System.out.println(sql_already_g2);
                    stm.executeUpdate(sql_already_g2);

                    set_current_tag_array.clear();
                }

            con.close();
        }catch (SQLException e) {
            System.out.println(e);
        }

    }
    public void setCurrentTag(String tagName, UUID get_uuid) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            this.alreadySetCurrentTag(tagName, get_uuid);

            Statement stm = con.createStatement();
            String sql2 = "SELECT * FROM tags WHERE tagName = '"+tagName+"'";
            ResultSet rs = stm.executeQuery(sql2);
            while (rs.next()) {
                current_tag = rs.getString(6);
            }

            if (current_tag != null) {
                if (current_tag.contains(get_uuid.toString())) return;

                grant_new_tag = current_tag.replace("]", "").replace("[", "").split(", ");

                set_current_tag_array.addAll(Arrays.asList(grant_new_tag));

            }
            set_current_tag_array.add(String.valueOf(get_uuid));

            String sql = "UPDATE tags " +
                    "SET currentTag = '"+set_current_tag_array +
                    "' WHERE tagName = '"+tagName+"'";
            System.out.println(sql);
            stm.executeUpdate(sql);
            set_current_tag_array.clear();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
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
                tagDesign = rs.getString(3);

                tagData.put(tagName, tagDesign);
            }
            con.close();
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
                tag_players = rs.getString(5);
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
                tag_players = rs.getString(5);
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
            con.close();
        } catch (SQLException e) {
            p.sendMessage("§7This tag doesn't §cexist§7.");
            e.printStackTrace();
        }
    }

    public void tagChat(UUID get_uuid) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql = "SELECT * FROM tags WHERE currentTag LIKE '%"+get_uuid+"%'";
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                tag_in_chat = rs.getString(3);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void check(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql = "SELECT * FROM tags WHERE currentTag LIKE '%"+p.getUniqueId()+"%'";
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                tag_exist_name = rs.getString(1);
                tag_exist_format = rs.getString(3);
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetTag(String tagName, String playerName, Player p) {
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
                current_tag = rs.getString(6);
            }

            if (grant == null) {
                p.sendMessage("§7This tag doesn't §4exit§7.");
                return;
            }

            if (current_tag != null) {

                grant_new_tag = current_tag.replace("]", "").replace("[", "").split(", ");

                player_name_tags_array.addAll(Arrays.asList(grant_new_tag));
            }

            if (!player_name_tags_array.contains(UUIDChecker.uuid)) {
                p.sendMessage("§7This player doesn't §4own §7that tag.");
                return;
            } else {
                player_name_tags_array.remove(UUIDChecker.uuid);
            }

            String sql2;
            if (player_name_tags_array.size() == 0) {
                sql2 = "UPDATE tags " +
                        "SET currentTag = NULL" +
                        " WHERE tagName = '"+tagName+"'";
            } else {
                sql2 = "UPDATE tags " +
                        "SET currentTag = '"+player_name_tags_array +
                        "' WHERE tagName = '"+tagName+"'";
            }

            stm.executeUpdate(sql2);
            player_name_tags_array.clear();

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}