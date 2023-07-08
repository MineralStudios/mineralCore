package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.CommandSender;
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

    String tagNameTags;

    String tagName;

    String tagCategory;

    public static String ownerTagName;
    String tagDesign;


    String ownedTags;

    String ownedItemsPlayerUUID;
    String current_tag;

    public static String tag_in_chat;

    public static String tag_exist_format;
    public static String tag_exist_name;

    public static String playerTag;

    public static String [] grant_new_tag;

    public static String [] already_set_tag;

    public ArrayList<String> arrayList = new ArrayList<>();

    public ArrayList<String> tagNameList = new ArrayList<>();

    public static ArrayList<String> tag_name_array = new ArrayList<String>();

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
            this.getPlayerTag(p);
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql = "SELECT ownedTags FROM items WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                ownerTagName = rs.getString(1);
                if (ownerTagName != null) {
                    String[] get_owned_tags = ownerTagName.replace("[", "").replace("]", "").split(", ");
                    tags_in_ownership_array.addAll(Arrays.asList(get_owned_tags));

                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getPlayerTag(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql = "SELECT tag FROM players WHERE playerUUID = '"+p.getUniqueId()+"'";
            ResultSet rs = stm.executeQuery(sql);
            if (!rs.next()) {
               playerTag = null;
            } else {
                do {
                   playerTag = rs.getString(1);
                } while (rs.next());
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getPlayerOwnedTags(String playerName) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            Statement stm = con.createStatement();

            String sql_items = "SELECT * FROM items WHERE playerUUID = '"+UUIDChecker.uuid+"'";

            ResultSet rs = stm.executeQuery(sql_items);

            if (!rs.next()) {
                ownedItemsPlayerUUID = null;
                ownedTags = null;
            } else {
                do {
                    ownedItemsPlayerUUID = rs.getString(2);
                    ownedTags = rs.getString(3);
                } while (rs.next());
            }
            stm.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void setCurrentTag(String tagName, Player player) {
        try {
            this.createConnection();

            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql2 = "SELECT * FROM items WHERE playerUUID = '"+player.getUniqueId()+"'";
            ResultSet rs = stm.executeQuery(sql2);

            if (!rs.next()) {
                current_tag = null;
            } else {
                do {
                    current_tag = rs.getString(3);
                } while (rs.next());
            }

            String sql_2 = "UPDATE players " +
                    "SET tag = '"+tagName +
                    "' WHERE playerUUID = '"+player.getUniqueId()+"'";
            stm.executeUpdate(sql_2);
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
                tagCategory = rs.getString(2);
                tagDesign = rs.getString(3);

                arrayList.add(Arrays.asList(tagName, tagCategory, tagDesign).toString());
                tagNameList.add(tagName);

            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void grantTag(Player p, String tagName, String playerName) {
        try {
            this.createConnection();
            this.getPlayerOwnedTags(playerName);
            Connection con = DriverManager.getConnection(url, user, password);
            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            Statement stm = con.createStatement();
            String sql_tags = "SELECT * FROM tags WHERE tagName = '"+tagName+"'";

            ResultSet rs = stm.executeQuery(sql_tags);

            while (rs.next()) {
                tagNameTags = rs.getString(1);
            }


            if (tagNameTags == null) {
                p.sendMessage("§4This rank hasn't been created yet.");
                return;
            }

            if (ownedTags != null) {

                grant_new_tag = ownedTags.replace("]", "").replace("[", "").split(", ");

                tag_name_array.addAll(Arrays.asList(grant_new_tag));

            }

            if (tag_name_array.contains(tagName)) {
                p.sendMessage("§7Tag has been §calready §7unlocked for §b"+playerName+"§7.");
            } else {
                tag_name_array.add(tagName);
                p.sendMessage("§2Successfully §7unlocked the §b"+tagName+" §7tag for §9"+playerName+"§7.");
            }

            if (ownedItemsPlayerUUID == null) {
                String sql_insert_items = "INSERT INTO items" +
                        "(playerName, playerUUID, ownedTags, ownedChatColors)" +
                        "VALUES ('"+UUIDChecker.uuidName+"', '"+UUIDChecker.uuid+"', '"+tag_name_array+"', '"+null+"')";
                stm.executeUpdate(sql_insert_items);
            } else {
                String sql_push_items = "UPDATE items " +
                        "SET ownedTags = '"+ tag_name_array +
                        "' WHERE playerUUID = '"+UUIDChecker.uuid+"'";
                stm.executeUpdate(sql_push_items);
            }
                tag_name_array.clear();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    public void grantTagConsole(CommandSender sender, String tagName, String playerName) {
        try {
            this.createConnection();
            this.getPlayerOwnedTags(playerName);
            Connection con = DriverManager.getConnection(url, user, password);
            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            Statement stm = con.createStatement();
            String sql_tags = "SELECT * FROM tags WHERE tagName = '"+tagName+"'";

            ResultSet rs = stm.executeQuery(sql_tags);

            while (rs.next()) {
                tagNameTags = rs.getString(1);
            }


            if (tagNameTags == null) {
                sender.sendMessage("§4This rank hasn't been created yet.");
                return;
            }

            if (ownedTags != null) {

                grant_new_tag = ownedTags.replace("]", "").replace("[", "").split(", ");

                tag_name_array.addAll(Arrays.asList(grant_new_tag));

            }

            if (tag_name_array.contains(tagName)) {
                sender.sendMessage("§7Tag has been §calready §7unlocked for §b"+playerName+"§7.");
            } else {
                tag_name_array.add(tagName);
                sender.sendMessage("§2Successfully §7unlocked the §b"+tagName+" §7tag for §9"+playerName+"§7.");
            }

            if (ownedItemsPlayerUUID == null) {
                String sql_insert_items = "INSERT INTO items" +
                        "(playerName, playerUUID, ownedTags, ownedChatColors)" +
                        "VALUES ('"+UUIDChecker.uuidName+"', '"+UUIDChecker.uuid+"', '"+tag_name_array+"', '"+null+"')";
                stm.executeUpdate(sql_insert_items);
            } else {
                String sql_push_items = "UPDATE items " +
                        "SET ownedTags = '"+ tag_name_array +
                        "' WHERE playerUUID = '"+UUIDChecker.uuid+"'";
                stm.executeUpdate(sql_push_items);
            }
            tag_name_array.clear();
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
            this.getPlayerOwnedTags(playerName);
            Statement stm = con.createStatement();

            if (ownedTags != null) {

                grant_new_tag = ownedTags.replace("]", "").replace("[", "").split(", ");

                tag_name_array.addAll(Arrays.asList(grant_new_tag));
            }

            if (!tag_name_array.contains(tagName)) {
                p.sendMessage("§7This player doesn't §4own §7that tag.");
                return;
            } else {
                tag_name_array.remove(tagName);
                p.sendMessage("§2Successfully §7removed the §b"+tagName+" §7tag for §9"+playerName+"§7.");
            }
            String sql;
            if (tag_name_array.size() == 0) {
                System.out.println("I am getting here");
                sql = "UPDATE items " +
                        "SET ownedTags = NULL" +
                        " WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            } else {
                sql = "UPDATE items " +
                        "SET ownedTags = '"+ tag_name_array +
                        "' WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            }

            stm.executeUpdate(sql);
            tag_name_array.clear();
            con.close();
        } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    public void unGrantTagConsole(String tagName, String playerName, CommandSender sender) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            this.getPlayerOwnedTags(playerName);

            Statement stm = con.createStatement();

            if (ownedTags != null) {

                grant_new_tag = ownedTags.replace("]", "").replace("[", "").split(", ");

                tag_name_array.addAll(Arrays.asList(grant_new_tag));
            }

            if (!tag_name_array.contains(tagName)) {
                sender.sendMessage("§7This player doesn't §4own §7that tag.");
                return;
            } else {
                tag_name_array.remove(tagName);
                sender.sendMessage("§2Successfully §7removed the §b"+tagName+" §7tag for §9"+playerName+"§7.");
            }
            String sql2;
            if (tag_name_array.size() == 0) {
                System.out.println("I am getting here");
                sql2 = "UPDATE items " +
                        "SET ownedTags = NULL" +
                        " WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            } else {
                sql2 = "UPDATE items " +
                        "SET ownedTags = '"+ tag_name_array +
                        "' WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            }

            stm.executeUpdate(sql2);
            tag_name_array.clear();

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

    public void tagChat(Player player) {
        try {
            this.createConnection();
            this.getPlayerTag(player);
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql = "SELECT * FROM tags WHERE tagName = '"+playerTag+"'";
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
            getPlayerTag(p);
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stm = con.createStatement();
            String sql = "SELECT * FROM tags WHERE tagName = '"+playerTag+"'";
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
            String sql = "SELECT * FROM items WHERE playerUUID = '"+p.getUniqueId()+"'";
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                current_tag = rs.getString(3);
            }

            String sql2 = "UPDATE players " +
                    "SET tag = "+null+
                    " WHERE playerUUID = '"+UUIDChecker.uuid+"'";

            stm.executeUpdate(sql2);
            tag_name_array.clear();
            ArrayStorage.tagsCheckStatus.remove(p.getPlayer().getUniqueId());

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetTagConsole(String tagName, String playerName, CommandSender sender) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);

            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            Statement stm = con.createStatement();
            String sql = "SELECT * FROM items WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                current_tag = rs.getString(3);
            }

            String sql2 = "UPDATE players " +
                    "SET tag = "+null+
                    " WHERE playerUUID = '"+UUIDChecker.uuid+"'";

            stm.executeUpdate(sql2);
            tag_name_array.clear();
            ArrayStorage.tagsCheckStatus.remove(UUID.fromString(UUIDChecker.uuid));

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}