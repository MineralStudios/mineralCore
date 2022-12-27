package de.jeezycore.db;

import com.google.common.base.Splitter;
import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.Map;
import java.util.UUID;

import static de.jeezycore.utils.ArrayStorage.mineralsStorage;


public class MineralsSQL {

    public String url;
    public String user;
    public String password;
    public String mineralsData;

    private void createConnection() {
        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void start() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

            String sql = "INSERT INTO minerals " +
                    "(serverName, minerals_data) " +
                    "VALUES " +
                    "('mineral', null)";

            stm.executeUpdate(sql);
            con.close();
        } catch (SQLException e) {
        }
    }

    public void mineralsData() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM minerals WHERE serverName = 'mineral'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                mineralsData = rs.getString(2);

                if (mineralsData != null) {
                    String replacedMineralsData = mineralsData.replace("{", "").replace("}", "");
                    Map<String, String> properties = Splitter.on(", ")
                            .withKeyValueSeparator("=")
                            .split(replacedMineralsData);

                    System.out.println(properties);

                    ArrayStorage.mineralsStorage.putAll(properties);
                }
            }
            rs.close();
            stm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMineralsData() {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();

                String sql = "UPDATE minerals " +
                        "SET minerals_data = '"+ ArrayStorage.mineralsStorage +"'"+
                        " WHERE serverName = 'mineral'";
                stm.executeUpdate(sql);

            stm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMinerals(Player player, String p, int amount) {
        if (UUIDChecker.uuid == null) {
            player.sendMessage("§7This player doesn't §4exist§7.");
            return;
        }
        mineralsData();
        if(mineralsStorage.containsKey(UUID.fromString(p).toString())) {
            String getCurrentMinerals = mineralsStorage.get(UUID.fromString(p).toString());
            int count = Integer.parseInt(getCurrentMinerals) + amount;
            mineralsStorage.put(UUID.fromString(p).toString(), String.valueOf(count));
        } else {
            mineralsStorage.put(UUID.fromString(p).toString(), String.valueOf(amount));
        }

        player.sendMessage("§7You §2successfully §7added §9"+amount+" §fminerals §7to §9§l"+ UUIDChecker.uuidName+"§7.");

            updateMineralsData();
            ArrayStorage.mineralsStorage.clear();
    }

    public void removeMinerals(Player player, String p, int amount) {
        if (UUIDChecker.uuid == null) {
            player.sendMessage("§7This player doesn't §4exist§7.");
            return;
        }
        mineralsData();
        if(mineralsStorage.containsKey(UUID.fromString(p).toString())) {
            String getCurrentMinerals = mineralsStorage.get(UUID.fromString(p).toString());
            int count = Integer.parseInt(getCurrentMinerals) - amount;
            if (count < 0) {
                count = 0;
            }
            mineralsStorage.put(UUID.fromString(p).toString(), String.valueOf(count));
        } else {
            mineralsStorage.put(UUID.fromString(p).toString(), String.valueOf(0));
        }

        player.sendMessage("§7You §2successfully §7removed §9"+amount+" §fminerals §7from §9§l"+ UUIDChecker.uuidName+"§7.");

        updateMineralsData();
        ArrayStorage.mineralsStorage.clear();
    }

    public void mineralsBalance(Player p) {
        try {
            ArrayStorage.mineralsStorage.clear();
             mineralsData();

             String message;

                if (ArrayStorage.mineralsStorage.get(p.getPlayer().getUniqueId().toString()) != null) {
                    message = "§9§lYou §7§lhave: §f§l"+ArrayStorage.mineralsStorage.get(p.getPlayer().getUniqueId().toString())+" §9§lminerals§7§l.";
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