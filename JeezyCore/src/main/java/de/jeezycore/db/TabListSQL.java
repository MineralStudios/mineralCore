package de.jeezycore.db;

import org.bukkit.entity.Player;
import java.sql.*;
import java.util.Arrays;
import java.util.UUID;

import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static de.jeezycore.utils.ArrayStorage.*;

public class TabListSQL {
    public String url;
    public String user;
    public String password;
    String rankColor;
    String getUsers;
    public static String getTabListRanks;
    public static Integer getTabListPriority;
    String getTabListPerms;
    String [] getTabListPerms_Array;
    String getTabListRankColor;
    RanksSQL ranksSQL = new RanksSQL();

    public void getTabListData(UUID uuid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ranksSQL.getPlayerInformation(uuid);
            String sql = "SELECT * FROM ranks WHERE rankName = '"+ RanksSQL.rankNameInformation +"'";
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                getTabListRanks = null;
                getTabListPriority = null;
                rankColor = null;
            } else {
                do {
                    getTabListRanks = resultSet.getString("rankName");
                    getTabListPriority = resultSet.getInt("rankPriority");
                    rankColor = resultSet.getString(3);
                } while(resultSet.next());

                if (rankColor == null) {
                return;
                }

                tab_name_list_array.put(uuid, rankColor);
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

    public void getTabListRanks() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT * FROM ranks ORDER BY rankPriority ASC";
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                getTabListRanks = null;
            } else {
                do {
                    getTabListRanks = resultSet.getString("rankName");
                    getTabListPriority = resultSet.getInt("rankPriority");
                    getTabListRankColor = resultSet.getString("rankColor");
                    rankTabListSorting.put(getTabListPriority+""+getTabListRanks, getTabListRankColor);
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

    public void getTabListPerms() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT rankPerms FROM ranks WHERE rankPerms IS NOT NULL ORDER BY rankPriority DESC";
            resultSet = statement.executeQuery(sql);


            if (!resultSet.next()) {
                getTabListPerms = null;
            } else {
                do {
                        getTabListPerms = resultSet.getString("rankPerms");
                        getTabListPerms_Array = getTabListPerms.replace("]", "").replace("[", "").split(", ");
                        rankTabListPerms.addAll(Arrays.asList(getTabListPerms_Array));
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


    public void getUsers() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT * FROM players P1 INNER JOIN ranks R1 ON P1.rank = R1.rankName ORDER BY rankPriority DESC";
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                getUsers = null;
            } else {
                do {
                    getUsers = resultSet.getString(1);
                    playerRankNames.add(getUsers);
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
}