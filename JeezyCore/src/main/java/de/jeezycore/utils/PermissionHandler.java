package de.jeezycore.utils;


import de.jeezycore.db.JeezySQL;
import de.jeezycore.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;


public class PermissionHandler {

    public static HashMap<UUID,PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();
    public static PermissionAttachment attachment;


    public void onAddPerms(Player p, String perm) {
        if (JeezySQL.permPlayerUUIDArray.size() == 0) return;

        for (int i = 0; i < JeezySQL.permPlayerUUIDArray.size(); i++) {
            System.out.println(Bukkit.getServer().getPlayer(UUID.fromString(JeezySQL.permPlayerUUIDArray.get(i))));
            if (Bukkit.getServer().getPlayer(UUID.fromString(JeezySQL.permPlayerUUIDArray.get(i))) == null) {
                continue;
            }
             attachment = Bukkit.getServer().getPlayer(UUID.fromString(JeezySQL.permPlayerUUIDArray.get(i))).addAttachment(Main.getPlugin(Main.class));

            perms.put(UUID.fromString(JeezySQL.permPlayerUUIDArray.get(i)), attachment);

            attachment = perms.get(UUID.fromString(JeezySQL.permPlayerUUIDArray.get(i)));
            System.out.println(attachment);
            if (attachment == null) {
                return;
            }
            attachment.setPermission(perm, true);

        }
        JeezySQL.permPlayerUUIDArray.clear();

    }

    public void onRemovePerms(Player p, String perm) {
        if (JeezySQL.permPlayerUUIDArray.size() == 0) return;

        for (int i = 0; i < JeezySQL.permPlayerUUIDArray.size(); i++) {
            System.out.println(Bukkit.getServer().getPlayer(UUID.fromString(JeezySQL.permPlayerUUIDArray.get(i))));
            if (Bukkit.getServer().getPlayer(UUID.fromString(JeezySQL.permPlayerUUIDArray.get(i))) == null) {
                continue;
            }
            attachment = Bukkit.getServer().getPlayer(UUID.fromString(JeezySQL.permPlayerUUIDArray.get(i))).addAttachment(Main.getPlugin(Main.class));

            perms.put(UUID.fromString(JeezySQL.permPlayerUUIDArray.get(i)), attachment);

            attachment = perms.get(UUID.fromString(JeezySQL.permPlayerUUIDArray.get(i)));
            System.out.println(attachment);
            if (attachment == null) {
                return;
            }
            attachment.setPermission(perm, false);

        }
        JeezySQL.permPlayerUUIDArray.clear();

    }

    public void onJoin(UUID u) {
        if (JeezySQL.joinPermRanks == null) return;
        String[] uuidStrings = JeezySQL.joinPermRanks.replace("[", "").replace("]", "").
                replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5").split(", ");


            attachment = Bukkit.getServer().getPlayer(UUID.fromString(String.valueOf(u))).addAttachment(Main.getPlugin(Main.class));

            perms.put(UUID.fromString(String.valueOf(u)), attachment);


        for (int i = 0; i < uuidStrings.length; i++) {
            attachment.setPermission(uuidStrings[i], true);

        }
    }

    public void onGranting(HumanEntity p) {
        if (JeezySQL.grantingPermRanks == null) return;
        String[] uuidStrings = JeezySQL.grantingPermRanks.replace("[", "").replace("]", "").
                replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5").split(", ");

        try {
            attachment = Bukkit.getServer().getPlayer(ArrayStorage.grant_array.get(p.getUniqueId())).addAttachment(Main.getPlugin(Main.class));

            perms.put(ArrayStorage.grant_array.get(p.getUniqueId()), attachment);


            for (int i = 0; i < uuidStrings.length; i++) {
                attachment.setPermission(uuidStrings[i], true);

            }
        }catch (Exception e) {

        }

    }

    public void onUnGranting(HumanEntity p) {
        if (JeezySQL.unGrantingPermRanks == null) return;
        String[] uuidStrings = JeezySQL.unGrantingPermRanks.replace("[", "").replace("]", "").
                replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5").split(", ");

    try {
        attachment = Bukkit.getServer().getPlayer(ArrayStorage.grant_array.get(p.getUniqueId())).addAttachment(Main.getPlugin(Main.class));

        perms.put(ArrayStorage.grant_array.get(p.getUniqueId()), attachment);


        for (int i = 0; i < uuidStrings.length; i++) {
            attachment.setPermission(uuidStrings[i], false);

        }
    } catch (Exception e) {

    }

    }

}
