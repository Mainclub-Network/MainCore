package mainclub.network.core.command.admin;

import mainclub.network.core.Core;
import mainclub.network.core.utils.Moderation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GodCommand implements CommandExecutor {
    private Moderation moderation = Core.get().getModeration();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.god")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        final Player player = (Player) sender;
        final boolean value = moderation.isGod(player.getUniqueId());

        moderation.setGod(player.getUniqueId(), !value);
        player.sendMessage(value ? "§cGodmode: §eNo" : "§cGodmode: §aYes");
        return false;
    }
}
