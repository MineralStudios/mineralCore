package de.jeezycore.commands.basic.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.jeezycore.main.Main;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.nick") || p.hasPermission("jeezy.core.nick.*")) {
                if (cmd.getName().equalsIgnoreCase("nick") && args.length == 1) {

                    if (playerOnNickCoolDownArray.contains(p.getUniqueId()) || playerOnFetchSkinCoolDownArray.contains(p.getUniqueId())) {
                        p.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §7You can §9nick §7again in a §9couple §7of §9seconds§7!");
                        return true;
                    }


                    if (!playerNickedList.containsKey(p.getUniqueId())) {
                        spoofPlayer(args[0], generateMarkovName(p), p);
                    } else {
                        p.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §7You're §9already §7nicked. Do ./§9cnick§7 to §9remove §7it!");
                    }

                } else {
                    p.sendMessage("Usage: /nick <playernameskin>");
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
        return name.toString();
    }


    private static void spoofPlayer(String playerSkin, String fakeName, Player player) {
        playerNickedList.put(player.getUniqueId(), player.getPlayer().getDisplayName());
        saveOriginalSkin(player);
        fetchSkinData(playerSkin, fakeName, player);
    }


    private static void changeNameWithReflectionFor1_8(Player player, String fakeName, String texture, String signature) {
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
        profile.getProperties().put("textures", new Property("textures", texture, signature));

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


    // Method to fetch the skin texture and signature from Mojang's API
    public static void fetchSkinData(String skinPlayer, String fakeName, Player player) {
        try {
            // Step 1: Get the UUID of the player by querying Mojang's API
            String uuid = getUUID(skinPlayer);
            if (uuid == null) {
                player.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §9Player §7not found!");
                nickFetchAgainTimer(player);
            }
                // Step 2: Fetch the player's profile data using their UUID
                String urlString = "https://sessionserver.mojang.com/session/minecraft/profile/"+uuid+"?unsigned=false";
                URL url = new URL(urlString);
                InputStream inputStream = url.openStream();
                String jsonResponse = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

                // Step 3: Parse the JSON response to extract the skin texture and signature
                JSONObject jsonObject = new JSONObject(jsonResponse);
                String texture = jsonObject.getJSONArray("properties")
                        .getJSONObject(0)
                        .getString("value"); // The texture value
                String signature = jsonObject.getJSONArray("properties")
                        .getJSONObject(0)
                        .getString("signature"); // The signature value



                changeNameWithReflectionFor1_8(player, fakeName, texture, signature);
                updateScoreboard(player, fakeName);
                refreshPlayer(player);
                player.sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §7You're nicked as: §9" + fakeName + "§7! §7You can do §9/cnick §7to §9remove §7the nick!");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to fetch UUID of the player using Mojang's API
    public static String getUUID(String playerName) {
        try {
            // Request the UUID for the given playerName
            String urlString = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
            URL url = new URL(urlString);
            InputStream inputStream = url.openStream();
            String jsonResponse = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            // Parse the UUID from the response
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void nickFetchAgainTimer(Player p) {
        playerOnFetchSkinCoolDownArray.add(p.getUniqueId());
        Timer time = new Timer();
        TimerTask nickAgainTask = new TimerTask() {
            @Override
            public void run() {
                playerOnFetchSkinCoolDownArray.remove(p.getPlayer().getUniqueId());
                time.purge();
                time.cancel();
            }
        };
        time.schedule(nickAgainTask, 10000);
    }

}