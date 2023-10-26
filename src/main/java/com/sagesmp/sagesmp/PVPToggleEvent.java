package com.sagesmp.sagesmp;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PVPToggleEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final boolean isEnabled;

    public PVPToggleEvent(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isPvPEnabled() {
        return isEnabled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
