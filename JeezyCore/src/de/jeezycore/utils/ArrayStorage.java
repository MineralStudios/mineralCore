package de.jeezycore.utils;

import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class ArrayStorage {
    // Msg command array
    public static HashMap<String, String> reply_array = new HashMap<String, String>();

    // fly command array
    public  static ArrayList<String> fly_array = new ArrayList<String>();

    // grant array
    public static HashMap<String, String> grant_array = new HashMap<>();

    public static HashMap<String, Inventory> grant_inv_array = new HashMap<>();

}
