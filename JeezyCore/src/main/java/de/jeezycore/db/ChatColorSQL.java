package de.jeezycore.db;

import de.jeezycore.db.cache.ChatColorsCache;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import static de.jeezycore.db.hikari.HikariCP.dataSource;


public class ChatColorSQL {

    String chatColorName;

    String colorName;
    String color;
    String colorRGB;

    String itemsAvailable;

    String itemsAlreadyGranted;

    String chatColorExisting;

    String chatColorExistingColor;

    public static String currentChatColorName;

    public static String currentChatColor;

    public ArrayList<String> grantChatColorArray = new ArrayList<>();

    private final UUIDChecker uc = new UUIDChecker();


    private void setup() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM items WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                itemsAvailable = null;
                itemsAlreadyGranted = null;
            } else {
                do {
                    itemsAvailable = resultSet.getString(2);
                    itemsAlreadyGranted = resultSet.getString(7);
                } while (resultSet.next());
            }
            if (itemsAvailable == null) {
                String sql_settings_available ="INSERT INTO items" +
                        "(playerName, playerUUID) " +
                        "VALUES ('"+UUIDChecker.uuidName+"', '"+UUIDChecker.uuid+"')";
                statement.executeUpdate(sql_settings_available);
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


    public void create(Player p, String colorName, String color, String colorRGB, int colorPriority) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM chatColors WHERE colorName = '"+colorName+"'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                chatColorName = null;
            } else {
                do {
                    chatColorName = resultSet.getString(1);
                } while (resultSet.next());
            }

            if (chatColorName == null) {
                String sql_chatColor_insert ="INSERT INTO chatColors" +
                        "(colorName, color, colorRGB, colorPriority) " +
                        "VALUES ('"+colorName+"', '"+color+"', " +
                        "'"+colorRGB+"', '"+colorPriority+"')";
                statement.executeUpdate(sql_chatColor_insert);
                p.sendMessage("§7You §2successfully §7created the chatColor "+color+colorName+"§7.");
            } else {
                p.sendMessage("§7The chatColor "+color+colorName+" §calready §7exists.");
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

    public void getChatColorsData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM chatColors ORDER BY colorPriority DESC";
            resultSet = statement.executeQuery(sql_select);

            while (resultSet.next()) {
                colorName = resultSet.getString(1);
                color = resultSet.getString(2);
                colorRGB = resultSet.getString(3);

                ChatColorsCache.getInstance().saveChatColors(colorName, color, colorRGB);
            }
            ChatColorsCache.getInstance().saveAllChatColors();
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

    public void getChatColorsPlayersData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT players.playerUUID, color, players.chatColor, colorRGB FROM players JOIN chatColors ON players.chatColor = chatColors.colorName";
            resultSet = statement.executeQuery(sql_select);

            while (resultSet.next()) {
                String playerUUID = resultSet.getString("playerUUID");
                String playerChatColor = resultSet.getString("color");
                String playerChatColorName = resultSet.getString("chatColor");

                ChatColorsCache.getInstance().saveChatColorsPlayers(UUID.fromString(playerUUID), playerChatColor+playerChatColorName);
            }
            ChatColorsCache.getInstance().saveAllChatColorsPlayers();
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

    public void getPlayerChatName(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT chatColor FROM players WHERE playerUUID = '"+p.getUniqueId()+"'";
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                currentChatColorName = null;
            } else {
                do {
                    currentChatColorName = resultSet.getString(1);
                } while (resultSet.next());
            }
            this.getPlayerChatColor(p);
        } catch (SQLException f) {
            f.printStackTrace();
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

    public void getPlayerChatColor(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM chatColors WHERE colorName = '"+currentChatColorName+"'";
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                currentChatColor = "§2";
            } else {
                do {
                    currentChatColor = resultSet.getString(2);
                } while (resultSet.next());
            }
        } catch (SQLException f) {
            f.printStackTrace();
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

        public void getPlayerChatColorOnUngrant(String chatColorName) {
            Connection connection = null;
            Statement statement = null;
            ResultSet resultSet = null;
            try {
                connection = dataSource.getConnection();
                statement = connection.createStatement();
                String sql_select = "SELECT * FROM chatColors WHERE colorName = '" + chatColorName + "'";
                resultSet = statement.executeQuery(sql_select);
                if (!resultSet.next()) {
                    currentChatColor = null;
                } else {
                    do {
                        currentChatColor = resultSet.getString(2);
                    } while (resultSet.next());
                }
            } catch (SQLException f) {
                f.printStackTrace();
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



    public void setChatColor(org.bukkit.event.inventory.InventoryClickEvent e) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

                String updateChatColor = "UPDATE players " +
                        "SET chatColor = '"+e.getCurrentItem().getItemMeta().getDisplayName().substring(2)+"'" +
                        " WHERE playerUUID = '"+e.getWhoClicked().getUniqueId()+"'";
            statement.executeUpdate(updateChatColor);

        } catch (SQLException f) {
            f.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
        }
    }

    public void getChatColorsGrantedBefore() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM items WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                itemsAlreadyGranted = null;
            } else {
                do {
                    itemsAlreadyGranted = resultSet.getString(7);
                } while (resultSet.next());
            }

            if (itemsAlreadyGranted != null) {
                String[] get_owned_tags = itemsAlreadyGranted.replace("[", "").replace("]", "").split(", ");
                grantChatColorArray.addAll(Arrays.asList(get_owned_tags));
            }
        } catch (SQLException f) {
            f.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
        }
    }

    public void grantChatColor(Player p, String playerName, String chatColor) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            uc.check(playerName);
            this.setup();
            this.getChatColorsGrantedBefore();

            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM chatColors WHERE colorName = '"+chatColor+"'";
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                chatColorExisting = null;
            } else {
                do {
                    chatColorExisting = resultSet.getString(1);
                    chatColorExistingColor = resultSet.getString(2);
                } while (resultSet.next());
            }

            if (itemsAlreadyGranted != null){
                if (itemsAlreadyGranted.contains(chatColor)) {
                    p.sendMessage("§9§l"+UUIDChecker.uuidName+" §7has been already §9granted §7the chat color "+chatColorExistingColor+chatColor+"§7.");
                    return;
                }
            }
            if (chatColorExisting != null) {
            grantChatColorArray.add(chatColor);
            System.out.println(grantChatColorArray.size());
            p.sendMessage("§7You §2successfully §7granted §9"+playerName+" §7the "+chatColorExistingColor+chatColor+" §7chat color.");
            } else {
                p.sendMessage("§7The chat color §9"+chatColor+" §7doesn't exist.");
            }


            String updateChatColor = "UPDATE items " +
                    "SET ownedChatColors = '"+grantChatColorArray+"'" +
                    " WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            statement.executeUpdate(updateChatColor);
            grantChatColorArray.clear();
        } catch (SQLException f) {
            f.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
        }
    }

    public void grantChatColorConsole(CommandSender sender, String playerName, String chatColor) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            uc.check(playerName);
            this.setup();
            this.getChatColorsGrantedBefore();

            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM chatColors WHERE colorName = '"+chatColor+"'";
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                chatColorExisting = null;
            } else {
                do {
                    chatColorExisting = resultSet.getString(1);
                    chatColorExistingColor = resultSet.getString(2);
                } while (resultSet.next());
            }

            if (itemsAlreadyGranted != null){
                if (itemsAlreadyGranted.contains(chatColor)) {
                    sender.sendMessage("§9§l"+UUIDChecker.uuidName+" §7has been already §9granted §7the chat color "+chatColorExistingColor+chatColor+"§7.");
                    return;
                }
            }
            if (chatColorExisting != null) {
                grantChatColorArray.add(chatColor);
                System.out.println(grantChatColorArray.size());
                sender.sendMessage("§7You §2successfully §7granted §9"+playerName+" §7the "+chatColorExistingColor+chatColor+" §7chat color.");
            } else {
                sender.sendMessage("§7The chat color §9"+chatColor+" §7doesn't exist.");
            }


            String updateChatColor = "UPDATE items " +
                    "SET ownedChatColors = '"+grantChatColorArray+"'" +
                    " WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            statement.executeUpdate(updateChatColor);
            grantChatColorArray.clear();
        } catch (SQLException f) {
            f.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
        }
    }

    public void unGrantChatColor(Player p, String playerName, String chatColorName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            uc.check(playerName);
            this.getPlayerChatColorOnUngrant(chatColorName);
            this.getChatColorsGrantedBefore();

            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM items WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            String update_ungrant = null;
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                itemsAlreadyGranted = null;
            } else {
                do {
                    itemsAlreadyGranted = resultSet.getString(7);
                } while (resultSet.next());
            }

            if (grantChatColorArray.size() == 0) {
                p.sendMessage("§7This player doesn't have any §9chat §7colors §cgranted §7to them yet.");
                return;
            } else if (!grantChatColorArray.contains(chatColorName)) {
                p.sendMessage("§7The §9"+chatColorName+" §7chat color §7couldn't be §cfound §7in player chat color list.");
                return;
            }

            grantChatColorArray.remove(chatColorName);

            if (grantChatColorArray.size() < 1) {
                update_ungrant = "UPDATE items " +
                        "SET ownedChatColors = NULL" +
                        " WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            } else {
                update_ungrant = "UPDATE items " +
                        "SET ownedChatColors = '"+grantChatColorArray+"'" +
                        " WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            }
            statement.executeUpdate(update_ungrant);
            this.resetChatColor(UUID.fromString(UUIDChecker.uuid));
            grantChatColorArray.clear();
            p.sendMessage("§7You §2successfully §7ungranted §7the chat color "+currentChatColor+chatColorName+" §7from §9"+UUIDChecker.uuidName+"§7.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
        }
    }

    public void resetChatColorsOnUnGrantingRank() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql2 = "UPDATE players " +
                    "SET chatColor = NULL"+
                    " WHERE playerUUID = '"+UUIDChecker.uuid+"'";

            statement.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
        }
    }

    public void unGrantChatColorConsole(CommandSender sender, String playerName, String chatColorName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            uc.check(playerName);
            this.getPlayerChatColorOnUngrant(chatColorName);
            this.getChatColorsGrantedBefore();

            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM items WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            String update_ungrant = null;
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                itemsAlreadyGranted = null;
            } else {
                do {
                    itemsAlreadyGranted = resultSet.getString(7);
                } while (resultSet.next());
            }

            if (grantChatColorArray.size() == 0) {
                sender.sendMessage("§7This player doesn't have any §9chat §7colors §cgranted §7to them yet.");
                return;
            } else if (!grantChatColorArray.contains(chatColorName)) {
                sender.sendMessage("§7The §9"+chatColorName+" §7chat color §7couldn't be §cfound §7in player chat color list.");
                return;
            }

            grantChatColorArray.remove(chatColorName);

            if (grantChatColorArray.size() < 1) {
                update_ungrant = "UPDATE items " +
                        "SET ownedChatColors = NULL" +
                        " WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            } else {
                update_ungrant = "UPDATE items " +
                        "SET ownedChatColors = '"+grantChatColorArray+"'" +
                        " WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            }
            statement.executeUpdate(update_ungrant);
            this.resetChatColor(UUID.fromString(UUIDChecker.uuid));
            grantChatColorArray.clear();
            sender.sendMessage("§7You §2successfully §7ungranted §7the chat color "+currentChatColor+chatColorName+" §7from §9"+UUIDChecker.uuidName+"§7.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
        }
    }

    public void resetChatColor(UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String updateChatColor = "UPDATE players " +
                    "SET chatColor = NULL" +
                    " WHERE playerUUID = '"+uuid+"'";
            statement.executeUpdate(updateChatColor);

        } catch (SQLException f) {
            f.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
        }
    }
}