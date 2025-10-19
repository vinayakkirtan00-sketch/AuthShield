package com.authshield.plugin.listeners;

import com.authshield.plugin.AuthShieldPlugin;
import com.authshield.plugin.Session;  // NEW: Explicit import for Session

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

/**
 * Listens for player events to enforce authentication restrictions.
 */
public class PlayerListener implements Listener {

    private final AuthShieldPlugin plugin;

    public PlayerListener(AuthShieldPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        plugin.setLoggedIn(uuid, false);

        if (plugin.isRegistered(uuid)) {
            Session session = plugin.getSession(uuid);
            String ip = player.getAddress().getHostString();
            boolean autoLogin = false;
            if (session != null) {
                if (System.currentTimeMillis() < session.expiry && ip.equals(session.ip)) {
                    plugin.setLoggedIn(uuid, true);
                    autoLogin = true;
                } else {
                    plugin.removeSession(uuid);
                }
            }
            if (autoLogin) {
                player.sendMessage(plugin.getMessage("auto-login"));
            } else {
                player.sendMessage(plugin.getMessage("login-required"));
            }
        } else {
            player.sendMessage(plugin.getMessage("register-required"));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!plugin.isLoggedIn(uuid)) {
            plugin.removeSession(uuid);
        }
        // Keep session for potential reconnect if logged in
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (!plugin.isLoggedIn(player.getUniqueId())) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                event.setTo(from);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!plugin.isLoggedIn(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!plugin.isLoggedIn(player.getUniqueId())) {
            String message = event.getMessage().toLowerCase();
            if (!message.startsWith("/login") && !message.startsWith("/register")) {
                event.setCancelled(true);
                player.sendMessage(plugin.getMessage("must-login"));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.isLoggedIn(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!plugin.isLoggedIn(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!plugin.isLoggedIn(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.isLoggedIn(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.isLoggedIn(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!plugin.isLoggedIn(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (!plugin.isLoggedIn(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!plugin.isLoggedIn(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}