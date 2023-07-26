package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import java.sql.*;
import java.util.UUID;

import static de.jeezycore.db.hikari.HikariCP.dataSource;

public class LogsSQL {

    public String url;
    public String user;
    public String password;

    public static String ban_log;
    public static String mute_log;
    

    public void punishment_log(UUID userUUID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String select_sql = "SELECT * FROM punishments WHERE UUID = '" +userUUID+"'";
            resultSet = statement.executeQuery(select_sql);
            while (resultSet.next()) {
                ban_log = resultSet.getString(11);
                mute_log = resultSet.getString(12);
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

    public void refreshData() {
        ban_log = null;
        mute_log = null;
    }

}
