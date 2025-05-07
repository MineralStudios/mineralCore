package de.jeezycore.events.inventories.chatColors;

import de.jeezycore.db.ChatColorSQL;
import de.jeezycore.db.cache.ChatColorsCache;
import de.jeezycore.db.services.ChatColorsService;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChatColorsInventory {

    public Inventory chatColorsInv;
    private int addUp = 10;
    private final ChatColorSQL chatColorSQL = new ChatColorSQL();
    private final ChatColorsService chatColorsService = new ChatColorsService();


    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getInventory().getTitle().contains("§9§lChat§f§lColors")) {
            if (e.getCurrentItem().getData().toString().equalsIgnoreCase("LEATHER_CHESTPLATE(0)") && e.getCurrentItem().getItemMeta().getLore().get(5).equalsIgnoreCase("§a§lYou own this chat color§7§l.")) {
                for (int i = 0; i < chatColorsService.getAllChatColorsPlayers().length(); i++) {
                    try {
                        JSONParser jsParser = new JSONParser();
                        JSONArray jsonA = (JSONArray) jsParser.parse(chatColorsService.getAllChatColorsPlayers().toString());
                        JSONObject jsonOB = (JSONObject) jsonA.get(i);
                        if (jsonOB.get("playerUUID").toString().equalsIgnoreCase(e.getWhoClicked().getUniqueId().toString())) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equalsIgnoreCase(jsonOB.get("currentChatColorName").toString().substring(2))) {
                                e.getWhoClicked().sendMessage("§7You have §c§lalready §7selected that chat color.");
                                e.getWhoClicked().closeInventory();
                                return;
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
                CompletableFuture.runAsync(() -> {
                    e.getWhoClicked().sendMessage("§7You §2§lsuccessfully §7gave yourself the §9§l"+e.getCurrentItem().getItemMeta().getDisplayName()+ " §7chat color§7.");
                    e.getWhoClicked().closeInventory();
                    executeMYSQL(e);
                    ChatColorsCache.getInstance().reloadChatColorsPlayersNow();
                });
            } else if (e.getCurrentItem().getData().toString().equalsIgnoreCase("LEATHER_CHESTPLATE(0)") && e.getCurrentItem().getItemMeta().getLore().get(5).equalsIgnoreCase("§4§lYou don't own this chat color yet§7§l.")) {
                e.getWhoClicked().sendMessage("§4§lYou don't own this chat color yet§7§l.");
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cReset chat color")) {
                CompletableFuture.runAsync(() -> {
                    e.getWhoClicked().sendMessage("§7You §2successfully §creset §7your chatcolor.");
                    e.getWhoClicked().closeInventory();
                    chatColorSQL.resetChatColor(e.getWhoClicked().getUniqueId());
                    ChatColorsCache.getInstance().reloadChatColorsPlayersNow();
                });
            }
            e.setCancelled(true);
        }
    }

    public void chatColorsMenu(Player p) {
        /*
        chatColorSQL.grantChatColorArray.clear();
        chatColorSQL.getPlayerChatName(p);
        uc.check(p.getDisplayName());
        chatColorSQL.getChatColorsGrantedBefore();
         */

        chatColorsInv = Bukkit.createInventory(null, 45,"§9§lChat§f§lColors");

        for (int i = 0; i < chatColorsInv.getSize(); i++) {
            ItemStack placeholder = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) ((int) 11));
            ItemMeta placeholderMeta = placeholder.getItemMeta();
            placeholderMeta.setDisplayName("§9§lChoose your chatColor!");
            placeholder.setItemMeta(placeholderMeta);
            chatColorsInv.setItem(i, placeholder);
        }

        try {
            for (int i = 0; i < chatColorsService.getAllChatColorsPlayers().length(); i++) {
                JSONParser jsParser = new JSONParser();
                JSONArray jsonA = (JSONArray) jsParser.parse(chatColorsService.getAllChatColorsPlayers().toString());
                JSONObject jsonOB = (JSONObject) jsonA.get(i);

                if (jsonOB.get("playerUUID").toString().equalsIgnoreCase(p.getUniqueId().toString())) {
                    String currentChatColorName = (String) jsonOB.get("currentChatColorName");

                    ItemStack resetChatColor = new ItemStack(Material.TORCH, 1);
                    ItemMeta resetChatColorMeta = resetChatColor.getItemMeta();
                    List<String> resetChatColorText = new ArrayList<>();
                    resetChatColorMeta.setDisplayName("§cReset chat color");
                    resetChatColorText.add(0, "§8§m-----------------------------------");
                    resetChatColorText.add(1, "§9§l"+currentChatColorName);
                    resetChatColorText.add(2, "§7Current chat color display: §2"+currentChatColorName.substring(0, 2)+p.getDisplayName()+"§7§l.");
                    resetChatColorText.add(3, "§8§m-----------------------------------");
                    resetChatColorText.add(4, "§eClick to reset your chatColor§7.");
                    resetChatColorMeta.setLore(resetChatColorText);
                    resetChatColor.setItemMeta(resetChatColorMeta);

                    chatColorsInv.setItem(4, resetChatColor);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }


        /*
            ItemStack customChatColorItem = new ItemStack(Material.BLAZE_ROD);
            ItemMeta customChatColorMeta = customChatColorItem.getItemMeta();
            List<String> customChatColorText = new ArrayList<>();
            customChatColorMeta.setDisplayName("§9§lCustom §f§lchatColor");
            customChatColorText.add(0, "§8§m-----------------------------------");
            customChatColorText.add(1, "§f§lCreate your §9§lown custom §f§lchat color");
            customChatColorText.add(2, "§8§m-----------------------------------");
            customChatColorText.add(3, "§cLeft click to create your own.");
            customChatColorMeta.setLore(customChatColorText);
            customChatColorItem.setItemMeta(customChatColorMeta);
            chatColorsInv.setItem(8, customChatColorItem);

         */

        for (int i = 0; i < chatColorsService.getAllChatColors().length(); i++) {
            try {
            JSONParser jsParser = new JSONParser();
            JSONArray jsonA = (JSONArray) jsParser.parse(chatColorsService.getAllChatColors().toString());
            JSONObject jsonOB = (JSONObject) jsonA.get(i);

            String colorName = (String) jsonOB.get("colorName");
            String color = (String) jsonOB.get("color");
            String colorRGB = (String) jsonOB.get("colorRGB");
            String[] rgbParts = colorRGB.split(",");
            String red = rgbParts[0].trim();
            String green = rgbParts[1].trim();
            String blue = rgbParts[2].trim();

            ItemStack placeColors = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
            LeatherArmorMeta placeColorsMeta = (LeatherArmorMeta) placeColors.getItemMeta();
            List<String> placeColorsDesc = new ArrayList<>();
            placeColorsDesc.add(0, "§8§m-----------------------------------");
            placeColorsDesc.add(1, "");
            placeColorsDesc.add(2, "§7§lDisplay: §2" + color+p.getDisplayName());
            placeColorsDesc.add(3, "");
            placeColorsDesc.add(4, "§8§m-----------------------------------");
            if (p.hasPermission("jeezy.core.chatColors.all")) {
                placeColorsDesc.add(5, "§a§lYou own this chat color§7§l.");
            } else {
                placeColorsDesc.add(5, "§4§lYou don't own this chat color yet§7§l.");
            }
            if (i == 7) {
                addUp += 2;
            } else if (i == 14) {
                addUp += 4;
            } else if (i >= 21) {
                break;
            }
            /*
            if (chatColorSQL.grantChatColorArray.size() != 0) {
                for (int x = 0; x < chatColorSQL.grantChatColorArray.size(); x++) {
                    if (chatColorSQL.grantChatColorArray.get(x).equalsIgnoreCase(colorName)) {
                        placeColorsDesc.remove(5);
                        placeColorsDesc.add(5, "§a§lYou own this chat color§7§l.");
                        break;
                    }
                }
            }
             */

            placeColorsMeta.setColor((Color.fromRGB(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue))));
            placeColorsMeta.setDisplayName(color+colorName);
            placeColorsMeta.setLore(placeColorsDesc);
            placeColors.setItemMeta(placeColorsMeta);
            chatColorsInv.setItem(addUp + i, placeColors);
            } catch (Exception e) {
            System.out.println(e);
            }
        }
        p.openInventory(chatColorsInv);
        addUp = 10;
    }

    private void executeMYSQL(org.bukkit.event.inventory.InventoryClickEvent e) {
        chatColorSQL.setChatColor(e);
    }
}