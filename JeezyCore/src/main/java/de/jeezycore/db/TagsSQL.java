package de.jeezycore.db;

import de.jeezycore.db.cache.TagsCache;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static de.jeezycore.utils.ArrayStorage.*;

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

    public static String playerTagUUID;

    public static String playerTag;

    public static String playerTagCategory;

    public static String playerTagDesign;

    public static String [] grant_new_tag;

    public static ArrayList<String> tag_name_array = new ArrayList<String>();

    public void pushData(String sql, Player p, String tagName, String tagCategory, String tagDesign, String tagPriority) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, tagName);
            pstmt.setString(2, tagCategory);
            pstmt.setString(3, tagDesign);
            pstmt.setString(4, tagPriority);

            pstmt.executeUpdate();

            p.sendMessage("§2Successfully §7created the tag §b§l"+tagName+".");
        } catch (SQLException e) {
            p.sendMessage("§7The tag §b§l"+tagName+"§7 has been §calready §7created!");
            System.out.println(e);
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void getOwnershipData(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.getAllPlayerTags();
            connection = dataSource.getConnection();

            statement = connection.createStatement();
            String sql = "SELECT ownedTags FROM items WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ownerTagName = resultSet.getString(1);
                if (ownerTagName != null) {
                    String[] get_owned_tags = ownerTagName.replace("[", "").replace("]", "").split(", ");
                    tags_in_ownership_array.addAll(Arrays.asList(get_owned_tags));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getAllPlayerTags() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            statement = connection.createStatement();
            String sql = "SELECT \n" +
                    "    players.playerUUID,\n" +
                    "    players.tag,\n" +
                    "    tags.tagCategory,\n" +
                    "    tags.tagDesign,\n" +
                    "    tags.tagPriority\n" +
                    "FROM \n" +
                    "    players\n" +
                    "JOIN \n" +
                    "    tags ON players.tag = tags.tagName\n" +
                    "WHERE \n" +
                    "    players.tag IS NOT NULL;";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                playerTagUUID = null;
                playerTag = null;
                playerTagCategory = null;
                playerTagDesign = null;
            } else {
                do {
                    playerTagUUID = resultSet.getString(1);
                    playerTag = resultSet.getString(2);
                    playerTagCategory = resultSet.getString(3);
                    playerTagDesign = resultSet.getString(4);

                   TagsCache.getInstance().savePlayerTags(UUID.fromString(playerTagUUID), playerTag, playerTagCategory, playerTagDesign);

                } while (resultSet.next());
                    TagsCache.getInstance().saveAllPlayerTags();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getPlayerOwnedTags(String playerName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            statement = connection.createStatement();

            String sql_items = "SELECT * FROM items WHERE playerUUID = '"+UUIDChecker.uuid+"'";

            resultSet = statement.executeQuery(sql_items);

            if (!resultSet.next()) {
                ownedItemsPlayerUUID = null;
                ownedTags = null;
            } else {
                do {
                    ownedItemsPlayerUUID = resultSet.getString(2);
                    ownedTags = resultSet.getString(6);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCurrentTag(String tagName, Player player) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql_2 = "UPDATE players " +
                    "SET tag = '"+tagName +
                    "' WHERE playerUUID = '"+player.getUniqueId()+"'";
            statement.executeUpdate(sql_2);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getData() {
            Connection connection = null;
            Statement statement = null;
            ResultSet resultSet = null;
            try {
                connection = dataSource.getConnection();

                statement = connection.createStatement();
                String sql = "SELECT * FROM tags ORDER BY tagPriority DESC";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    tagName = resultSet.getString(1);
                    tagCategory = resultSet.getString(2);
                    tagDesign = resultSet.getString(3);

                    TagsCache.getInstance().saveTag(tagName, tagCategory, tagDesign);
                }
                TagsCache.getInstance().saveAllTags();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    resultSet.close();
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    }


    public void grantTag(Player p, String tagName, String playerName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.getPlayerOwnedTags(playerName);
            connection = dataSource.getConnection();
            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            statement = connection.createStatement();
            String sql_tags = "SELECT * FROM tags WHERE tagName = '"+tagName+"'";

            resultSet = statement.executeQuery(sql_tags);

            while (resultSet.next()) {
                tagNameTags = resultSet.getString(1);
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
                statement.executeUpdate(sql_insert_items);
            } else {
                String sql_push_items = "UPDATE items " +
                        "SET ownedTags = '"+ tag_name_array +
                        "' WHERE playerUUID = '"+UUIDChecker.uuid+"'";
                statement.executeUpdate(sql_push_items);
            }
                tag_name_array.clear();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void grantTagConsole(CommandSender sender, String tagName, String playerName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.getPlayerOwnedTags(playerName);
            connection = dataSource.getConnection();
            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            statement = connection.createStatement();
            String sql_tags = "SELECT * FROM tags WHERE tagName = '"+tagName+"'";

            resultSet = statement.executeQuery(sql_tags);

            while (resultSet.next()) {
                tagNameTags = resultSet.getString(1);
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
                statement.executeUpdate(sql_insert_items);
            } else {
                String sql_push_items = "UPDATE items " +
                        "SET ownedTags = '"+ tag_name_array +
                        "' WHERE playerUUID = '"+UUIDChecker.uuid+"'";
                statement.executeUpdate(sql_push_items);
            }
            tag_name_array.clear();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void unGrantTag(String tagName, String playerName, Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            this.getPlayerOwnedTags(playerName);
            statement = connection.createStatement();

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

            statement.executeUpdate(sql);
            tag_name_array.clear();
        } catch (SQLException e) {
        e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void unGrantTagConsole(String tagName, String playerName, CommandSender sender) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            this.getPlayerOwnedTags(playerName);

            statement = connection.createStatement();

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

            statement.executeUpdate(sql2);
            tag_name_array.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteTag(String tag, Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            statement = connection.createStatement();
            String sql = "DELETE FROM tags WHERE tagName = '"+tag+"'";
            statement.executeUpdate(sql);
            p.sendMessage("§2Successfully §7deleted the §9"+tag+" §7tag.");
        } catch (SQLException e) {
            p.sendMessage("§7This tag doesn't §cexist§7.");
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void tagChat(Player player) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.getAllPlayerTags();

            connection = dataSource.getConnection();

            statement = connection.createStatement();
            String sql = "SELECT * FROM tags WHERE tagName = '"+playerTag+"'";
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                tag_in_chat = null;
            } else {
                do {
                    tag_in_chat = resultSet.getString(3);
                } while (resultSet.next());
            }

            if (tag_in_chat == null) {
                tag_in_chat = "";
            } else {
                tag_in_chat = " "+tag_in_chat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetTag(String tagName, String playerName, Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            statement = connection.createStatement();
            String sql = "SELECT * FROM items WHERE playerUUID = '"+p.getUniqueId()+"'";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                current_tag = resultSet.getString(6);
            }

            String sql2 = "UPDATE players " +
                    "SET tag = "+null+
                    " WHERE playerUUID = '"+UUIDChecker.uuid+"'";

            statement.executeUpdate(sql2);
            tag_name_array.clear();
            ArrayStorage.tagsCheckStatus.remove(p.getPlayer().getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetTagOnUnGrantingRank() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql2 = "UPDATE players " +
                    "SET tag = "+null+
                    " WHERE playerUUID = '"+UUIDChecker.uuid+"'";

            statement.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetTagConsole(String tagName, String playerName, CommandSender sender) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            statement = connection.createStatement();
            String sql = "SELECT * FROM items WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                current_tag = resultSet.getString(6);
            }

            String sql2 = "UPDATE players " +
                    "SET tag = NULL"+
                    " WHERE playerUUID = '"+UUIDChecker.uuid+"'";

            statement.executeUpdate(sql2);
            tag_name_array.clear();
            ArrayStorage.tagsCheckStatus.remove(UUID.fromString(UUIDChecker.uuid));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}