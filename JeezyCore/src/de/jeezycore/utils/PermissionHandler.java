package de.jeezycore.utils;


import de.jeezycore.db.JeezySQL;
import de.jeezycore.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

public class PermissionHandler {

    public static HashMap<UUID,PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();

    public void perms(Player p) {

        String[] uuidStrings = JeezySQL.permPlayerName.replace("[", "").replace("]", "").
                replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5").split(", ");

        for (int i = 0; i < uuidStrings.length; i++) {
            System.out.println(uuidStrings[i]);
            PermissionAttachment attachment = Bukkit.getServer().getPlayer(UUID.fromString(uuidStrings[i])).addAttachment(Main.getPlugin(Main.class));

            perms.put(UUID.fromString(uuidStrings[i]), attachment);

            PermissionAttachment set_perms = perms.get(UUID.fromString(uuidStrings[i]));
            System.out.println(set_perms);
            if (set_perms == null) {
                return;
            }
            set_perms.setPermission("ajparkour.setup", true);
        }

    }
}
