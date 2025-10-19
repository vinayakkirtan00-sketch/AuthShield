package com.authshield.plugin.commands;

import com.authshield.plugin.AuthShieldPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

/**
 * Handles the /register command.
 */
public class RegisterCommand implements CommandExecutor {

    private final AuthShieldPlugin plugin;

    public RegisterCommand(AuthShieldPlugin plugin) {
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

        if (!player.hasPermission("authshield.register")) {
            player.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }

        if (plugin.isRegistered(uuid)) {
            player.sendMessage(plugin.getMessage("already-registered"));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(plugin.getMessage("register-usage"));
            return true;
        }

        String password = args[0];
        String confirm = args[1];

        if (!password.equals(confirm)) {
            player.sendMessage(plugin.getMessage("password-mismatch"));
            return true;
        }

        // Hash password with BCrypt
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        plugin.register(uuid, hashed);
        plugin.setLoggedIn(uuid, true);
        plugin.createSession(uuid, player.getAddress().getHostString());
        player.sendMessage(plugin.getMessage("register-success"));
        return true;
    }
}