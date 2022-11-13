package de.jeezycore.db;

import com.google.gson.JsonArray;
import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class LogsSQL {

    public String url;
    public String user;
    public String password;

    public static String ban_log;
    public static JSONObject ban_log_json = new JSONObject();

    private void createConnection() {

        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

        url = "jdbc:mysql://localhost:3306/" + mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }

    public void punishment_log(UUID userUUID) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            String select_sql = "SELECT * FROM punishments WHERE UUID = '" +userUUID+"'";
            ResultSet rs = stm.executeQuery(select_sql);
            while (rs.next()) {
                ban_log = rs.getString(10);
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
