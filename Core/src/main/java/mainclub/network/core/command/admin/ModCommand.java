package mainclub.network.core.command.admin;

import mainclub.network.core.Core;
import mainclub.network.core.utils.Moderation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModCommand implements CommandExecutor {
    private Moderation moderation = Core.get().getModeration();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!sender.hasPermission("core.command.modmode")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        final Player player = (Player) sender;
        final boolean value = moderation.isModMode(player.getUniqueId());
        moderation.setModMode(player.getUniqueId(), !value);
        player.sendMessage(value ? "§cYa no estás en ModMode!":"§aAhora estás en ModMode!");
        return false;
    }
}
