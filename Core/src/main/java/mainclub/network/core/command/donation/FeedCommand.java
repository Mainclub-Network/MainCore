package mainclub.network.core.command.donation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FeedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.feed")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        ((Player)sender).setFoodLevel(24);
        sender.sendMessage("§aFelt!");
        return false;
    }
}
