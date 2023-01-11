package de.jeezycore.commands.disguise;

import de.jeezycore.disguise.manger.DisguiseManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class DisguiseCommand implements CommandExecutor {
  private final DisguiseManager disguiseManager;

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
    if (!(commandSender instanceof Player)) return true;
    if (command.getName().equalsIgnoreCase("disguise")) {
      Player player = (Player) commandSender;
      if (args.length == 0) {
        player.sendMessage("/disguise <user> / <clear>");
        return true;
      }

      String playerName = args[0];
      if (playerName.equalsIgnoreCase("clear")) {
        disguiseManager.deleteDisguise(player);
        player.sendMessage(ChatColor.BLUE + "Undisguised"+ChatColor.GRAY+".");
        return true;
      }

      player.sendMessage(ChatColor.BLUE + "Disguising...");
      disguiseManager.loadDisguiseInfo(playerName, ((texture, signature) -> {
        if (texture == null || signature == null) {
          player.sendMessage(ChatColor.RED + "Failed to find "+ ChatColor.WHITE + playerName+"'s skin"+ChatColor.GRAY+".");
          return;
        }

        disguiseManager.applyDisguise(player, playerName, texture, signature);
        player.sendMessage(ChatColor.BLUE + "Disguised as "+ ChatColor.WHITE + playerName + ChatColor.GRAY +".");
      }));
    }
    return true;
  }
}
