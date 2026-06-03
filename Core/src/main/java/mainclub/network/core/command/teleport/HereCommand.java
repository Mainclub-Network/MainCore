package mainclub.network.core.command.teleport;

import mainclub.network.core.Core;
import mainclub.network.core.manager.CoreManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HereCommand implements CommandExecutor {
    private final Core main = Core.get();
    private final CoreManager manager = main.getManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission("core.command.tphere")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        if (strings.length == 0) {
            sender.sendMessage("Use: /" + s + " <player>");
            return false;
        }

        final Player target = Bukkit.getPlayer(strings[0]);
        if (target == null || !target.isOnline() || main.getModeration().isVanish(target.getUniqueId())) {
            sender.sendMessage("Player no online.");
            return false;
        } else if (target.getName().equals(sender.getName())) {
            sender.sendMessage("No podes teletransportarte a ti mismo.");
            return false;
        } else if (manager.tpaToggled().contains(target.getUniqueId())) {
            sender.sendMessage(target.getName() + " tiene las teletransportaciones desactivadas.");
            return false;
        } else if (manager.tpaIgnore().containsKey(target.getUniqueId()) && manager.tpaIgnore().get(target.getUniqueId()).contains(((Player) sender).getUniqueId())) {
            sender.sendMessage("Teletransporte cancelado, " + target.getName() + " te está ignorando.");
            return false;
        }

        manager.requestTPA(target.getUniqueId(), ((Player) sender).getUniqueId(), true);
        sender.sendMessage("Solicitando teletransportacion...");
        return false;
    }
}