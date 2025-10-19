package com.authshield.plugin.commands;

import com.authshield.plugin.AuthShieldPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

/**
 * Handles the /login command.
 */
public class LoginCommand implements CommandExecutor {

    private final AuthShieldPlugin plugin;

    public LoginCommand(AuthShieldPlugin plugin) {
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

        if (!player.hasPermission("authshield.login")) {
            player.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }

        if (plugin.isLoggedIn(uuid)) {
            player.sendMessage(plugin.getMessage("already-logged-in"));
            return true;
        }

        if (!plugin.isRegistered(uuid)) {
            player.sendMessage(plugin.getMessage("not-registered"));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(plugin.getMessage("login-usage"));
            return true;
        }

        String password = args[0];
        String hashed = plugin.getHashedPassword(uuid);

        if (BCrypt.checkpw(password, hashed)) {
            plugin.setLoggedIn(uuid, true);
            plugin.createSession(uuid, player.getAddress().getHostString());
            player.sendMessage(plugin.getMessage("login-success"));
        } else {
            player.sendMessage(plugin.getMessage("incorrect-password"));
        }
        return true;
    }
}