package de.jeezycore.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ArrayStorage {
    // Msg command array
    public static HashMap<String, String> reply_array = new HashMap<String, String>();

    // fly command array
    public  static ArrayList<String> fly_array = new ArrayList<String>();

    // >> Giving player rank / or remove
    // grant arrays
    public static HashMap<UUID, UUID> grant_array = new HashMap<>();

    public static HashMap<UUID, String> grant_array_names = new HashMap<UUID, String>();

    public static HashMap<String, Inventory> grant_inv_array = new HashMap<>();

    // profile array
    public static HashMap<String, Inventory> profile_inv_array = new HashMap<>();

    // manage_menu array
    public static HashMap<String, Inventory> manage_menu_inv_array = new HashMap<>();

    // punishments_menu array
    public static HashMap<String, Inventory> punishments_menu_inv_array = new HashMap<>();

    // ban_logs array
    public static ArrayList<JSONObject> ban_logs = new ArrayList<>();

    // mute_logs array
    public static ArrayList<JSONObject> mute_logs = new ArrayList<>();

    // tags array
    public static HashMap<UUID, Integer> tags_inv_array = new HashMap<>();

    public static ArrayList<String> tags_in_ownership_array = new ArrayList<>();

    public static ArrayList<String> set_current_tag_array = new ArrayList<>();

    public static HashMap<Player, Integer> tab_name_list_array = new HashMap<>();

}
