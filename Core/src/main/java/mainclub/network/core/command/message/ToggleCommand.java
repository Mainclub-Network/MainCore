package mainclub.network.core.command.message;

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
        if(!sender.hasPermission("clubcore.command.messagetoggle")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }
        final Player player = (Player) sender;
        if(manager.messagesToggled().contains(player.getUniqueId())) {
            manager.messagesToggled().remove(player.getUniqueId());
            sender.sendMessage("Ahora todos pueden enviarte mensajes.");
            return false;
        }
        manager.messagesToggled().add(player.getUniqueId());
        sender.sendMessage("Mensajes privados desactivados.");
        return false;
    }
}
