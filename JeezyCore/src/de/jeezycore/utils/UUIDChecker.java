package de.jeezycore.utils;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;

public class UUIDChecker {
        public String uuid;

        public void check(String userName)  {

                for (Player ps : Bukkit.getOnlinePlayers()) {
                        if (ps.getName().equalsIgnoreCase(userName)) {
                                 uuid = String.valueOf(ps.getUniqueId());
                                 System.out.println(uuid);
                        } else {
                                try {
                                        String user = userName;
                                        String url = "https://api.mojang.com/users/profiles/minecraft/"+user;
                                        String UUIDJson = IOUtils.toString(new URL(url));
                                        JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
                                        String uuid = UUIDObject.get("id").toString();
                                        System.out.println(uuid);
                                } catch (Exception e) {
                                   System.out.println(e);
                                }


                        }
                }


        }


}
