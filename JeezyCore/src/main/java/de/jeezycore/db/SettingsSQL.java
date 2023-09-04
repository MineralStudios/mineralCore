package de.jeezycore.db;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.Arrays;
import java.util.UUID;
import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static de.jeezycore.utils.ArrayStorage.*;

public class SettingsSQL {

    public String playerUUID;

    String settingsAvailable;

    public String settingsIgnoredList;

    public boolean friendsRequests;

    public boolean settingsMsg;

    public boolean settingsPmSound;

    public boolean settingsFriendsSound;
    

    public void setup(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM settings WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";
            resultSet = statement.executeQuery(sql_select);
            if (!resultSet.next()) {
                settingsAvailable = null;
            } else {
                do {
                    settingsAvailable = resultSet.getString(2);
                } while (resultSet.next());
            }
            if (settingsAvailable == null) {
                String sql_settings_available ="INSERT INTO settings" +
                        "(playerName, playerUUID, ignoredPlayerList, msg, friendsRequests, pmSound, friendsSound) " +
                        "VALUES ('"+p.getDisplayName()+"', '"+p.getUniqueId()+"', " +
                        "NULL, true, true, true, true)";
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

    public void getSettingsData(UUID receiver) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql_select = "SELECT * FROM settings WHERE playerUUID = '"+receiver+"'";
            resultSet = statement.executeQuery(sql_select);

            if (!resultSet.next()) {
                playerUUID = null;
                settingsIgnoredList = null;
                settingsMsg = false;
                friendsRequests = false;
                settingsPmSound = false;
                settingsFriendsSound = false;
            } else {
                do {
                    playerUUID = resultSet.getString("playerUUID");
                    settingsIgnoredList = resultSet.getString(3);
                    friendsRequests = resultSet.getBoolean("friendsRequests");
                    settingsMsg = resultSet.getBoolean(4);
                    settingsPmSound = resultSet.getBoolean("pmSound");
                    settingsFriendsSound = resultSet.getBoolean("friendsSound");
                } while (resultSet.next());
            }

            if (settingsIgnoredList != null) {
                String [] convertStringArray = settingsIgnoredList.replace("[", "").replace("]", "").split(", ");
                msg_ignore_array.addAll(Arrays.asList(convertStringArray));
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


    public void disableMsg(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.setup(p);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdateMsg = "UPDATE settings " +
                    "SET msg = false"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";
            statement.executeUpdate(sqlUpdateMsg);
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

    public void enableMsg(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.setup(p);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdateMsg = "UPDATE settings " +
                    "SET msg = true"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";
            statement.executeUpdate(sqlUpdateMsg);
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

    public void addIgnore(Player p, String playerToAdd) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.setup(p);
            this.getSettingsData(p.getUniqueId());

            connection = dataSource.getConnection();
            statement = connection.createStatement();
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
            statement.executeUpdate(update_sql);
            msg_ignore_array.clear();
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

    public void removeIgnore(Player p, String playerToRemove) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.setup(p);
            this.getSettingsData(p.getUniqueId());

            connection = dataSource.getConnection();
            statement = connection.createStatement();
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
            statement.executeUpdate(update_sql);
            msg_ignore_array.clear();
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

    public void showIgnoreList(Player p) {
        try {
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

    public void enablePmSound(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.setup(p);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdatePmSound = "UPDATE settings " +
                    "SET pmSound = true"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";
            statement.executeUpdate(sqlUpdatePmSound);
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

    public void disablePmSound(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.setup(p);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdatePmSound = "UPDATE settings " +
                    "SET pmSound = false"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";
            statement.executeUpdate(sqlUpdatePmSound);
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

    public void enableFriendsSound(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.setup(p);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdatePmSound = "UPDATE settings " +
                    "SET friendsSound = true"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";
            statement.executeUpdate(sqlUpdatePmSound);
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

    public void disableFriendsSound(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.setup(p);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlUpdatePmSound = "UPDATE settings " +
                    "SET friendsSound = false"+
                    " WHERE playerUUID = '"+p.getUniqueId()+"'";
            statement.executeUpdate(sqlUpdatePmSound);
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
}