package de.jeezycore.commands.basic.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.jeezycore.main.Main;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import static de.jeezycore.utils.ArrayStorage.*;

public class CNick implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.cnick") || p.hasPermission("jeezy.core.nick.*")) {
            if (cmd.getName().equalsIgnoreCase("cnick")) {
                    if (playerNickedList.containsKey(p.getUniqueId())) {
                        clearNick(p, playerNickedList.get(p.getUniqueId()));
                        nickAgainTimer(p);
                        playerNickedList.remove(p.getUniqueId());
                        p.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §7You §9successfully §7removed your §9nick§7!");
                    } else {
                        p.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §7You need to §9nick §7yourself first with ./§9nick§7.");
                    }

            } else {
                p.sendMessage("Usage: /cnick");
            }
            } else {
                p.sendMessage("No permission.");
            }
        }

        return true;
    }

    private static void clearNick(Player player, String realName) {
        changeNameWithReflectionFor1_8(player, realName);
        updateScoreboard(player, realName);
        refreshPlayer(player);
    }


    private static void changeNameWithReflectionFor1_8(Player player, String realName) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getProfile();

        try {
            Field nameField = GameProfile.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(profile, realName);
        } catch (Exception e) {
            return;
        }

        try {
            Field profileField = EntityHuman.class.getDeclaredField("bH");
            profileField.setAccessible(true);
            profileField.set(entityPlayer, profile);
        } catch (Exception e) {
        }

        // Restore the player's skin back to default
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", originalTextures.get(player.getUniqueId()), originalSignatures.get(player.getUniqueId())));
    }


    private static void updateScoreboard(Player player, String realName) {
        Team team = player.getScoreboard().getTeam("ZNick");
        if (team != null) {
            team.removeEntry(player.getName());
        }
        player.setDisplayName(realName);
    }


    private static void refreshPlayer(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        // Remove player from the tab list
        PacketPlayOutPlayerInfo removePacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        sendPacketToAll(removePacket);

        // Destroy the player's entity for all players (force skin refresh)
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(player.getEntityId());
        sendPacketToAll(destroyPacket);

        // Teleport the player slightly to force reloading their entity (can be same location)
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            player.teleport(player.getLocation().add(0, 0.1, 0)); // Small Y teleport to trigger a reload

            // Re-add the player to the tab list
            PacketPlayOutPlayerInfo addPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
            sendPacketToAll(addPacket);

            // Re-send entity spawn packets
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.equals(player)) {
                    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
                }
            }

            // Send a metadata update packet (final refresh)
            PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(player.getEntityId(), entityPlayer.getDataWatcher(), true);
            sendPacketToAll(metadataPacket);
        }, 5L); // Small delay to allow packets to process
    }

    private static void sendPacketToAll(Packet<?> packet) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void nickAgainTimer(Player p) {
        playerOnNickCoolDownArray.add(p.getUniqueId());
        Timer time = new Timer();
        TimerTask nickAgainTask = new TimerTask() {
            @Override
            public void run() {
                playerOnNickCoolDownArray.remove(p.getPlayer().getUniqueId());
                originalTextures.remove(p.getUniqueId());
                originalSignatures.remove(p.getUniqueId());
                time.purge();
                time.cancel();
            }
        };
        time.schedule(nickAgainTask, 30000);
    }

}
