package de.jeezycore.utils;

import com.nametagedit.plugin.NametagEdit;
import de.jeezycore.db.TabListSQL;
import org.bukkit.entity.Player;

import static de.jeezycore.colors.ColorTranslator.colorTranslator;
import static de.jeezycore.utils.ArrayStorage.tab_name_list_array;

public class NameTag {

    TabListSQL tabListSQL = new TabListSQL();

    public void giveTagOnJoin(Player player) {
            tabListSQL.getTabListData(player);

            if (colorTranslator.get(tab_name_list_array.get(player)) != null) {
                NametagEdit.getApi().setPrefix(player, colorTranslator.get(tab_name_list_array.get(player)));
            } else {
                NametagEdit.getApi().setPrefix(player, "§2");
            }
        }

    public void giveTagAfterMatch(Player player1, Player player2) {

        if (colorTranslator.get(tab_name_list_array.get(player1)) != null) {
            NametagEdit.getApi().setPrefix(player1, colorTranslator.get(tab_name_list_array.get(player1)));
        } else {
            NametagEdit.getApi().setPrefix(player1, "§2");
        }

        if (colorTranslator.get(tab_name_list_array.get(player2)) != null) {
            NametagEdit.getApi().setPrefix(player2, colorTranslator.get(tab_name_list_array.get(player2)));
        } else {
            NametagEdit.getApi().setPrefix(player2, "§2");
        }
    }

    public void clearTagOnMatchStart(Player player1, Player player2) {
        NametagEdit.getApi().clearNametag(player1);
        NametagEdit.getApi().clearNametag(player2);
    }

}
