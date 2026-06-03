package mainclub.network.core.command;

import mainclub.network.core.Core;
import mainclub.network.core.manager.MenusManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 0) {
            Player player = Bukkit.getPlayer(sender.getName());
            player.openInventory(new MenusManager().getStats(player));
            return false;
        } else if(strings.length > 1) {
            sender.sendMessage("§cUse: /stats <player>");
            return false;
        }

        final String name = strings[0];
        final Player target = Bukkit.getPlayer(name);
        if(target == null || !target.isOnline()) {
            sender.sendMessage("§c"+name+ " no online.");
            return false;
        }

        target.openInventory(new MenusManager().getStats(target));
        return false;
    }

}
