package mainclub.network.core.command.donation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CraftCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.craft")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        ((Player)sender).openWorkbench(null, true);
        return false;
    }
}
