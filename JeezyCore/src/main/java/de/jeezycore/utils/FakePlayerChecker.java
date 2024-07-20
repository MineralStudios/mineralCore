package de.jeezycore.utils;

import gg.mineral.botapi.manager.FakePlayerManager;
import org.bukkit.entity.Player;


public class FakePlayerChecker {

    public static boolean isFakePlayer(Player player) {
        return FakePlayerManager.getFakePlayer(player.getUniqueId()) != null;
    }
}