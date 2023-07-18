package de.jeezycore.events;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.BanSQL;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.db.PlayersSQL;
import de.jeezycore.db.TabListSQL;
import de.jeezycore.utils.FakePlayerChecker;
import de.jeezycore.utils.NameTag;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class JoinEvent implements Listener {

    JeezySQL givePermsOnJoin = new JeezySQL();
    BanSQL check_if_banned = new BanSQL();
    NameTag nameTag = new NameTag();

    TabListSQL tabList = new TabListSQL();
    PlayersSQL playersSQL = new PlayersSQL();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (FakePlayerChecker.isFakePlayer(e.getPlayer()))
            return;

        tabList.setTabList1_8(e.getPlayer(), "§7You're connected to §9§lmineral.gg",
                "§7Purchase ranks and more at: §9§lstore.mineral.gg");
        check_if_banned.banData(e.getPlayer().getUniqueId());
        if (BanSQL.ban_forever) {
            e.getPlayer().kickPlayer("§7You are §4permanently §7banned from §9MineralPractice§7.\n\n" +
                    "§7If you feel this ban has been unjustified, appeal on our §9discord §7at\n §9discord.mineral.gg§7.");
            return;
        } else if (check_if_banned.ban_end != null) {
            check_if_banned.tempBanDurationCalculate(e.getPlayer());
            return;
        }

        e.getPlayer().sendMessage(new String[] {
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
        });

        e.getPlayer().sendMessage(new String[] {
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
        });
        playersSQL.firstJoined(e);
        playersSQL.checkIfUsernameChanged(e);
        givePermsOnJoin.rankMonthlyDurationCalculator(e.getPlayer());
        nameTag.giveTagOnJoin(e.getPlayer());

        try {
            givePermsOnJoin.getPlayerInformation(e.getPlayer());
            givePermsOnJoin.onJoinPerms(givePermsOnJoin.rankNameInformation, e.getPlayer().getUniqueId());

            MemorySection mc = (MemorySection) JeezyConfig.config_defaults.get("spawn-settings");
            boolean spawnOnSpownpointOnJoin = mc.getBoolean("spawn-at-spawnpoint-on-join");

            if (!spawnOnSpownpointOnJoin)
                return;
            List<Location> ls = (List<Location>) JeezyConfig.config_defaults.get("entry-spawn-point");
            World w = ls.get(0).getWorld();
            double x = ls.get(0).getBlockX();
            double y = ls.get(0).getBlockY();
            double z = ls.get(0).getBlockZ();
            float pitch = ls.get(0).getPitch();
            float yaw = ls.get(0).getYaw();

            e.getPlayer().teleport(new Location(w, x, y, z, yaw, pitch));

        } catch (Exception f) {
            f.printStackTrace();
        }

    }
}