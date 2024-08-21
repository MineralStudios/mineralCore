package de.jeezycore.utils;

import net.suuft.libretranslate.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

public class ArrayStorage {
    // Msg command array

    public static HashMap<String, String> reply_array = new HashMap<String, String>();

    public static HashMap<UUID, String> msg_ignore_list = new HashMap<UUID, String>();

    public static ArrayList<String> msg_ignore_array = new ArrayList<>();

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
    public static HashMap<UUID, Inventory> punishments_menu_inv_array = new HashMap<>();

    // ban_logs array
    public static ArrayList<JSONObject> ban_logs = new ArrayList<>();

    // mute_logs array
    public static ArrayList<JSONObject> mute_logs = new ArrayList<>();

    // tags array
    public static HashMap<UUID, Integer> tags_inv_array = new HashMap<>();

    public static ArrayList<String> tags_in_ownership_array = new ArrayList<>();

    public static ArrayList<String> set_current_tag_array = new ArrayList<>();

    public static HashMap<UUID, String> tab_name_list_array = new HashMap<>();

    public static ArrayList<ItemStack> rewardItems = new ArrayList<>();

    public static ArrayList<String> tagItems = new ArrayList<>();

    public static HashMap<Player, Inventory> rewardPerPLayerInventory = new HashMap<>();

    public static HashMap<Player, Integer> rewardPerPlayerTimer = new HashMap<>();

    public static HashMap<String, String> mineralsStorage = new HashMap<String, String>();

    public static HashMap<UUID, String> tagsCheckStatus = new HashMap<>();

   public static HashMap<String, Language> languageMap = new HashMap<>();

   //friends
    public static ArrayList<UUID> friendsOnJoinMessageArray = new ArrayList<>();

    public static ArrayList<String> friendsList = new ArrayList<>();

    public static ArrayList<String> showFriendsList = new ArrayList<>();


    public static ArrayList<UUID> friendRequestsArrayList = new ArrayList<>();
    public static HashMap<UUID, ArrayList<UUID>> friendRequestsList = new HashMap<>();

    public static ArrayList<UUID> friendsAddArrayList = new ArrayList<>();
    public static HashMap<UUID, ArrayList<UUID>> friendsAddList = new HashMap<>();

    // Tablist
    public static ArrayList<String> playerRankNames = new ArrayList<>();
    public static LinkedHashMap<String, String> rankTabListSorting = new LinkedHashMap<>();

    // NameMC

    public static HttpResponse<String> gettingNameMcLikesResponse;

    public static ArrayList<String> prepareNameMcVoters = new ArrayList<>();
    public static ArrayList<String> nameMcVoters = new ArrayList<>();

    public static ArrayList<String> nameMcOldVoters = new ArrayList<>();


}