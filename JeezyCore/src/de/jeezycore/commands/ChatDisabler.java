package de.jeezycore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;


public class ChatDisabler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("chat-disable") && args.length > 0) {
                p.sendMessage("Usage: /chat-disable or enable");
            } else if (cmd.getName().equalsIgnoreCase("chat-disable") && args.length == 0) {
                p.sendMessage("§2You successfully disabled the chat §b§l"+p.getDisplayName());
                JSONObject jsobj_enable = new JSONObject();
                jsobj_enable.put("chat_muted", true);
                //Write JSON file
                try (FileWriter file = new FileWriter("C:\\Users\\Lassd\\IdeaProjects\\JeezyDevelopment\\JeezyCore\\src\\config.json")) {
                    //We can write any JSONArray or JSONObject instance to the file
                    file.write(jsobj_enable.toJSONString());
                    file.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (cmd.getName().equalsIgnoreCase("chat-enable") && args.length > 0) {
                p.sendMessage("Usage: /chat-enable or disable");
            } else if (cmd.getName().equalsIgnoreCase("chat-enable") && args.length == 0) {
                p.sendMessage("§2You successfully enabled the chat §b§l"+p.getDisplayName());
                JSONObject jsobj_disable = new JSONObject();
                jsobj_disable.put("chat_muted", false);
                //Write JSON file
                try (FileWriter file = new FileWriter("C:\\Users\\Lassd\\IdeaProjects\\JeezyDevelopment\\JeezyCore\\src\\config.json")) {
                    //We can write any JSONArray or JSONObject instance to the file
                    file.write(jsobj_disable.toJSONString());
                    file.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


        return false;
    }
}
