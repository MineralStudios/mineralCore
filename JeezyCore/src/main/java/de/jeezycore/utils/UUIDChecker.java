package de.jeezycore.utils;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;

public class UUIDChecker {
        public static String uuid;
        public static String uuidName;
        public void check(String userName)  {

                for (Player ps : Bukkit.getOnlinePlayers()) {
                        if (ps.getName().equalsIgnoreCase(userName)) {
                                 uuid = ps.getUniqueId().toString().replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
                                 uuidName = ps.getPlayer().getDisplayName();
                                 System.out.println(uuid);
                        } else {
                                try {
                                        String user = userName;
                                        String url = "https://api.mojang.com/users/profiles/minecraft/"+user;
                                        String UUIDJson = IOUtils.toString(new URL(url));
                                        JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
                                        uuid = UUIDObject.get("id").toString().replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
                                        uuidName = UUIDObject.get("name").toString();
                                        System.out.println(uuid);
                                        System.out.println(uuidName);
                                } catch (Exception e) {
                                   System.out.println(e);
                                }


                        }
                }


        }


}
