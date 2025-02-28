package de.jeezycore.commands.basic.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
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
            e.printStackTrace();
            return;
        }

        try {
            Field profileField = EntityHuman.class.getDeclaredField("bH");
            profileField.setAccessible(true);
            profileField.set(entityPlayer, profile);
        } catch (Exception e) {
            e.printStackTrace();
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

        // Remove the player from the tab list
        PacketPlayOutPlayerInfo removePacket = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);

        // Add the player back to the tab list
        PacketPlayOutPlayerInfo addPacket = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);

        // Remove the player entity from everyone's world
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(entityPlayer.getId());

        // Re-add the player entity to everyone's world
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(entityPlayer);

        // Send all these packets to every online player
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player)) {
                CraftPlayer craftPlayer = (CraftPlayer) online;
                craftPlayer.getHandle().playerConnection.sendPacket(removePacket);
                craftPlayer.getHandle().playerConnection.sendPacket(addPacket);
                craftPlayer.getHandle().playerConnection.sendPacket(destroyPacket);
                craftPlayer.getHandle().playerConnection.sendPacket(spawnPacket);
            }
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
