package de.jeezycore.commands.chat;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ChatDisabler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("chat-disable") && args.length > 0) {
                p.sendMessage("Usage: /chat-disable or enable");
            } else if (cmd.getName().equalsIgnoreCase("chat-disable") && args.length == 0) {
                if (p.hasPermission("jeezy.core.chat.disable")) {
                    p.sendMessage("§2You successfully disabled the chat §b§l" + p.getDisplayName());
                } else {
                    p.sendMessage("No permission.");
                }
                try {
                    MemorySection mc = (MemorySection) JeezyConfig.config_defaults.get("chat");
                    mc.set("muted", true);

                    JeezyConfig.config_defaults.save(JeezyConfig.config);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (cmd.getName().equalsIgnoreCase("chat-enable") && args.length > 0) {
                p.sendMessage("Usage: /chat-enable or disable");
            } else if (cmd.getName().equalsIgnoreCase("chat-enable") && args.length == 0) {
                if (p.hasPermission("jeezy.core.chat.enable")) {
                    p.sendMessage("§2You successfully enabled the chat §b§l" + p.getDisplayName());
                } else {
                    p.sendMessage("No permission.");
                }
                try {
                    MemorySection mc = (MemorySection) JeezyConfig.config_defaults.get("chat");
                    mc.set("muted", false);

                    JeezyConfig.config_defaults.save(JeezyConfig.config);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
