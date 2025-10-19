package com.authshield.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.authshield.plugin.commands.AdminCommand;
import com.authshield.plugin.commands.LoginCommand;
import com.authshield.plugin.commands.LogoutCommand;
import com.authshield.plugin.commands.RegisterCommand;
import com.authshield.plugin.listeners.PlayerListener;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Main class for the AuthShield plugin.
 * Handles player authentication, sessions, and data storage.
 */
public class AuthShieldPlugin extends JavaPlugin {

    private Map<UUID, Boolean> loggedIn = new HashMap<>();
    private Map<UUID, Session> sessions = new HashMap<>();
    private FileConfiguration config;
    private FileConfiguration messages;
    private FileConfiguration playersData;
    private File playersFile;

    @Override
    public void onEnable() {
        // Save default config if not exists
        saveDefaultConfig();
        config = getConfig();

        // Load players data
        playersFile = new File(getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            try {
                playersFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Failed to create players.yml");
            }
        }
        playersData = YamlConfiguration.loadConfiguration(playersFile);

        // Load messages
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        // Register commands
        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("login").setExecutor(new LoginCommand(this));
        getCommand("logout").setExecutor(new LogoutCommand(this));
        getCommand("authshield").setExecutor(new AdminCommand(this));

        // Register event listener
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getLogger().info("AuthShield enabled.");
    }

    @Override
    public void onDisable() {
        savePlayersData();
        getLogger().info("AuthShield disabled.");
    }

    /**
     * Saves the players data to file.
     */
    public void savePlayersData() {
        try {
            playersData.save(playersFile);
        } catch (IOException e) {
            getLogger().severe("Failed to save players.yml");
        }
    }

    /**
     * Gets a message from messages.yml, replacing color codes and %prefix%.
     * @param key The message key.
     * @return The formatted message.
     */
    public String getMessage(String key) {
        String msg = messages.getString(key, "Missing message: " + key).replace("&", "§");
        if (msg.contains("%prefix%")) {
            msg = msg.replace("%prefix%", getMessage("prefix"));
        }
        return msg;
    }

    /**
     * Checks if a player is registered.
     * @param uuid The player's UUID.
     * @return True if registered.
     */
    public boolean isRegistered(UUID uuid) {
        return playersData.contains(uuid.toString());
    }

    /**
     * Registers a player's hashed password.
     * @param uuid The player's UUID.
     * @param hashed The hashed password.
     */
    public void register(UUID uuid, String hashed) {
        playersData.set(uuid.toString(), hashed);
        savePlayersData();
    }

    /**
     * Gets the hashed password for a player.
     * @param uuid The player's UUID.
     * @return The hashed password or null.
     */
    public String getHashedPassword(UUID uuid) {
        return playersData.getString(uuid.toString());
    }

    /**
     * Sets a player's logged-in status.
     * @param uuid The player's UUID.
     * @param value True if logged in.
     */
    public void setLoggedIn(UUID uuid, boolean value) {
        loggedIn.put(uuid, value);
    }

    /**
     * Checks if a player is logged in.
     * @param uuid The player's UUID.
     * @return True if logged in.
     */
    public boolean isLoggedIn(UUID uuid) {
        return loggedIn.getOrDefault(uuid, false);
    }

    /**
     * Creates a session for the player if timeout > 0.
     * @param uuid The player's UUID.
     * @param ip The player's IP.
     */
    public void createSession(UUID uuid, String ip) {
        long timeout = config.getLong("session-timeout", 300); // seconds
        if (timeout > 0) {
            long expiry = System.currentTimeMillis() + (timeout * 1000);
            sessions.put(uuid, new Session(ip, expiry));
        }
    }

    /**
     * Gets the session for a player.
     * @param uuid The player's UUID.
     * @return The session or null.
     */
    public Session getSession(UUID uuid) {
        return sessions.get(uuid);
    }

    /**
     * Removes a player's session.
     * @param uuid The player's UUID.
     */
    public void removeSession(UUID uuid) {
        sessions.remove(uuid);
    }

    /**
     * Resets a player's password by removing their entry.
     * @param playerName The player's name.
     * @return True if reset successfully.
     */
    public boolean resetPassword(String playerName) {
        UUID uuid = getServer().getOfflinePlayer(playerName).getUniqueId();
        if (uuid != null && isRegistered(uuid)) {
            playersData.set(uuid.toString(), null);
            savePlayersData();
            return true;
        }
        return false;
    }
}