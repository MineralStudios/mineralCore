package de.jeezycore.commands.basic;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Reboot implements CommandExecutor {

    private final File startFile = new File("./start.sh");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("reboot") && args.length == 0) {
                if (p.hasPermission("jeezy.core.reboot")) {
                    rebootServer();
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
                p.sendMessage("Usage /reboot");
            }
        }
        return true;
    }

    public void rebootServer() {
        shutDownServer();
        startServer();
    }

    public void shutDownServer() {
        kickAllPlayers();
        Bukkit.getServer().shutdown();

    }

    public void kickAllPlayers() {
        for (Player ps: Bukkit.getOnlinePlayers()) {
            ps.kickPlayer("§fThe §9server §fis restarting!");
        }
    }

    public void startServer() {
        if (startFile.exists() && startFile.canExecute()) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(startFile.getAbsolutePath());
                processBuilder.directory(startFile.getParentFile());

                processBuilder.inheritIO();

                Map<String, String> environment = processBuilder.environment();
                environment.put("TERM", "xterm-256color");

                Process process = processBuilder.start();
                int exitCode = process.waitFor();

                System.out.println("Script exited with code: " + exitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File does not exist or is not executable: " + startFile.getAbsolutePath());
        }
    }
}