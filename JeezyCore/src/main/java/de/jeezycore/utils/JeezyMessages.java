package de.jeezycore.utils;

import org.json.JSONObject;

public class JeezyMessages {

    public String[] loginMessage() {
        return new String[] {
                "\n",
                " §9§lMineral Network §7§l(§6§lSeason §f§l1§7§l)\n",
                "\n",
                " §9§l♦ §f§lWebsite: §9§lmineral.gg\n",
                "\n",
                " §9§l♦ §f§lStore: §9§lstore.mineral.gg\n",
                "\n",
                " §9§l♦ §f§lDiscord: §9§ldiscord.mineral.gg\n",
                "\n",
                " §9§l♦ §f§lTwitter: §9§ltwitter.com/MineralServer\n",
                "\n",
                "\n",
                " §9§lMineral §f§lPractice",
                "\n",
                " §71v1s, 2v2s, PvPBots, Duels, Parties, Events",
                "\n",
                " §9§l♦ §fTo play,§9§l right click with your sword",
                "\n",
                " §9§l♦ §fTo duel someone,§9§l /duel [player]",
                "\n",
                " §9§l♦ §fTo edit your kit,§9§l right click with your book",
                "\n",
                " §9§lThanks §f§lfor §9§ljoining §f§lour network§9§l! §9§l❤"
        };
    }

    public String bannedForeverMessage() {
        return "§7You are §4permanently §7banned from §9MineralPractice§7.\n\n"+
                "§7If you feel this ban has been unjustified, appeal on our §9discord §7at\n §9discord.mineral.gg§7.";
    }

    public String tabList(String value) {
        JSONObject jo = new JSONObject(
                "{\"title\":\"§7You're connected to §9§lmineral.gg\",\"subTitle\":\"§7Purchase ranks and more at: §9§lstore.mineral.gg\"}"
        );
       return jo.getString(value);
    }
}