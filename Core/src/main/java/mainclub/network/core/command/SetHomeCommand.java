package mainclub.network.core.command;

import mainclub.network.core.Core;
import mainclub.network.core.manager.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetHomeCommand implements CommandExecutor {
    private final HomeManager manager = Core.get().getHomes();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final Player player = (Player) sender;
        if(strings.length == 0) {
            manager.create(player.getUniqueId(), "home", player.getLocation());
            player.sendMessage("§a§lHOME CREADO§a!");
            return false;
        } else if(strings.length > 1) {
            sender.sendMessage("§cUse: /sethome <home name>");
            return false;
        } else if (manager.hasHome(player.getUniqueId()) && manager.getHomes(player.getUniqueId()).size() >= 3) {
            sender.sendMessage("§cNo podes crear más homes. §7(/delhome <home name>)");
            return false;
        }

        manager.create(player.getUniqueId(), strings[0], player.getLocation());
        player.sendMessage("§a§lHOME §b§l"+strings[0].toUpperCase()+"§a§l CREADO§a!");
        return false;
    }
}