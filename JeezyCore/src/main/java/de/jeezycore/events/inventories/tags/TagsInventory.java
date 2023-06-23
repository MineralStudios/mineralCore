package de.jeezycore.events.inventories.tags;

import de.jeezycore.db.RewardSQL;
import de.jeezycore.db.TagsSQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.jeezycore.utils.ArrayStorage.tags_in_ownership_array;
import static de.jeezycore.utils.ArrayStorage.tags_inv_array;

public class TagsInventory {
    Inventory tag_inv;
    RewardSQL rewardSQL = new RewardSQL();
    private int addUp = 10;

    private final TagsSQL display = new TagsSQL();

    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getInventory().getTitle().contains("§8§lTags")) {
            if (e.getCurrentItem().getData().toString().equalsIgnoreCase("NAME_TAG(0)") && e.getCurrentItem().getItemMeta().getLore().get(5).equalsIgnoreCase("§a§lYou own this tag§7§l.")) {
                e.getWhoClicked().sendMessage("§7You §2§lsuccessfully §7gave yourself the §9§l"+e.getCurrentItem().getItemMeta().getDisplayName()+ " §7tag§7.");
                e.getWhoClicked().closeInventory();
                executeMYSQL(e.getCurrentItem().getItemMeta().getDisplayName().substring(4), e.getWhoClicked().getUniqueId());
            } else if (e.getCurrentItem().getData().toString().equalsIgnoreCase("NAME_TAG(0)") && e.getCurrentItem().getItemMeta().getLore().get(5).equalsIgnoreCase("§4§lYou don't own this tag yet§7§l.")) {
                e.getWhoClicked().sendMessage("§4§lYou don't own this tag.");
            }

            if (e.getCurrentItem().getData().toString().equalsIgnoreCase("DIAMOND(0)")) {
                int add = tags_inv_array.get(e.getWhoClicked().getUniqueId());
                tags_inv_array.put(e.getWhoClicked().getUniqueId(), add + 1);

                tags_menu(Bukkit.getPlayer(UUID.fromString(String.valueOf(e.getWhoClicked().getUniqueId()))));
            } else if (e.getCurrentItem().getData().toString().equalsIgnoreCase("EMERALD(0)")) {
                int add = tags_inv_array.get(e.getWhoClicked().getUniqueId());
                tags_inv_array.put(e.getWhoClicked().getUniqueId(), add - 1);

                tags_menu(Bukkit.getPlayer(UUID.fromString(String.valueOf(e.getWhoClicked().getUniqueId()))));
            }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cReset tag")) {
                display.resetTag(e.getCurrentItem().getItemMeta().getLore().get(1).replace("§9§l", ""), e.getWhoClicked().getName(), (Player) e.getWhoClicked());
                e.getWhoClicked().sendMessage("§7You §2successfully §creset §7your tag.");
                e.getWhoClicked().closeInventory();
            }

            e.setCancelled(true);
        }
        }
    public void tags_menu(Player p) {
        if (tags_inv_array.get(p.getPlayer().getUniqueId()) == 1) {
            display.getData(0);
        } else {
            display.getData(tags_inv_array.get(p.getPlayer().getUniqueId()) * 21 - 21);
        }
        tags_in_ownership_array.clear();
        display.check(p);
        display.getFullDataSize();
        display.getOwnershipData(p);
        rewardSQL.checkIfClaimed(p);
        int pageEnd = (int) Math.ceil((double)display.tagDataFullSize.size() / 21);

        tag_inv = Bukkit.createInventory(null, 45,"§8§lTags "+"§7(§f§l"+tags_inv_array.get(p.getPlayer().getUniqueId())+" §7§l/§9§l "+pageEnd+"§7)");
        for (int b = 0; b < tag_inv.getSize(); b++) {
            ItemStack placeholder = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) ((int) 11));
            ItemMeta placeholderMeta = placeholder.getItemMeta();
            placeholderMeta.setDisplayName("§9§lChoose your tag!");
            placeholder.setItemMeta(placeholderMeta);
            tag_inv.setItem(b, placeholder);
        }

        for (int pa = 0; pa < tag_inv.getSize(); pa++) {

            if (tags_inv_array.get(p.getPlayer().getUniqueId()) >= 2) {
                ItemStack pagesBack = new ItemStack(Material.EMERALD, 1);
                ItemMeta pagesBackMeta = pagesBack.getItemMeta();
                pagesBackMeta.setDisplayName("§c§lPrevious Site §7§l(§f§l"+tags_inv_array.get(p.getPlayer().getUniqueId())+" §7§l/§9§l "+pageEnd+"§7§l)");
                pagesBack.setItemMeta(pagesBackMeta);
                tag_inv.setItem(0, pagesBack);
            }
            if (tags_inv_array.get(p.getPlayer().getUniqueId()) != pageEnd) {
                ItemStack pages = new ItemStack(Material.DIAMOND, 1);
                ItemMeta pagesMeta = pages.getItemMeta();
                pagesMeta.setDisplayName("§9§lNext Site §7§l(§f§l"+tags_inv_array.get(p.getPlayer().getUniqueId())+" §7§l/§9§l "+pageEnd+"§7§l)");
                pages.setItemMeta(pagesMeta);
                tag_inv.setItem(8, pages);
            }

            if (TagsSQL.tag_exist_name != null) {
                ItemStack removeTagViaGui = new ItemStack(Material.TORCH, 1);
                ItemMeta removeTagViaGuiMeta = removeTagViaGui.getItemMeta();
                ArrayList<String> reset_tag_desc = new ArrayList<>();
                reset_tag_desc.add(0, "§8§m-----------------------------------");
                reset_tag_desc.add(1, "§9§l"+TagsSQL.tag_exist_name);
                reset_tag_desc.add(2, "§7Current tag display: §2"+p.getDisplayName()+ " §9§l" + TagsSQL.tag_exist_format.replace("&", "§")+ "§7§l.");
                reset_tag_desc.add(3, "§8§m-----------------------------------");
                reset_tag_desc.add(4, "§eClick to reset your tag§7.");
                removeTagViaGuiMeta.setDisplayName("§cReset tag");
                removeTagViaGuiMeta.setLore(reset_tag_desc);
                removeTagViaGui.setItemMeta(removeTagViaGuiMeta);
                tag_inv.setItem(4, removeTagViaGui);
                TagsSQL.tag_exist_name = null;
            }

        }



            for (int i = 0; i < display.arrayList.size(); i++) {

                String test = display.arrayList.get(i).replace("[", "").replace(",", "").replace("]", "");

                ItemStack tag = new ItemStack(Material.NAME_TAG, 1);
                String tagName = test.split(" ")[0];
                String tagCategory = test.split(" ")[1];
                String tagFormat = test.split(" ")[2];
                ItemMeta tagMeta = tag.getItemMeta();
                List<String> desc = new ArrayList<String>();
                desc.add(0, "§8§m-----------------------------------");
                desc.add(1, "§7§lCategory: §f§l" + tagCategory);
                desc.add(2, "");
                desc.add(3, "§7§lDisplay: §2" + p.getDisplayName() + " " + tagFormat.replaceAll("&", "§"));
                desc.add(4, "§8§m-----------------------------------");
                desc.add(5, "§4§lYou don't own this tag yet§7§l.");
                if (i == 7 || i == 14) {
                    addUp += 2;
                } else if (i >= 21) {
                    break;
                }


                if (display.arrayList.get(i).equalsIgnoreCase(RewardSQL.rewardPrice)) {
                    desc.remove(3);
                    desc.add(3, "§a§lYou own this tag§7§l.");
                }

                if (tags_in_ownership_array.size() != 0) {
                    for (int x = 0; x < tags_in_ownership_array.size(); x++) {
                        if (display.arrayList.get(i).contains(tags_in_ownership_array.get(x))) {
                            desc.remove(5);
                            desc.add(5, "§a§lYou own this tag§7§l.");
                            break;
                        }
                    }
                }
                tagMeta.setDisplayName("§9§l"+tagName);
                tagMeta.setLore(desc);
                tag.setItemMeta(tagMeta);
                tag_inv.setItem(addUp + i, tag);


                p.openInventory(tag_inv);
                RewardSQL.rewardPrice = null;
            }
    }

    public void executeMYSQL(String tagName, UUID p) {
        display.setCurrentTag(tagName, p);
    }
}