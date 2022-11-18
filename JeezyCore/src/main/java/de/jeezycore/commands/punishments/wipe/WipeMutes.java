package de.jeezycore.commands.punishments.wipe;

import de.jeezycore.db.WipeSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WipeMutes implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("wipemutes") && args.length == 1) {

                WipeSQL execute = new WipeSQL();
                execute.wipeMutes(args[0], p);


            } else {
                p.sendMessage("Usage: /wipemutes <player>");
            }

        }

        return false;
    }
}
