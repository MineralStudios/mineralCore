package de.jeezycore.utils;

import de.jeezycore.colors.Color;
import org.bukkit.entity.Player;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import static de.jeezycore.utils.ArrayStorage.nameMcLikes;

public class NameMC {

    public void getLikes() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.namemc.com/server/mineral.gg/likes"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            nameMcLikes.addAll(Collections.singleton(response.body()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void checkNameMc(Player p) {
       if (!checkIfVoted(p)) {
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


    public boolean checkIfVoted(Player p) {
        return nameMcLikes.get(0).contains(p.getPlayer().getUniqueId().toString());
    }

}