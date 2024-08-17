package de.jeezycore.db;

import org.bukkit.entity.Player;
import java.sql.*;
import java.util.UUID;
import static de.jeezycore.db.hikari.HikariCP.dataSource;

public class SyncSQL {

    private String syncCode;
    private String linked;
    private String codeDuplicated;


    public void createCode (Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String syncCode = UUID.randomUUID().toString().replace("-", "").substring(4, 14);

            if (checkIfCodeDuplicated(syncCode) != null) return;

            String select_sql ="INSERT INTO sync" +
                    "(playerUUID, syncCode) " +
                    "VALUES ('"+ p.getPlayer().getUniqueId() + "', '"+ syncCode +"')";
            statement.executeUpdate(select_sql);
            p.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §2§lSuccess§f! Go to our §9discord §fand do §f§l/sync §9"+syncCode+"§f.");
        } catch (SQLException e) {
            p.getPlayer().sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §fYou have §4already §fgenerated a key §9/sync §fcode to look it up again!");
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void showCode (Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String select_sql = "SELECT syncCode FROM sync WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";

            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                syncCode = null;
            }  else {
                do {
                    syncCode = resultSet.getString(1);
                } while (resultSet.next());
            }
            if (syncCode != null) {
                p.getPlayer().sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §fYour code is: §9"+syncCode+"§f. §fGo to our discord and do §9/sync §f(code).");
            } else {
                p.getPlayer().sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §fYou §4haven't §flinked your account yet. Do §9/sync §fto do so.");
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

    public void unLink (Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            if (checkIfAlreadyRemoved(p) == null) {
                p.getPlayer().sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §fYou §4haven't §flinked your account yet. Do §9/sync §fto do so.");
                return;
            }

            String select_sql ="DELETE FROM sync WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";
            statement.executeUpdate(select_sql);
            p.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §fYou §2§lsuccessfully§f §funlinked your account from §9discord§f.");

        } catch (SQLException e) {
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String checkIfAlreadyRemoved (Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String select_sql = "SELECT * FROM sync WHERE playerUUID = '"+p.getPlayer().getUniqueId()+"'";

            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                linked = null;
            }  else {
                do {
                    linked = resultSet.getString(1);
                } while (resultSet.next());
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
        return linked;
    }

    private String checkIfCodeDuplicated (String code) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String select_sql = "SELECT * FROM sync WHERE syncCode = '"+code+"'";

            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                codeDuplicated = null;
            }  else {
                do {
                    codeDuplicated = resultSet.getString(1);
                } while (resultSet.next());
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
        return codeDuplicated;
    }
}