package de.jeezycore.events.inventories.rewards;

import de.jeezycore.db.MineralsSQL;
import de.jeezycore.db.RewardSQL;
import de.jeezycore.utils.ArrayStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static de.jeezycore.utils.ArrayStorage.*;


public class RewardsInventory implements Listener {

    RewardSQL rewardSQL = new RewardSQL();
    MineralsSQL mineralsSQL = new MineralsSQL();

    Inventory rewardsInventory;

    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getInventory().getTitle().contains("§9§lDaily Reward")) {
        e.setCancelled(true);
     }
    }

    public void pushItemsTags() {
        for (String tag : tagItems) {
            ItemStack tags = new ItemStack(Material.NAME_TAG, 1);
            ItemMeta test1Meta = tags.getItemMeta();
            test1Meta.setDisplayName(tag);
            tags.setItemMeta(test1Meta);

            rewardItems.add(tags);
        }
    }

    private void pushMinerals() {

        for (int i = 12; i < 50; i++) {
            int max = 30;
            int min = 2;
            int range = max - min + 1;
            int rand = (int)(Math.random() * range) + min;
            ItemStack minerals = new ItemStack(Material.INK_SACK, 1, (short) 12);
            ItemMeta mineralsMeta = minerals.getItemMeta();
            mineralsMeta.setDisplayName(""+i*rand);
            minerals.setItemMeta(mineralsMeta);

            rewardItems.add(minerals);
        }
    }

    public void setItems(Player p) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Collections.shuffle(rewardItems);
                rewardPerPLayerInventory.get(p).setItem(13, rewardItems.get(0));
                int counter = rewardPerPlayerTimer.get(p);
                int upCounter = counter + 1;
                rewardPerPlayerTimer.put(p, upCounter);
                if (rewardPerPlayerTimer.get(p) == 20) {
                  winGlass(p);
                  timer.cancel();
                    rewardPerPlayerTimer.remove(p);
                  try {
                      TimeUnit.SECONDS.sleep(3);
                  } catch (InterruptedException e) {
                      throw new RuntimeException(e);
                  }
                  p.getPlayer().closeInventory();
                  wonItemMessage(p);
                  rewardSQL.rewardClaimed(p, rewardPerPLayerInventory.get(p).getItem(13).getItemMeta().getDisplayName());
                    mineralsSQL.mineralsData();
                    addMineralsToDB(p);
              }
            }
        },0,500);
    }

    private void winGlass(Player p) {
        for (int i = 0; i < 27; i++) {
            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) ((int) 5));
            ItemMeta glassMeta = glass.getItemMeta();

            glassMeta.setDisplayName("§9§lSee you next time!");

            glass.setItemMeta(glassMeta);

            if (i == 13) {
                continue;
            }

            rewardPerPLayerInventory.get(p).setItem(i, glass);
        }
    }

    private void addMineralsToDB(Player p) {
        String minerals = rewardPerPLayerInventory.get(p).getItem(13).getType().name();

        if (minerals.equalsIgnoreCase("INK_SACK")) {

            mineralsStorage.put(p.getPlayer().getUniqueId().toString(), rewardPerPLayerInventory.get(p).getItem(13).getItemMeta().getDisplayName());

            mineralsSQL.updateMineralsData();
            ArrayStorage.mineralsStorage.clear();
            System.out.println(minerals);
        }
    }


    private void wonItemMessage(Player p) {
        String wonItem = rewardPerPLayerInventory.get(p).getItem(13).getItemMeta().getDisplayName();
        p.sendRawMessage("§7You §a§lsuccessfully §7won §9§l"+wonItem+"§7.");
    }


    public void rewards_menu(Player p) {
        rewardsInventory = Bukkit.createInventory(null, 27, "§9§lDaily Reward");
        rewardPerPlayerTimer.put(p, 0);
        rewardPerPLayerInventory.put(p, rewardsInventory);
        pushMinerals();
        pushItemsTags();
        for (int i = 0; i < 27; i++) {
            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) ((int) 11));
            ItemMeta glassMeta = glass.getItemMeta();

            glassMeta.setDisplayName("§9§lGood Luck!");

            glass.setItemMeta(glassMeta);

            rewardPerPLayerInventory.get(p).setItem(i, glass);
        }
        setItems(p);
        p.openInventory(rewardPerPLayerInventory.get(p));
    }
}