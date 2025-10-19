package com.authshield.plugin.commands;

import com.authshield.plugin.AuthShieldPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Handles the /logout command.
 */
public class LogoutCommand implements CommandExecutor {

    private final AuthShieldPlugin plugin;

    public LogoutCommand(AuthShieldPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (!player.hasPermission("authshield.logout")) {
            player.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }

        if (!plugin.isLoggedIn(uuid)) {
            player.sendMessage(plugin.getMessage("not-logged-in"));
            return true;
        }

        plugin.setLoggedIn(uuid, false);
        plugin.removeSession(uuid);
        player.sendMessage(plugin.getMessage("logout-success"));
        return true;
    }
}