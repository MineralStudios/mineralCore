package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Arrays;
import java.util.UUID;

import static de.jeezycore.utils.ArrayStorage.*;

public class SettingsSQL {

    String url;
    String user;
    String password;

    String settingsAvailable;

    public String settingsIgnoredList;
    public boolean settingsMsg;

    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    private void setup(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM settings WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";
            ResultSet rs = stm.executeQuery(sql_select);
            if (!rs.next()) {
                settingsAvailable = null;
            } else {
                do {
                    settingsAvailable = rs.getString(2);
                } while (rs.next());
            }
            if (settingsAvailable == null) {
                String sql_settings_available ="INSERT INTO settings" +
                        "(playerName, playerUUID, ignoredPlayerList, msg) " +
                        "VALUES ('"+p.getDisplayName()+"', '"+p.getUniqueId()+"', " +
                        "NULL, false)";
                stm.executeUpdate(sql_settings_available);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSettingsData(UUID receiver) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sql_select = "SELECT * FROM settings WHERE playerUUID = '"+receiver+"'";
            ResultSet rs = stm.executeQuery(sql_select);

            if (!rs.next()) {
                settingsIgnoredList = null;
                settingsMsg = false;
            } else {
                do {
                    settingsIgnoredList = rs.getString(3);
                    settingsMsg = rs.getBoolean(4);
                } while (rs.next());
            }

            if (settingsIgnoredList != null) {
                String [] convertStringArray = settingsIgnoredList.replace("[", "").replace("]", "").split(", ");
                msg_ignore_array.addAll(Arrays.asList(convertStringArray));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void disableMsg(Player p) {
        try {
            this.setup(p);
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sqlUpdateMsg = "UPDATE settings " +
                    "SET msg = false"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";
            stm.executeUpdate(sqlUpdateMsg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void enableMsg(Player p) {
        try {
            this.setup(p);
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String sqlUpdateMsg = "UPDATE settings " +
                    "SET msg = true"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";
            stm.executeUpdate(sqlUpdateMsg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addIgnore(Player p, String playerToAdd) {
        try {
            this.setup(p);
            this.getSettingsData(p.getUniqueId());
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String update_sql;
            Player ps = Bukkit.getPlayer(playerToAdd);

            if (ps == null) {
                p.sendMessage("§9§l"+playerToAdd +" §7isn't online.");
                msg_ignore_array.clear();
                return;
            }

            if (p.getUniqueId().toString().equalsIgnoreCase(ps.getUniqueId().toString())) {
                p.sendMessage("§7You can't §cignore §7yourself.");
                msg_ignore_array.clear();
                return;
            }

            if (msg_ignore_array.contains(ps.getUniqueId().toString())) {
                p.sendMessage("§7You have already §cignored §7that player.");
                msg_ignore_array.clear();
                return;
            }

            if (settingsIgnoredList != null) {
                msg_ignore_array.addAll(Arrays.asList(ps.getDisplayName(), ps.getUniqueId().toString()));
                 update_sql = "UPDATE settings " +
                        "SET ignoredPlayerList = '"+ msg_ignore_array +"'"+
                        " WHERE playerUUID = '" + p.getUniqueId() +"'";
            } else {
                msg_ignore_array.addAll(Arrays.asList(ps.getDisplayName(), ps.getUniqueId().toString()));
                update_sql = "UPDATE settings " +
                        "SET ignoredPlayerList = '"+ msg_ignore_array +"'"+
                        " WHERE playerUUID = '" + p.getUniqueId() +"'";
            }
            p.sendMessage("§7You §2successfully §7ignored §9" + playerToAdd + "§7.");
            stm.executeUpdate(update_sql);
            msg_ignore_array.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeIgnore(Player p, String playerToRemove) {
        try {
            this.setup(p);
            this.getSettingsData(p.getUniqueId());
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String update_sql;

            if (p.getDisplayName().equalsIgnoreCase(playerToRemove)) {
                p.sendMessage("§7You can't §cunignore §7yourself.");
                msg_ignore_array.clear();
                return;
            }

            if (!msg_ignore_array.contains(playerToRemove)) {
                p.sendMessage("§7You haven't §cignored §7the player §9"+playerToRemove+" §7yet.");
                msg_ignore_array.clear();
                return;
            }

            if (msg_ignore_array.size() == 2) {
                update_sql = "UPDATE settings " +
                        "SET ignoredPlayerList = NULL"+
                        " WHERE playerUUID = '" + p.getUniqueId() +"'";
                p.sendMessage("§7You have §2successfully §7removed §9"+playerToRemove+" §7from the ignore list.");
            } else {
                int getIndex = msg_ignore_array.indexOf(playerToRemove);
                msg_ignore_array.remove(getIndex + 1);
                msg_ignore_array.remove(playerToRemove);

                update_sql = "UPDATE settings " +
                        "SET ignoredPlayerList = '"+ msg_ignore_array +"'"+
                        " WHERE playerUUID = '" + p.getUniqueId() +"'";
                p.sendMessage("§7You have §2successfully §7removed §9"+playerToRemove+" §7from the ignore list.");
            }
            stm.executeUpdate(update_sql);
            msg_ignore_array.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showIgnoreList(Player p) {
        try {
            this.createConnection();
            this.getSettingsData(p.getUniqueId());

            if (msg_ignore_array.size() == 0) {
                p.sendMessage("§7You haven't §cignored §7anyone yet.");
                return;
            }

            p.sendMessage(new String[] {
                    "                       ",
                    " §9§lIgnore §f§lList §7(§9§l"+msg_ignore_array.size() / 2 +"§7)\n",
                    "                       ",
            });
        for (int i = 0; i < msg_ignore_array.size(); i++) {
            p.sendMessage(" §9§l♦ §7" + msg_ignore_array.get(i)  + "\n");
            i++;
            System.out.println(i);
        }
        p.sendMessage("                       ");
            msg_ignore_array.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}