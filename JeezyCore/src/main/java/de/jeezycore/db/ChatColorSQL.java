package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import static de.jeezycore.utils.ArrayStorage.tags_in_ownership_array;


public class ChatColorSQL {

    String url;
    String user;
    String password;

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

    public ArrayList<String> chatColorArray = new ArrayList<>();

    public ArrayList<String> grantChatColorArray = new ArrayList<>();


    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    private void setup() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM items WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            ResultSet rs = stm.executeQuery(sql_select);
            if (!rs.next()) {
                itemsAvailable = null;
                itemsAlreadyGranted = null;
            } else {
                do {
                    itemsAvailable = rs.getString(2);
                    itemsAlreadyGranted = rs.getString(7);
                } while (rs.next());
            }
            if (itemsAvailable == null) {
                String sql_settings_available ="INSERT INTO items" +
                        "(playerName, playerUUID) " +
                        "VALUES ('"+UUIDChecker.uuidName+"', '"+UUIDChecker.uuid+"')";
                stm.executeUpdate(sql_settings_available);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void create(Player p, String colorName, String color, String colorRGB, int colorPriority) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM chatColors WHERE colorName = '"+colorName+"'";
            ResultSet rs = stm.executeQuery(sql_select);

            if (!rs.next()) {
                chatColorName = null;
            } else {
                do {
                    chatColorName = rs.getString(1);
                } while (rs.next());
            }

            if (chatColorName == null) {
                String sql_chatColor_insert ="INSERT INTO chatColors" +
                        "(colorName, color, colorRGB, colorPriority) " +
                        "VALUES ('"+colorName+"', '"+color+"', " +
                        "'"+colorRGB+"', '"+colorPriority+"')";
                stm.executeUpdate(sql_chatColor_insert);
                p.sendMessage("§7You §2successfully §7created the chatColor "+color+colorName+"§7.");
            } else {
                p.sendMessage("§7The chatColor "+color+colorName+" §calready §7exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getChatColorsData() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM chatColors ORDER BY colorPriority DESC";
            ResultSet rs = stm.executeQuery(sql_select);

            while (rs.next()) {
                colorName = rs.getString(1);
                color = rs.getString(2);
                colorRGB = rs.getString(3);

                chatColorArray.add(Arrays.asList(colorName, color, colorRGB).toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getPlayerChatName(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT chatColor FROM players WHERE playerUUID = '"+p.getUniqueId()+"'";
            ResultSet rs = stm.executeQuery(sql_select);
            if (!rs.next()) {
                currentChatColorName = null;
            } else {
                do {
                    currentChatColorName = rs.getString(1);
                } while (rs.next());
            }
            this.getPlayerChatColor(p);
        } catch (SQLException f) {
            f.printStackTrace();
        }
    }

    public void getPlayerChatColor(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM chatColors WHERE colorName = '"+currentChatColorName+"'";
            ResultSet rs = stm.executeQuery(sql_select);
            if (!rs.next()) {
                currentChatColor = null;
            } else {
                do {
                    currentChatColor = rs.getString(2);
                } while (rs.next());
            }
        } catch (SQLException f) {
            f.printStackTrace();
        }
    }

    public void setChatColor(org.bukkit.event.inventory.InventoryClickEvent e) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

                String updateChatColor = "UPDATE players " +
                        "SET chatColor = '"+e.getCurrentItem().getItemMeta().getDisplayName().substring(2)+"'" +
                        " WHERE playerUUID = '"+e.getWhoClicked().getUniqueId()+"'";
                stm.executeUpdate(updateChatColor);
        } catch (SQLException f) {
            f.printStackTrace();
        }
    }

    public void getChatColorsGrantedBefore(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM items WHERE playerUUID = '"+UUIDChecker.uuid+"'";
            ResultSet rs = stm.executeQuery(sql_select);
            if (!rs.next()) {
                itemsAlreadyGranted = null;
            } else {
                do {
                    itemsAlreadyGranted = rs.getString(7);
                } while (rs.next());
            }

            if (itemsAlreadyGranted != null) {
                String[] get_owned_tags = itemsAlreadyGranted.replace("[", "").replace("]", "").split(", ");
                grantChatColorArray.addAll(Arrays.asList(get_owned_tags));
            }
        } catch (SQLException f) {
            f.printStackTrace();
        }
    }

    public void grantChatColor(Player p, String playerName, String chatColor) {
        try {
            UUIDChecker uc = new UUIDChecker();
            uc.check(playerName);
            this.setup();
            this.getChatColorsGrantedBefore(p);
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM chatColors WHERE colorName = '"+chatColor+"'";
            ResultSet rs = stm.executeQuery(sql_select);
            if (!rs.next()) {
                chatColorExisting = null;
            } else {
                do {
                    chatColorExisting = rs.getString(1);
                    chatColorExistingColor = rs.getString(2);
                } while (rs.next());
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
            stm.executeUpdate(updateChatColor);
            grantChatColorArray.clear();
        } catch (SQLException f) {
            f.printStackTrace();
        }
    }

    public void resetChatColor(org.bukkit.event.inventory.InventoryClickEvent e) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String updateChatColor = "UPDATE players " +
                    "SET chatColor = NULL" +
                    " WHERE playerUUID = '"+e.getWhoClicked().getUniqueId()+"'";
            stm.executeUpdate(updateChatColor);
        } catch (SQLException f) {
            f.printStackTrace();
        }
    }
}