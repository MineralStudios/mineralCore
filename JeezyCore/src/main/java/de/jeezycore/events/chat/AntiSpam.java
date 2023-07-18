package de.jeezycore.events.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class AntiSpam implements Listener {
    HashMap<UUID, Integer> messageCounterList  = new HashMap<>();
    HashMap<UUID, Integer> kickCounterList = new HashMap<>();

    int messageCounter = 5;
    int kickCounter = 0;

    @EventHandler
    public void AntiSpamChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().hasPermission("jeezy.core.bypass.anti.spam")) {
            return;
        }
        if (messageCounterList.containsKey(e.getPlayer().getUniqueId()) && kickCounterList.containsKey(e.getPlayer().getUniqueId())) {
            e.getPlayer().sendMessage("§cYou will be able to send a message after "+messageCounterList.get(e.getPlayer().getUniqueId())+" second(s). §7§lSilver §cand up bypasses the antispam.");
            e.setCancelled(true);
            int updateKickCounter = kickCounterList.get(e.getPlayer().getUniqueId());
            kickCounterList.put(e.getPlayer().getUniqueId(), updateKickCounter + 1);
            if (kickCounterList.get(e.getPlayer().getUniqueId()) == 4) {
               // e.getPlayer().kickPlayer("§cdisconnect.spam");
                kickCounterList.put(e.getPlayer().getUniqueId(), 0);
            }
            if (messageCounterList.get(e.getPlayer().getUniqueId()) > 1) {
                int updateMessageCounter = messageCounterList.get(e.getPlayer().getUniqueId());
                messageCounterList.put(e.getPlayer().getUniqueId(), updateMessageCounter - 1);
            }
        } else {
            messageCounterList.put(e.getPlayer().getUniqueId(), messageCounter);
            kickCounterList.put(e.getPlayer().getUniqueId(), kickCounter);
            timeRemover(e);
        }

    }

    public void timeRemover(AsyncPlayerChatEvent e) {
        Timer time = new Timer();
        TimerTask AntiSpamTask = new TimerTask() {
            @Override
            public void run() {
                messageCounterList.remove(e.getPlayer().getUniqueId());
                kickCounterList.remove(e.getPlayer().getUniqueId());
                System.out.println("removed");
                time.purge();
                time.cancel();
            }
        };
        time.schedule(AntiSpamTask, 5000);
    }
    }