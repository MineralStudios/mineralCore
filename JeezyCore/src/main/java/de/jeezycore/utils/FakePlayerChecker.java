package de.jeezycore.utils;

import gg.mineral.botapi.manager.FakePlayerManager;
import org.bukkit.entity.Player;


public class FakePlayerChecker {

   private static final NameTag nameTag = new NameTag();

    public static boolean isFakePlayer(Player player) {

        nameTag.specialBotTag(player);

        return FakePlayerManager.getFakePlayer(player.getUniqueId()) != null;
    }
}