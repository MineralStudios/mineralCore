package de.jeezycore.utils;

import de.jeezycore.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.UUID;

public class PermissionHandler {

    public static HashMap<UUID,PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();

    public void perms(Player p) {
        PermissionAttachment attachment = p.addAttachment(Main.getPlugin(Main.class));

        perms.put(UUID.fromString("e150b93f-a86b-488c-8414-4065f2717c5c"), attachment);

        PermissionAttachment set_perms = perms.get(p.getUniqueId());
        System.out.println(set_perms);
        System.out.println(perms);
        if (set_perms == null) {
            return;
        }
        set_perms.setPermission("ajparkour.setup", true);
    }
}
