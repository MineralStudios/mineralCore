package de.jeezycore.db;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.sql.*;

import static de.jeezycore.db.hikari.HikariCP.dataSource;
import static de.jeezycore.utils.ArrayStorage.tab_name_list_array;

public class TabListSQL {
    public String url;
    public String user;
    public String password;
    String rankColor;

    RanksSQL ranksSQL = new RanksSQL();
    
    
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
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ranksSQL.getPlayerInformation(p);
            String sql = "SELECT * FROM ranks WHERE rankName = '"+ ranksSQL.rankNameInformation+"'";
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
}