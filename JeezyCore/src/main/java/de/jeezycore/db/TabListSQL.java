package de.jeezycore.db;

import de.jeezycore.config.JeezyConfig;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.configuration.MemorySection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.sql.*;

import static de.jeezycore.utils.ArrayStorage.tab_name_list_array;

public class TabListSQL {
    public String url;
    public String user;
    public String password;
    String rankColor;

    JeezySQL jeezySQL = new JeezySQL();

    private void createConnection() {
        MemorySection mc = (MemorySection) JeezyConfig.database_defaults.get("MYSQL");
        url = "jdbc:mysql://"+mc.get("ip")+":"+mc.get("mysql-port")+"/"+mc.get("database");
        user = (String) mc.get("user");
        password = (String) mc.get("password");
    }


    public void setTabList1_8(Player p, String Title, String subTitle) {
        IChatBaseComponent tabTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Title+ "\"}");
        IChatBaseComponent tabSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subTitle + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabTitle);

        try {
            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, tabSubTitle);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        }
    }


    public void getTabListData(Player p) {
        try {
            this.createConnection();
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stm = con.createStatement();
            jeezySQL.getPlayerInformation(p);
            String sql = "SELECT * FROM ranks WHERE rankName = '"+jeezySQL.rankNameInformation+"'";
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                rankColor = rs.getString(3);

                if (rankColor == null) {
                return;
                }

                tab_name_list_array.put(p, rankColor);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


}