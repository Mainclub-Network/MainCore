package mainclub.network.core.command.admin;

import mainclub.network.core.Core;
import mainclub.network.core.utils.Moderation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {
    private Moderation moderation = Core.get().getModeration();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!sender.hasPermission("core.command.vanish")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        final Player player = (Player) sender;
        final boolean value = moderation.isVanish(player.getUniqueId());

        moderation.setVanish(player.getUniqueId(), !value);
        if (moderation.isModMode(player.getUniqueId())) player.getInventory().setItem(8, moderation.modTools(player.getUniqueId())[3]);
        player.sendMessage(value ? "§aAhora eres visible!" : "§2Ahora eres invisible!");
        return false;
    }
}
