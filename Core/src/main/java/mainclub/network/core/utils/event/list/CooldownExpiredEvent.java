package mainclub.network.core.utils.event.list;


import mainclub.network.core.utils.event.EventHandler;
import mainclub.network.core.utils.event.PlayerHandler;
import org.bukkit.entity.Player;

public class CooldownExpiredEvent extends EventHandler {
    private Player player;
    private String cooldownName;

    public CooldownExpiredEvent(final Player player, final String cooldownName) {
        this.player = player;
        this.cooldownName = cooldownName;
    }

    public PlayerHandler getPlayer() {
        return new PlayerHandler(player);
    }
    public String getName() {
        return cooldownName;
    }

}
