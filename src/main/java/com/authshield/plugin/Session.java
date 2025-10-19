package com.authshield.plugin;

/**
 * Represents a player session for auto-login.
 */
public class Session {
    public final String ip;
    public final long expiry;

    public Session(String ip, long expiry) {
        this.ip = ip;
        this.expiry = expiry;
    }
}