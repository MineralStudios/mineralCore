package de.jeezycore.commands.rewards;

import de.jeezycore.db.RewardSQL;
import de.jeezycore.events.inventories.rewards.RewardsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DailyReward implements CommandExecutor {
    RewardSQL rewardSQL = new RewardSQL();
    RewardsInventory rewardsInventory = new RewardsInventory();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("daily-reward")) {
                rewardSQL.checkIfClaimed(p);

               if (RewardSQL.alreadyClaimedReward) {
                   rewardSQL.nextRewardPeriod(p);
                   RewardSQL.alreadyClaimedReward = false;
               } else {
                   rewardsInventory.rewards_menu(p);
               }
            }
        }
        return true;
    }
}