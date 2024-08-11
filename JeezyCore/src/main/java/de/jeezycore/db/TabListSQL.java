package de.jeezycore.db;

import org.bukkit.entity.Player;
import java.sql.*;
import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static de.jeezycore.utils.ArrayStorage.playerRankNames;
import static de.jeezycore.utils.ArrayStorage.tab_name_list_array;

public class TabListSQL {
    public String url;
    public String user;
    public String password;
    String rankColor;
    String getUsers;
    RanksSQL ranksSQL = new RanksSQL();

    public void getTabListData(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ranksSQL.getPlayerInformation(p);
            String sql = "SELECT * FROM ranks WHERE rankName = '"+ RanksSQL.rankNameInformation +"'";
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                rankColor = null;
            } else {
                do {
                    rankColor = resultSet.getString(3);
                } while(resultSet.next());

                if (rankColor == null) {
                return;
                }

                tab_name_list_array.put(p, rankColor);
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