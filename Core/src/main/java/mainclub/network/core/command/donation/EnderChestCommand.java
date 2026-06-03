package mainclub.network.core.command.donation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EnderChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.enderchest")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        final Player player = ((Player)sender);
        player.openInventory(player.getEnderChest());
        return false;
    }
}
