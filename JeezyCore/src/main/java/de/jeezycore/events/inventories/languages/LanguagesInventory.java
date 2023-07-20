package de.jeezycore.events.inventories.languages;

import de.jeezycore.db.redis.LanguagesRedis;
import de.jeezycore.utils.ArrayStorage;
import net.suuft.libretranslate.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Map;

public class LanguagesInventory {
    Inventory lang;

    LanguagesRedis languagesRedis = new LanguagesRedis();


    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getInventory().getTitle().contains("§f§lLang§9§luages")) {
            if (e.getCurrentItem().getData().toString().equalsIgnoreCase("PAINTING(0)")) {
                    e.getWhoClicked().sendMessage("§7You §2successfully §7choose "+e.getCurrentItem().getItemMeta().getDisplayName()+" §7as your language.");
                    executeRedis(e, e.getCurrentItem().getItemMeta().getDisplayName());
                    e.getWhoClicked().closeInventory();
                    return;
            } else if (e.getCurrentItem().getData().toString().equalsIgnoreCase("LEATHER_CHESTPLATE(0)") && e.getCurrentItem().getItemMeta().getLore().get(5).equalsIgnoreCase("§4§lYou don't own this chat color yet§7§l.")) {
                e.getWhoClicked().sendMessage("§4§lYou don't own this chat color yet§7§l.");
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cReset chat color")) {
                //uc.check(e.getWhoClicked().getName());
                //chatColorSQL.resetChatColor(UUID.fromString(UUIDChecker.uuid));
                e.getWhoClicked().sendMessage("§7You §2successfully §creset §7your chatcolor.");
                e.getWhoClicked().closeInventory();
            }
            e.setCancelled(true);
        }
    }


    public void languagesMenu(Player p) {
        lang = Bukkit.createInventory(null, 54,"§f§lLang§9§luages");
        for (int i = 0; i < lang.getSize(); i++) {
            ItemStack placeholder = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) ((int) 11));
            ItemMeta placeholderMeta = placeholder.getItemMeta();
            placeholderMeta.setDisplayName("§9§lChoose your language!");
            placeholder.setItemMeta(placeholderMeta);
            lang.setItem(i, placeholder);
        }
        int i = 0;
        int addUp = 10;
        for (Map.Entry<String, Language> set : ArrayStorage.languageMap.entrySet()) {
                ItemStack languages = new ItemStack(Material.PAINTING, 1);
                ItemMeta languagesMeta = languages.getItemMeta();
                String str = set.getValue().name().toLowerCase();
                String finalStr = str.substring(0, 1).toUpperCase() + str.substring(1);
                languagesMeta.setDisplayName("§9§l"+finalStr);
                languages.setItemMeta(languagesMeta);

            if (i == 7 || i == 14 || i == 21) {
                addUp += 2;
            } else if (i == 28) {
                addUp +=4;
            }
            if (set.getValue().name().equalsIgnoreCase("NONE")) {
                continue;
            }
                lang.setItem(addUp + i, languages);
            i++;
        }
        p.openInventory(lang);
    }

    private void executeRedis(InventoryClickEvent e, String language) {
        languagesRedis.setLanguage(e, language);
    }

}