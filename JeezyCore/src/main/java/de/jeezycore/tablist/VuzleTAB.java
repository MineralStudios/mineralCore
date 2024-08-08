package de.jeezycore.tablist;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public class VuzleTAB implements Listener {

    private final MinecraftServer server = MinecraftServer.getServer();
    private final PlayerInteractManager interactManager = new PlayerInteractManager(server.getWorldServer(0));


    public void addPlayersBackToList (Player e) {

        CraftPlayer craftPlayer = (CraftPlayer) e.getPlayer(); // CraftBukkit
        EntityPlayer entityPlayer = craftPlayer.getHandle(); // NMS - net minecraft server


        if (e.getPlayer().hasPermission("vuzle.tab.first") || e.getPlayer().isOp()) {
            server.getPlayerList().players.remove(entityPlayer);
            server.getPlayerList().players.add(0, entityPlayer);
        }

        if (e.getPlayer().hasPermission("vuzle.tab.second") &&
                server.getPlayerList().players.size() > 1 && !e.getPlayer().isOp()) {
            server.getPlayerList().players.remove(entityPlayer);
            server.getPlayerList().players.add(1, entityPlayer);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, server.getPlayerList().players));
        }
    }
    public void removePlayersFromList () {

        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, server.getPlayerList().players));
        }
    }
}