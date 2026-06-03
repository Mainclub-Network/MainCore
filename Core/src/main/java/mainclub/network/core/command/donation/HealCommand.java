package mainclub.network.core.command.donation;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.heal")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        if(strings.length == 1) {
            final Player player = Bukkit.getPlayer(strings[0]);

            if(player != null && player.isOnline()) {
                player.setHealthScale(player.getMaxHealth());
                player.setFoodLevel(24);
                sender.sendMessage("§e"+player.getName()+ " healed!");
            } else sender.sendMessage("§c"+strings[0]+ " not online.");

            return false;
        }

        final Player player = (Player) sender;
        player.setHealthScale(player.getMaxHealth());
        player.setFoodLevel(24);
        player.sendMessage("§eHealed!");
        return false;
    }
}
