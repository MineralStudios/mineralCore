package de.jeezycore.utils;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class FakePlayerChecker {

    public boolean isFakePlayer(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        boolean entityPlayer = craftPlayer.getHandle().getClass().getSimpleName().equalsIgnoreCase("FakePlayer");
        System.out.println(entityPlayer);
        return entityPlayer;
    }



}
