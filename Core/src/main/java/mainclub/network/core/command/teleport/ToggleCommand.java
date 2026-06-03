package mainclub.network.core.command.teleport;

import mainclub.network.core.Core;
import mainclub.network.core.manager.CoreManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand implements CommandExecutor {
    private final CoreManager manager = Core.get().getManager();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.tptoggle")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }
        final Player player = (Player) sender;
        if(manager.tpaToggled().contains(player.getUniqueId())) {
            manager.tpaToggled().remove(player.getUniqueId());
            sender.sendMessage("Ahora todos pueden enviarte solicitudes de teletransporte.");
            return false;
        }
        manager.tpaToggled().add(player.getUniqueId());
        sender.sendMessage("Solicitudes de teletransporte desactivadas.");
        return false;
    }
}
