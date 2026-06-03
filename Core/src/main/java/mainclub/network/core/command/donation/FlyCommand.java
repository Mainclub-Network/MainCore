package mainclub.network.core.command.donation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.fly")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        final Player player = (Player) sender;
        if(player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            sender.sendMessage("§cYa no podes volar.");
            return false;
        }

        player.setAllowFlight(true);
        player.setFlying(true);
        sender.sendMessage("§aAhora podes volar!");
        return false;
    }
}
