package de.jeezycore.db;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.ArrayStorage;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static de.jeezycore.db.hikari.HikariCP.dataSource;

public class StaffSQL {

    public String url;
    public String user;
    public String password;

    JeezySQL jeezySQL = new JeezySQL();
    public static String staffPlayerNames;
    public static boolean staffRank;

    public static ArrayList<String> staff = new ArrayList<>();

    public static ArrayList<String> staffRankNamesArray = new ArrayList<>();

    public static String [] staff_array;

    
    public void addToStaff(String rankName, Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            jeezySQL.getRankData(rankName, p);

            getStaffRank(rankName);

            connection = dataSource.getConnection();
            statement = connection.createStatement();

            if (JeezySQL.permPlayerRankName == null) {
                p.sendMessage("§4This rank hasn't been created yet.");
                return;
            } else if (staffRank) {
                p.sendMessage("§7The "+JeezySQL.permPlayerRankColor.replace("&","§")+JeezySQL.permPlayerRankName+" §7rank is §calready §7a staff rank.");
                return;
            }

            String sql = "UPDATE ranks " +
                    "SET staffRank = true WHERE rankName = '"+rankName+"'";

            statement.executeUpdate(sql);

            p.sendMessage("§bSuccessfully§7 changed the "+JeezySQL.permPlayerRankColor.replace("&","§")+JeezySQL.permPlayerRankName+" §7rank to a staff rank.");
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

    public void removeFromStaff(String rankName, Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            jeezySQL.getRankData(rankName, p);

            getStaffRank(rankName);

            connection = dataSource.getConnection();
            statement = connection.createStatement();

            if (JeezySQL.permPlayerRankName == null) {
                p.sendMessage("§4This rank hasn't been setuped yet.");
                return;
            } else if (!staffRank) {
                p.sendMessage("§7The "+JeezySQL.permPlayerRankColor.replace("&","§")+JeezySQL.permPlayerRankName+" §7rank has been §calready §7removed from the staff ranks.");
                return;
            }

            String sql = "UPDATE ranks " +
                    "SET staffRank = false WHERE rankName = '"+rankName+"'";

            statement.executeUpdate(sql);

            p.sendMessage("§bSuccessfully§7 removed the "+JeezySQL.permPlayerRankColor.replace("&","§")+JeezySQL.permPlayerRankName+" §7rank from the staff ranks.");
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

    public void getStaffRank(String rankName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT staffRank FROM ranks WHERE staffRank = true AND rankName = '"+rankName+"'";
            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                staffRank = false;
            } else {
                do {
                    staffRank = resultSet.getBoolean(1);
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
    }

    public void getStaffRanksInfo() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT rankName FROM ranks WHERE staffRank = true";
            String rankNames;
            resultSet = statement.executeQuery(select_sql);

            if (!resultSet.next()) {
                rankNames = null;
                staffRankNamesArray.clear();
            } else {
                do {
                    rankNames = resultSet.getString(1);
                    staffRankNamesArray.add(rankNames);
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

    public void getStaff() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            this.getStaffRanksInfo();
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            for (String ranks : staffRankNamesArray) {
                String select_sql = "SELECT playerUUID FROM players WHERE rank = '" + ranks + "'";
                resultSet = statement.executeQuery(select_sql);

                if (!resultSet.next()) {
                    staffPlayerNames = null;
                } else {
                    do {
                        staffPlayerNames = resultSet.getString(1);
                        staff_array = staffPlayerNames.replace("]", "").replace("[", "").split(", ");
                        staff.addAll(Arrays.asList(staff_array));
                    } while (resultSet.next());
                }
            };
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

    public void checkIfStaff(String rankName, UUID get_UUID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String select_sql = "SELECT * FROM ranks WHERE rankName = '"+rankName+"'";
            resultSet = statement.executeQuery(select_sql);
            if (!resultSet.next()) {
                staffRank = false;
            } else {
                do {
                    staffRank = resultSet.getBoolean(6);
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
    }

}