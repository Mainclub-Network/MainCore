package mainclub.network.core.utils.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventHandler extends Event {
    @Getter
    private static HandlerList handlerList = new HandlerList();
    @Getter
    private boolean cancelled;

    public EventHandler setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }

    public boolean create() {
        if (!this.isCancelled()) {
            Bukkit.getPluginManager().callEvent(this);
        }
        return !this.isCancelled();
    }



    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}