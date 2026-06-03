package mainclub.network.core.command.message;

import mainclub.network.core.Core;
import mainclub.network.core.manager.CoreManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IgnoreCommand implements CommandExecutor {
    private final CoreManager manager = Core.get().getManager();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("clubcore.command.messageignore")) {
            sender.sendMessage("Comando denegado.");
            return false;
        }

        if(strings.length == 0) {
            sender.sendMessage("Use: /"+s+" <player>");
            return false;
        }

        final Player target = Bukkit.getPlayer(strings[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage("Player no online.");
            return false;
        } else if (target.hasPermission("core.command.vanish")) {
            sender.sendMessage("No podes ignorar al staff.");
            return false;
        } else if (target.getName().equals(sender.getName())) {
            sender.sendMessage("No podes ignorarte a ti mismo.");
            return false;
        }

        if (manager.messagesIgnore().containsKey(((Player)sender).getUniqueId()) && manager.messagesIgnore().get(((Player)sender).getUniqueId()).contains(target.getUniqueId())) {
            manager.messagesIgnore().get(((Player)sender).getUniqueId()).remove(target.getUniqueId());
            sender.sendMessage("Ya no ignoras ha "+target.getName()+".");
            return false;
        }
        manager.messagesIgnore().get(((Player)sender).getUniqueId()).add(target.getUniqueId());
        sender.sendMessage("Ahora ignoras ha "+target.getName()+".");
        return false;
    }
}
