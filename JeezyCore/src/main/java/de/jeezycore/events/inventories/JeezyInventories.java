package de.jeezycore.events.inventories;

import de.jeezycore.events.inventories.grant.GrantInventory;
import de.jeezycore.events.inventories.manage.ManageInventory;
import de.jeezycore.events.inventories.profiles.ProfileInventory;
import de.jeezycore.events.inventories.punishments.bans.BansInventory;
import de.jeezycore.events.inventories.punishments.PunishmentInventory;
import de.jeezycore.events.inventories.punishments.mutes.MutesInventory;
import de.jeezycore.events.inventories.rewards.RewardsInventory;
import de.jeezycore.events.inventories.tags.TagsInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JeezyInventories implements Listener {

    @EventHandler
    public void onCLickEvent(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null || e.getClickedInventory() == null) {
            return;
        }

        GrantInventory grantInventory = new GrantInventory();
        grantInventory.run(e);

        ProfileInventory profileInventory = new ProfileInventory();
        profileInventory.run(e);

        ManageInventory manageInventory = new ManageInventory();
        manageInventory.run(e);

        PunishmentInventory punishmentInventory = new PunishmentInventory();
        punishmentInventory.run(e);

        BansInventory bansInventory = new BansInventory();
        bansInventory.run(e);

        MutesInventory mutesInventory = new MutesInventory();
        mutesInventory.run(e);

        TagsInventory tagsInventory = new TagsInventory();
        tagsInventory.run(e);

        RewardsInventory rewardsInventory = new RewardsInventory();
        rewardsInventory.run(e);
    }
}