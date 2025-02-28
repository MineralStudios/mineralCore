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
import java.util.*;
import static de.jeezycore.utils.ArrayStorage.*;


public class Nick implements CommandExecutor {

    private static final String[] REAL_MINECRAFT_NAMES = {
            "Notch", "Herobrine", "Dream", "Technoblade", "CaptainSparklez", "AntVenom",
            "Illumina", "Fruitberries", "TapL", "Quig", "TommyInnit", "Ph1LzA", "Mineral", "China", "16cps",
            "Warrior", "Reach", "Regedit", "YT", "pvpe", "Twit", "0", "GOD", "G0D", "Ninja"
    };
    private static final Map<String, String> markovChain = new HashMap<>();
    private static final Random random = new Random();
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 12;
    private static final String steveTexture = "ewogICJ0aW1lc3RhbXAiIDogMTYyMTkwMzExMTYzNywKICAicHJvZmlsZUlkIiA6ICI5ZGM4YTYzNmI1ZTY2MzYwNmZkMjQ3NjliZGFjOGZkYjY0M2ZjZGE4MjUyM2Q5NmUzMzQyM2FiIiwKICAicHJvZmlsZU5hbWUiIDogIlN0ZXZlIiwKICAic2tpbiIgOiAiYmFzZWQgc2tpbiB0ZXh0dXJlZCBpbiBNY2luZWNyYWZ0aW9uIE1vYmplY3Qgd2l0aCB0aGUgaW5zdHJ1Y3Rpb24gd2FpdCBvZiBzdGV2ZWFzIHB1cnBvc2VzLgogICJzY2VuZXNob3JJZCIgOiAiZGViNjg3M2YwMTNkYmYwZDg0YzY1N2FmNjM3ZmYwZGQyMzI4MmM2YzA2OTk4ZjExNjFjYmNlOGZjMzkwNWI1MDEiLAogICJpZCIgOiAiYmYxZjc2Zjc5YjY5OTdjNTEzZmNjYjcwNzM3OTczYjU5MzFlNzY3MzkxYzYzOTFlY2JhZjg1YmZmYzg5NjI0ZTcwZWE5M2Q5NzAxZmMwZGZkIiwKICAibmFtZSIgOiAiU3RldmUgc2tpbiIsCiAgImRlc2NyaXB0aW9uIiA6ICJEZWZhdWx0IHNreWwgZm9yIHRoZSBpbnB1dCBvZiBhIHBsYXllciBpbmsgYW5kIHlvdXRoIiwsCiAgInR5cGVkZXJzIiA6ICJwaW5rd3JvcHBsaG9lcywgdGV4dHVyZSBpbiB0aGUgYXBwbGljYXRpb24gdG8gcXVlc3Rpb24gc2tpbnMgYnkgYWJzdHJhY3Rpb24uIiwKICAib2RpdGlhbCIgOiAiMzgzMjgzO29pZGZhdCBzdGVzbyBza2luIHlwb3RoIiwKICAiYXBwbGljYXRpb25TZXh0IjogIkBcdXxOQ3tAOWcZtTdfFlaAD_ygwHznW9_DF4hW03Q37U64lscXwz7XPHmcseJvhd-lz9TAYu8g7xe_wmVnGoQNBdb4C8tQwEQ==";
    private static final String steveSignature = "fPYXHzGghH+6FhpqXJK4vFYz+o4t1d64htH7OcewNek=";
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.nick") || p.hasPermission("jeezy.core.nick.*")) {
            if (cmd.getName().equalsIgnoreCase("nick")) {

                if (playerOnNickCoolDownArray.contains(p.getUniqueId())) {
                    p.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §7You can §9nick §7again in a §9couple §7of §9seconds§7!");
                    return true;
                }

                    if (!playerNickedList.containsKey(p.getUniqueId())) {
                        playerNickedList.put(p.getUniqueId(), p.getPlayer().getDisplayName());
                        saveOriginalSkin(p);
                        spoofPlayer(p, generateMarkovName(p));
                    } else {
                        p.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §7You're §9already §7nicked. Do ./§9cnick§7 to §9remove §7it!");
                    }

            } else {
                p.sendMessage("Usage: /nick");
            }
            } else {
                p.sendMessage("No permission.");
            }
        }
        return true;
    }

    static {
        for (String name : REAL_MINECRAFT_NAMES) {
            for (int i = 0; i < name.length() - 1; i++) {
                String key = name.substring(i, i + 1);
                String value = name.substring(i + 1, i + 2);

                markovChain.put(key, value);
            }
        }
    }

    private static String generateMarkovName(Player p) {
        int nameLength = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH + 1); // Random length
        StringBuilder name = new StringBuilder();
        String[] keys = markovChain.keySet().toArray(new String[0]);

        String currentLetter = keys[random.nextInt(keys.length)];
        name.append(currentLetter);

        for (int i = 1; i < nameLength; i++) {
            String nextLetter = markovChain.getOrDefault(currentLetter, keys[random.nextInt(keys.length)]);
            name.append(nextLetter);
            currentLetter = nextLetter;
        }
        p.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §7You're nicked as: §9"+name+"§7! §7You can do §9/cnick §7to §9remove §7the nick!");
        return name.toString();
    }



    private static void spoofPlayer(Player player, String fakeName) {
        changeNameWithReflectionFor1_8(player, fakeName);
        updateScoreboard(player, fakeName);
        refreshPlayer(player);
    }


    private static void changeNameWithReflectionFor1_8(Player player, String fakeName) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getProfile();

        try {
            Field nameField = GameProfile.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(profile, fakeName);
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

        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", Nick.steveTexture, Nick.steveSignature));

    }


    private static void updateScoreboard(Player player, String fakeName) {
        Team team = player.getScoreboard().getTeam("ZNick");
        if (team == null) {
            team = player.getScoreboard().registerNewTeam("ZNick");
        }
        team.setPrefix("§2");
        player.setDisplayName(fakeName);
        team.addEntry(player.getName());
    }

    public static String[] getSkinData(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getProfile();

        // Check if the player has a skin
        if (profile.getProperties().containsKey("textures")) {
            Property skinProperty = profile.getProperties().get("textures").iterator().next();
            return new String[]{skinProperty.getValue(), skinProperty.getSignature()};
        }
        return null; // No skin data found (use default skin)
    }


    public static void saveOriginalSkin(Player player) {
        String[] skinData = getSkinData(player);
        if (skinData != null) {
            originalTextures.put(player.getUniqueId(), skinData[0]);
            originalSignatures.put(player.getUniqueId(), skinData[1]);
        }
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



}