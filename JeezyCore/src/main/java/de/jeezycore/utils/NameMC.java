package de.jeezycore.utils;

import de.jeezycore.discord.messages.nameMc.RealtimeNameMc;
import org.bukkit.entity.Player;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import static de.jeezycore.utils.ArrayStorage.*;

public class NameMC {

    Timer timer = new Timer();
    RealtimeNameMc nameMc = new RealtimeNameMc();

    public void getLikes() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gettingNameMcLikesResponse = null;
                nameMcVoters.clear();
                prepareNameMcVoters.clear();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.namemc.com/server/mineral.gg/likes"))
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                try {
                    gettingNameMcLikesResponse = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                    String[] prepareResponse = gettingNameMcLikesResponse.body().replace("[", "").replace("]", "").split(",");

                    prepareNameMcVoters.addAll(List.of(prepareResponse));

                    for (int i = 0; i < prepareNameMcVoters.size(); i++) {
                        nameMcVoters.add(prepareNameMcVoters.get(i).substring(4, 40).trim());
                    }

                    checkForNewVoters();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 180000);
    }

    public void checkNameMc(Player p) {
       if (!checkIfAlreadyVoted(p)) {
          p.sendMessage(new String[] {
                  "\n",
                  "\n",
                  " §f§lVote for §9§lMineral",
                  "\n",
                  " §fIt seems like you haven't voted for §9Mineral §7yet!",
                  " §7Vote here to get a free §9in-game §7rank:",
                  " §9https://namemc.com/server/mineral.gg"
       });
        }
    }


    public boolean checkIfAlreadyVoted(Player p) {
        return gettingNameMcLikesResponse != null ? gettingNameMcLikesResponse.body().contains(p.getPlayer().getUniqueId().toString()) : false;
    }

    private void checkForNewVoters() {

        if (nameMcOldVoters.isEmpty()) {
            for (int i = 0; i < prepareNameMcVoters.size(); i++) {
                nameMcOldVoters.add(prepareNameMcVoters.get(i).substring(4, 40).trim());
            }
            return;
        }

        nameMcVoters.removeAll(nameMcOldVoters);

        if (!nameMcVoters.isEmpty()) {
            for (int i = 0; i < nameMcVoters.size(); i++) {
                sendDiscordMessage(nameMcVoters.get(i), nameMcOldVoters.size() + nameMcVoters.size());
            }
        }

        nameMcOldVoters.clear();
        for (int i = 0; i < prepareNameMcVoters.size(); i++) {
            nameMcOldVoters.add(prepareNameMcVoters.get(i).substring(4, 40).trim());
        }
    }

    private void sendDiscordMessage (String playerUUID, Integer serverLikes) {
        nameMc.nameMcNewVoterNotification(playerUUID, serverLikes);
    }
}
