package mainclub.network.core.command;

import mainclub.network.core.Core;
import mainclub.network.core.manager.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DelHomeCommand implements CommandExecutor {
    private final HomeManager manager = Core.get().getHomes();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final Player player = (Player) sender;

        if(strings.length == 0) {
            sender.sendMessage("§cUse: /delhome <home>");
            return false;
        }
        else if(!manager.hasHome(player.getUniqueId(), strings[0])) {
            sender.sendMessage("§cHome no creado.");
            return false;
        }

        manager.delete(player.getUniqueId(), strings[0]);
        sender.sendMessage("§c§lHOME BORRADO§c!");
        return false;
    }
}