package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatColorSQL {

    String url;
    String user;
    String password;

    String chatColorName;

    String colorName;
    String color;
    String colorRGB;

    public static String currentChatColorName;

    public static String currentChatColor;

    public ArrayList<String> chatColorArray = new ArrayList<>();


    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
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

    public void Granting(org.bukkit.event.inventory.InventoryClickEvent e) {
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