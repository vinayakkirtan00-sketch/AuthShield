package com.authshield.plugin.commands;

import com.authshield.plugin.AuthShieldPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handles admin commands like /authshield reset <player>.
 */
public class AdminCommand implements CommandExecutor {

    private final AuthShieldPlugin plugin;

    public AdminCommand(AuthShieldPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("authshield.admin")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }

        if (args.length != 2 || !args[0].equalsIgnoreCase("reset")) {
            sender.sendMessage("Usage: /authshield reset <player>");
            return true;
        }

        String playerName = args[1];
        if (plugin.resetPassword(playerName)) {
            sender.sendMessage(plugin.getMessage("reset-success").replace("%player%", playerName));
        } else {
            sender.sendMessage("Player not found or not registered.");
        }
        return true;
    }
}