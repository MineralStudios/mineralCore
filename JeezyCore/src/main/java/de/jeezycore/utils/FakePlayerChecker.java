package de.jeezycore.utils;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import gg.mineral.server.fakeplayer.FakePlayer;

public class FakePlayerChecker {

    public static boolean isFakePlayer(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        return FakePlayer.isFakePlayer(craftPlayer.getHandle());
    }

}
