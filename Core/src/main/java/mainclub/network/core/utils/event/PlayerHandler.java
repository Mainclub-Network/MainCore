package mainclub.network.core.utils.event;

import org.bukkit.entity.Player;

public class PlayerHandler {
    private Player player;

    public PlayerHandler(final Player player) {
        this.player = player;
    }

    public Player get() {
        return player;
    }
}