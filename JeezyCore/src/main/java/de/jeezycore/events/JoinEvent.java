package de.jeezycore.events;



import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.BanSQL;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.db.StatusSQL;
import de.jeezycore.utils.FakePlayerChecker;
import de.jeezycore.utils.NameTag;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


import java.io.File;
import java.util.*;


public class JoinEvent implements Listener {

    FakePlayerChecker fakePlayerChecker = new FakePlayerChecker();

    NameTag nameTag = new NameTag();
    StatusSQL statusSQL = new StatusSQL();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (fakePlayerChecker.isFakePlayer(e.getPlayer())) {
            return;
        }
        statusSQL.firstJoined(e);
        nameTag.giveTagOnJoin(e.getPlayer());
        e.setJoinMessage("");
        BanSQL check_if_banned = new BanSQL();
        check_if_banned.banData(e.getPlayer().getUniqueId());
        BanSQL.punishment_UUID = null;
        if (BanSQL.ban_forever) {
            e.getPlayer().kickPlayer("§4You are permanently banned from §bJeezyDevelopment.\n" +
                    "§7If you feel this ban is unjustified, appeal on our discord at\n §bjeezydevelopment.com§7.");
            BanSQL.ban_forever = false;
        } else if (check_if_banned.ban_end != null) {
            check_if_banned.tempBanDurationCalculate(e.getPlayer());
        }

    try {
        JeezySQL givePermsOnJoin = new JeezySQL();
        givePermsOnJoin.onJoinPerms(e.getPlayer().getUniqueId());


        MemorySection mc = (MemorySection) JeezyConfig.config_defaults.get("spawn-settings");
        boolean spawnOnSpownpointOnJoin = mc.getBoolean("spawn-at-spawnpoint-on-join");

        if (!spawnOnSpownpointOnJoin) return;
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
