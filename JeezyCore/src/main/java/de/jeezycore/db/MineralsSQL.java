package de.jeezycore.db;

import com.google.common.base.Splitter;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.FakePlayerChecker;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.Map;
import java.util.UUID;

import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static de.jeezycore.utils.ArrayStorage.mineralsStorage;

public class MineralsSQL {

    public String url;
    public String user;
    public String password;
    public String mineralsData;

    
    public void start() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql = "INSERT INTO minerals " +
                    "(serverName, minerals_data) " +
                    "VALUES " +
                    "('mineral', null)";

            statement.executeUpdate(sql);
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

    public void mineralsData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT * FROM minerals WHERE serverName = 'mineral'";
            resultSet = statement.executeQuery(select_sql);
            while (resultSet.next()) {
                mineralsData = resultSet.getString(2);

                if (mineralsData != null) {
                    String replacedMineralsData = mineralsData.replace("{", "").replace("}", "");
                    Map<String, String> properties = Splitter.on(", ")
                            .withKeyValueSeparator("=")
                            .split(replacedMineralsData);

                    System.out.println(properties);

                    ArrayStorage.mineralsStorage.putAll(properties);
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

    public String minerals(String uuid) {
        mineralsData();
        String getCurrentMinerals;
        if (mineralsStorage.containsKey(UUID.fromString(uuid).toString())) {
            getCurrentMinerals = mineralsStorage.get(UUID.fromString(uuid).toString());
        } else {
            getCurrentMinerals = String.valueOf(0);
        }

        return getCurrentMinerals;
    }

    public void updateMineralsData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String sql = "UPDATE minerals " +
                    "SET minerals_data = '" + ArrayStorage.mineralsStorage + "'" +
                    " WHERE serverName = 'mineral'";
            statement.executeUpdate(sql);
        } catch (Exception e) {
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

    public void addMinerals(Player player, String uuid, int amount, String message) {
        if (FakePlayerChecker.isFakePlayer(player))
            return;

        if (UUIDChecker.uuid == null) {
            player.sendMessage("§7This player doesn't §4exist§7.");
            return;
        }
        mineralsData();
        if (mineralsStorage.containsKey(UUID.fromString(uuid).toString())) {
            String getCurrentMinerals = mineralsStorage.get(UUID.fromString(uuid).toString());
            int count = Integer.parseInt(getCurrentMinerals) + amount;
            mineralsStorage.put(UUID.fromString(uuid).toString(), String.valueOf(count));
        } else {
            mineralsStorage.put(UUID.fromString(uuid).toString(), String.valueOf(amount));
        }

        player.sendMessage(message.replace("&", "§"));

        updateMineralsData();
        ArrayStorage.mineralsStorage.clear();
    }

    public void removeMinerals(Player player, String p, int amount) {
        if (UUIDChecker.uuid == null) {
            player.sendMessage("§7This player doesn't §4exist§7.");
            return;
        }
        mineralsData();
        if (mineralsStorage.containsKey(UUID.fromString(p).toString())) {
            String getCurrentMinerals = mineralsStorage.get(UUID.fromString(p).toString());
            int count = Integer.parseInt(getCurrentMinerals) - amount;
            if (count < 0) {
                count = 0;
            }
            mineralsStorage.put(UUID.fromString(p).toString(), String.valueOf(count));
        } else {
            mineralsStorage.put(UUID.fromString(p).toString(), String.valueOf(0));
        }

        player.sendMessage("§7You §2successfully §7removed §9" + amount + " §fminerals §7from §9§l"
                + UUIDChecker.uuidName + "§7.");

        updateMineralsData();
        ArrayStorage.mineralsStorage.clear();
    }

    public void mineralsBalance(Player p) {
        try {
            ArrayStorage.mineralsStorage.clear();
            mineralsData();

            String message;

            if (ArrayStorage.mineralsStorage.get(p.getPlayer().getUniqueId().toString()) != null) {
                message = "§9§lYou §7§lhave: §f§l"
                        + ArrayStorage.mineralsStorage.get(p.getPlayer().getUniqueId().toString())
                        + " §9§lminerals§7§l.";
            } else {
                message = "§9§lYou §7§lhave: §f§l0 §9§lminerals§7§l.";
            }
            System.out.println(ArrayStorage.mineralsStorage);
            p.sendMessage(message);
            mineralsData = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}