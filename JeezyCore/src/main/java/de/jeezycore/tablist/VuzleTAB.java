package de.jeezycore.tablist;

import de.jeezycore.main.Main;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

import static de.jeezycore.utils.ArrayStorage.playerRankNames;
import static de.jeezycore.utils.ArrayStorage.rankTabListPerms;


public class VuzleTAB implements Listener {

    private final MinecraftServer server = MinecraftServer.getServer();
    private final PlayerInteractManager interactManager = new PlayerInteractManager(server.getWorldServer(0));


    public void addPlayersBackToList (Player e) {

        CraftPlayer craftPlayer = (CraftPlayer) e.getPlayer(); // CraftBukkit
        EntityPlayer entityPlayer = craftPlayer.getHandle(); // NMS - net minecraft server

    if (!rankTabListPerms.isEmpty()) {
        for (int i = 0; i < rankTabListPerms.size(); i++) {
            if (e.getPlayer().hasPermission(rankTabListPerms.get(i)) && !e.getPlayer().isOp() && server.getPlayerList().players.size() > i || e.getPlayer().isOp() && e.getPlayer().hasPermission("vuzle.tab.first")
                    && server.getPlayerList().players.size() > i) {
                server.getPlayerList().players.remove(entityPlayer);
                server.getPlayerList().players.add(i, entityPlayer);
            }
        }
    }


/*
        if (e.getPlayer().hasPermission("vuzle.tab.second") &&
                server.getPlayerList().players.size() > 1 && !e.getPlayer().isOp()) {
            server.getPlayerList().players.remove(entityPlayer);
            server.getPlayerList().players.add(1, entityPlayer);
        }
 */
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, server.getPlayerList().players));
        }
    }
    public void removePlayersFromList () {

        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, server.getPlayerList().players));
        }
    }

    public void removePlayersFromListOnQuit (PlayerQuitEvent e, EntityPlayer entityPlayer) {

        if (playerRankNames.contains(e.getPlayer().getDisplayName())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    removePlayersFromList ();
                    server.getPlayerList().players.remove(entityPlayer);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, server.getPlayerList().players));
                    }
                }
            }.runTaskLater(Main.getPlugin(Main.class), 12L);
        }
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

}