package de.jeezycore.events.inventories.tags;

import de.jeezycore.db.RewardSQL;
import de.jeezycore.db.TagsSQL;
import de.jeezycore.db.cache.TagsCache;
import de.jeezycore.db.services.TagsService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static de.jeezycore.utils.ArrayStorage.*;

public class TagsInventory {
    Inventory tag_inv;
    //RewardSQL rewardSQL = new RewardSQL();

    private final TagsSQL display = new TagsSQL();

    private final TagsService tagsService = new TagsService();


    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getInventory().getTitle().contains("§8§lTags")) {
            if (e.getCurrentItem().getData().toString().equalsIgnoreCase("NAME_TAG(0)") && e.getCurrentItem().getItemMeta().getLore().get(5).equalsIgnoreCase("§a§lYou own this tag§7§l.")) {
                JSONObject tagData = tagsCheckStatus.get(e.getWhoClicked().getUniqueId());
                if (tagData != null) {
                    String clickedName = e.getCurrentItem().getItemMeta().getDisplayName().substring(4);
                    String tagName = (String) tagData.get("tagName");
                    if (clickedName.equalsIgnoreCase(tagName)) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().substring(4).equalsIgnoreCase((String) tagsCheckStatus.get(e.getWhoClicked().getUniqueId()).get("tagName"))) {
                            e.getWhoClicked().sendMessage("§7You have §c§lalready §7selected that tag.");
                            e.getWhoClicked().closeInventory();
                            return;
                        }
                    }
                }
                CompletableFuture.runAsync(() -> {
                    e.getWhoClicked().sendMessage("§7You §2§lsuccessfully §7gave yourself the §9§l"+e.getCurrentItem().getItemMeta().getDisplayName()+ " §7tag§7.");
                    e.getWhoClicked().closeInventory();
                    executeMYSQL(e.getCurrentItem().getItemMeta().getDisplayName().substring(4), (Player) e.getWhoClicked());
                    TagsCache.getInstance().reloadPlayerTagsNow();
                });
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
                CompletableFuture.runAsync(() -> {
                    e.getWhoClicked().sendMessage("§7You §2successfully §creset §7your tag.");
                    e.getWhoClicked().closeInventory();
                    display.resetTag(e.getCurrentItem().getItemMeta().getLore().get(1).replace("§9§l", ""), e.getWhoClicked().getName(), (Player) e.getWhoClicked());
                    TagsCache.getInstance().reloadPlayerTagsNow();
                });

            }

            e.setCancelled(true);
        }
        }

    public void getPlayerTag(Player p) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(tagsService.getAllPlayerTags().toString());

            for (Object item : jsonArray) {
                JSONObject entry = (JSONObject) item;

                for (Object keyObj : entry.keySet()) {
                    String key = (String) keyObj;

                    if (key.equalsIgnoreCase(p.getUniqueId().toString())) {
                        JSONObject value = (JSONObject) entry.get(key);
                        tagsCheckStatus.put(p.getPlayer().getUniqueId(), value);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void tags_menu(Player p) {
        tags_in_ownership_array.clear();
        getPlayerTag(p);
        // display.check(p);
        //display.getOwnershipData(p);
        //rewardSQL.checkIfClaimed(p);

        int pageEnd = (int) Math.ceil((double)tagsService.getAllTags().length() / 21);

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

            if (tagsCheckStatus.containsKey(p.getUniqueId())) {
                JSONObject storedValue = tagsCheckStatus.get(p.getPlayer().getUniqueId());
                String tagName = (String) storedValue.get("tagName");
                String tagDesign = (String) storedValue.get("tagDesign");

                ItemStack removeTagViaGui = new ItemStack(Material.TORCH, 1);
                ItemMeta removeTagViaGuiMeta = removeTagViaGui.getItemMeta();
                ArrayList<String> reset_tag_desc = new ArrayList<>();
                reset_tag_desc.add(0, "§8§m-----------------------------------");
                reset_tag_desc.add(1, "§9§l"+tagName);
                reset_tag_desc.add(2, "§7Current tag display: §2"+p.getDisplayName()+ " §9§l"+tagDesign+"§7§l.");
                reset_tag_desc.add(3, "§8§m-----------------------------------");
                reset_tag_desc.add(4, "§eClick to reset your tag§7.");
                removeTagViaGuiMeta.setDisplayName("§cReset tag");
                removeTagViaGuiMeta.setLore(reset_tag_desc);
                removeTagViaGui.setItemMeta(removeTagViaGuiMeta);
                tag_inv.setItem(4, removeTagViaGui);
            }
        }


        try {
            JSONParser jsParser = new JSONParser();
            JSONArray jsonA = (JSONArray) jsParser.parse(tagsService.getAllTags().toString());
            UUID playerId = p.getPlayer().getUniqueId();
            int currentPage = tags_inv_array.get(playerId);
            int itemsPerPage = 21;
            int startIndex = (currentPage - 1) * itemsPerPage;
            int endIndex = Math.min(startIndex + itemsPerPage, jsonA.size());
            int slot = 10;

            for (int i = startIndex; i < endIndex; i++) {
                JSONObject jsonOB = (JSONObject) jsonA.get(i);

            ItemStack tag = new ItemStack(Material.NAME_TAG, 1);

                String tagName = (String) jsonOB.get("tagName");
                String tagCategory = (String) jsonOB.get("tagCategories");
                String tagDesign = (String) jsonOB.get("tagDesign");
                ItemMeta tagMeta = tag.getItemMeta();
                List<String> desc = new ArrayList<String>();
                desc.add(0, "§8§m-----------------------------------");
                desc.add(1, "§7§lCategory: §f§l" + tagCategory);
                desc.add(2, "");
                desc.add(3, "§7§lDisplay: §2" + p.getDisplayName() + " " + tagDesign);
                desc.add(4, "§8§m-----------------------------------");
                if (p.hasPermission("jeezy.core.tags.all")) {
                    desc.add(5, "§a§lYou own this tag§7§l.");
                } else {
                    desc.add(5, "§4§lYou don't own this tag yet§7§l.");
                }
                if (i == 7 || i == 14) {
                    slot += 2;
                }


                /*
                if (display.arrayList.get(i).equalsIgnoreCase(RewardSQL.rewardPrice)) {
                    desc.remove(3);
                    desc.add(3, "§a§lYou own this tag§7§l.");
                }

                 */

            /*
                if (tags_in_ownership_array.size() != 0) {
                    for (int x = 0; x < tags_in_ownership_array.size(); x++) {
                        if (display.tagNameList.get(i).equalsIgnoreCase(tags_in_ownership_array.get(x))) {
                            desc.remove(5);
                            desc.add(5, "§a§lYou own this tag§7§l.");
                            break;
                        }
                    }
                }

             */
                tagMeta.setDisplayName("§9§l"+tagName);
                tagMeta.setLore(desc);
                tag.setItemMeta(tagMeta);
                tag_inv.setItem(slot, tag);
                slot++;


            }
        p.openInventory(tag_inv);
        RewardSQL.rewardPrice = null;
        } catch (Exception e) {

        }
    }

    public void executeMYSQL(String tagName, Player player) {
        display.setCurrentTag(tagName, player);
    }
}