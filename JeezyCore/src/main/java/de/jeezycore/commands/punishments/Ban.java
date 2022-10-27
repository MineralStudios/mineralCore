package de.jeezycore.commands.punishments;

import com.google.common.base.Joiner;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.*;

public class Ban implements CommandExecutor {
    int s = 1;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ban") && args.length > 1) {
                List<String> ls = new ArrayList<String>(Arrays.asList(args));
                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls).replace(args[0], "");

                String sql = "INSERT INTO punishments " +
                        "(UUID, banned_forever, ban_time, mute_time, punishment_log) " +
                        "VALUES " +
                        "(?, true, NULL, NULL, ?)";

                JeezySQL banSQL = new JeezySQL();
                UUIDChecker uc = new UUIDChecker();
                uc.check(args[0]);

                JSONObject json_o = new JSONObject();

                json_o.put("banned by", UUIDChecker.uuid);
                json_o.put("time", "forever");
                json_o.put("reason", input);
                ArrayStorage.punishment_log.add(json_o);

                banSQL.ban(sql, UUID.fromString(UUIDChecker.uuid), ArrayStorage.punishment_log.toString());

                System.out.println(ArrayStorage.punishment_log);

                p.sendMessage("§7You §asuccessfully §7banned §4"+ args[0]+" §7from the server.");

                try {
                    System.out.println(UUIDChecker.uuid);
                    Bukkit.getServer().getPlayer(UUID.fromString(UUIDChecker.uuid)).kickPlayer("§4You are permanently banned from JeezyDevelopment.\n\n" +
                            "§6Reason: §c" +input+"§7.\n\n"+
                            "§7If you feel this ban has been unjustified, appeal on our discord at\n §bjeezydevelopment.com§7.");
                } catch (Exception e) {
                    return true;
                }

            } else {
                p.sendMessage("Usage: /ban <player><reason>.");
            }

        }


        return false;
    }
}
