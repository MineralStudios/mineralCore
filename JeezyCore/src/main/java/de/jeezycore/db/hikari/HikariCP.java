package de.jeezycore.db.hikari;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;

public class HikariCP {

    private static final MemorySection db = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");

    public static HikariDataSource dataSource = null;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://"+db.get("ip")+":"+db.get("mysql-port")+"/"+db.get("database"));
        config.setUsername((String) db.get("user"));
        config.setPassword((String) db.get("password"));
        config.setMaximumPoolSize(25);
        config.setConnectionTimeout(300000);
        config.setConnectionTimeout(120000);
        config.setLeakDetectionThreshold(300000);

        dataSource = new HikariDataSource(config);
    }

    /*

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from tblemployee");
            while (resultSet.next()) {
                System.out.println("empId:" + resultSet.getInt("empId"));
                System.out.println("empName:" + resultSet.getString("empName"));
                System.out.println("dob:" + resultSet.getDate("dob"));
                System.out.println("designation:" + resultSet.getString("designation"));
            }
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

     */
}