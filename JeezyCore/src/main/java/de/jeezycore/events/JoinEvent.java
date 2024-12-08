package de.jeezycore.events;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.*;
import de.jeezycore.tablist.VuzleTAB;
import de.jeezycore.utils.JeezyMessages;
import de.jeezycore.utils.NameMC;
import de.jeezycore.utils.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.concurrent.CompletableFuture;

public class JoinEvent implements Listener {

    RanksSQL givePermsOnJoin = new RanksSQL();
    BanSQL check_if_banned = new BanSQL();
    NameTag nameTag = new NameTag();
    VuzleTAB vuzleTAB = new VuzleTAB();
    PlayersSQL playersSQL = new PlayersSQL();
    PlayTimeSQL playTimeSQL = new PlayTimeSQL();
    NameMC nameMC = new NameMC();
    JeezyMessages jeezyMessages = new JeezyMessages();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        CompletableFuture.runAsync(() -> {
            e.getPlayer().sendMessage(jeezyMessages.loginMessage());
            vuzleTAB.setTabList1_8(e.getPlayer(), jeezyMessages.tabList("title"), jeezyMessages.tabList("subTitle"));
            check_if_banned.banData(e.getPlayer().getUniqueId());
            if (BanSQL.ban_forever) {
                e.getPlayer().kickPlayer(jeezyMessages.bannedForeverMessage());
                return;
            } else if (check_if_banned.ban_end != null) {
                check_if_banned.tempBanDurationCalculate(e.getPlayer());
                return;
            }
            playersSQL.firstJoined(e);
            playTimeSQL.playTimeJoin(e);
            playersSQL.checkIfUsernameChanged(e);
            givePermsOnJoin.rankMonthlyDurationCalculator(e.getPlayer());
            nameTag.giveTagOnJoin(e.getPlayer());
            nameMC.checkNameMc(e.getPlayer());
            givePermsOnJoin.getPlayerInformation(e.getPlayer().getUniqueId());
            givePermsOnJoin.onJoinPerms(RanksSQL.rankNameInformation, e.getPlayer().getUniqueId());
        });

        try {
            MemorySection spawnPoint = (MemorySection) JeezyConfig.config_defaults.get("entry-spawn-point");
            MemorySection mc = (MemorySection) JeezyConfig.config_defaults.get("spawn-settings");
            boolean spawnOnSpawnPointOnJoin = mc.getBoolean("spawn-at-spawnpoint-on-join");

            if (!spawnOnSpawnPointOnJoin)
                return;

            World w = Bukkit.getServer().getWorld(spawnPoint.getString("world"));
            double x = Double.parseDouble(spawnPoint.getString("x"));
            double y = Double.parseDouble(spawnPoint.getString("y"));
            double z = Double.parseDouble(spawnPoint.getString("z"));
            float yaw = Float.parseFloat(spawnPoint.getString("yaw"));
            float pitch = Float.parseFloat(spawnPoint.getString("pitch"));

            e.getPlayer().teleport(new Location(w, x, y, z, yaw, pitch));

        } catch (Exception f) {
            f.printStackTrace();
        }
    }
}