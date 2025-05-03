package de.jeezycore.commands.basic;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Reboot implements CommandExecutor {

    String FolderPath = new File(".").getAbsoluteFile().getParentFile().getAbsolutePath();
    String restartScript = FolderPath+"/start.sh";

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
        kickAllPlayers();
        shutDownServer();
        restartServer();
    }

    private void kickAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("§fThe §9server §fis restarting!");
        }
    }

    public void shutDownServer() {
        Bukkit.getServer().shutdown();
    }

    private void restartServer() {
        try {
            List<String> scriptLines = Files.readAllLines(Paths.get(restartScript));

            String command = String.join(" ", scriptLines);
            command = command.replace("-S", "-dmS");

            System.out.println("Executing: " + command);

            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);

            processBuilder.inheritIO();

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            System.out.println("Script executed with exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}