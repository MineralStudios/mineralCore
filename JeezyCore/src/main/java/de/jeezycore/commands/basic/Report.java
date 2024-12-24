package de.jeezycore.commands.basic;

import com.google.common.base.Joiner;
import de.jeezycore.events.chat.StaffChat;
import de.jeezycore.utils.ArrayStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.*;

public class Report implements CommandExecutor {
    StaffChat staffChat = new StaffChat();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            List<String> ls = new ArrayList<String>(Arrays.asList(args));

            if (cmd.getName().equalsIgnoreCase("report") && args.length >= 2) {
                if (onCoolDown(p.getUniqueId())) {
                    p.sendMessage("§7§l[§4Report§7§l] §fYou need to §cwait §fbefore you can report a §9player §fagain!");
                    return true;
                }
                if (checkIfPlayerOnline(args[0]) == null) {
                        p.sendMessage("§7§l[§4Report§7§l] §fYou can't §creport §foffline players!");
                        return true;
                    } else if (((Player) sender).getDisplayName().equalsIgnoreCase(args[0])) {
                        p.sendMessage("§7§l[§4Report§7§l] §fYou can't §creport §fyourself!");
                        return true;
                    }

                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls).replace(args[0], "").replaceAll("\\s+", " ").trim();

                String report_msg = "§7§l[§4Report§7§l] (§9"+p.getDisplayName()+"§6§l -> §f§l"+args[0]+"§7§l) §c§lReason§f§l: §7"+input;
                staffChat.reportChat(p, report_msg);
                p.sendMessage("§7You §a§lsuccessfully §7reported §7§l"+args[0]+"§7.\n" +
                        "§7§l► §9Thanks §7for §9§lcaring §7about our §9§lcommunity§7. §7§l[§4§l❤§7§l]");
                ArrayStorage.reportCoolDownArray.add(p.getUniqueId());
                removePlayerFromCooldown(p.getUniqueId());
            } else {
                p.sendMessage("Usage: /report <player><message>");
            }
        }
        return true;
    }

    private boolean onCoolDown(UUID reporterUUID) {
        return ArrayStorage.reportCoolDownArray.contains(reporterUUID);
    }

    private Player checkIfPlayerOnline(String username) {
       return Bukkit.getPlayer(username);
    }

    private void removePlayerFromCooldown(UUID reporterUUID) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ArrayStorage.reportCoolDownArray.remove(reporterUUID);
                timer.cancel();
            }
        }, 180000);
    }
}