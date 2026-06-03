package mainclub.network.core.command;

import mainclub.network.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RealNickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 0) {
            sender.sendMessage("§cUse: /realnick <player>");
            return false;
        } else if(strings.length > 1) {
            sender.sendMessage("§cUse: /realnick <player>");
            return false;
        }

        final String name = strings[0];
        final Player player = Bukkit.getPlayer(name);
        if(player == null || !player.isOnline()) {
            sender.sendMessage("§c"+name+ " no online.");
            return false;
        }

        sender.sendMessage("§a"+player.getName()+" §f-> "+ Core.get().getBase().get(player.getUniqueId().toString()).getNick().replace("&", "§"));
        return false;
    }

}
