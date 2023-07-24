package de.jeezycore.commands.basic;

import de.jeezycore.config.JeezyConfig;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.*;

public class Tips {

    MemorySection tipsConfig = (MemorySection) JeezyConfig.tips_defaults.get("TIP");

    ArrayList<String> tipsArrayList = new ArrayList<>();

    List<?> list_tips_one = tipsConfig.getList("one");
    List<?> list_tips_two = tipsConfig.getList("two");
    List<?> list_tips_three = tipsConfig.getList("three");
    List<?> list_tips_four = tipsConfig.getList("four");
    List<?> list_tips_five = tipsConfig.getList("five");


    private void randomizeTipsArray() {
        Collections.shuffle(tipsArrayList);
    }

    private void putIntoTipsList() {
        if (!list_tips_one.isEmpty()) tipsArrayList.add(list_tips_one.get(0).toString()); {}
        if (!list_tips_two.isEmpty()) tipsArrayList.add(list_tips_two.get(0).toString()); {}
        if (!list_tips_three.isEmpty()) tipsArrayList.add(list_tips_three.get(0).toString()); {}
        if (!list_tips_four.isEmpty()) tipsArrayList.add(list_tips_four.get(0).toString()); {}
        if (!list_tips_five.isEmpty()) tipsArrayList.add(list_tips_five.get(0).toString()); {}
    }

    public void runTips() {
        putIntoTipsList();
        randomizeTipsArray();
            for (Player ps : Bukkit.getOnlinePlayers()) {
                ps.sendMessage(new String[]{
                        "",
                        tipsArrayList.get(0),
                        ""
                });
            }
    }

    public void scheduleTips() {
        if (!tipsConfig.getBoolean("status")) {
            System.out.println("Seems to be false");
            return;
        }
        Timer time = new Timer();
        TimerTask tipsSendTask = new TimerTask() {
            @Override
            public void run() {
                runTips();
                System.out.println("tips");
            }
        };
        time.scheduleAtFixedRate(tipsSendTask, tipsConfig.getInt("delay"), tipsConfig.getInt("period"));
    }

}
