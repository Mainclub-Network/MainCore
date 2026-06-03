package mainclub.network.core.command.admin;

import mainclub.network.core.Core;
import mainclub.network.core.manager.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {
    private final WarpManager manager = Core.get().getWarps();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!sender.hasPermission("core.command.setwarp")) {
            sender.sendMessage("§cNo permission.");
            return false;
        } else if(strings.length == 0) {
            sender.sendMessage("§cUse: /"+s+" <name>");
            return false;
        }

        final Player player = (Player) sender;
        final String warpName = strings[0];
        if(manager.has(warpName)) {
            player.sendMessage("§cWarp "+warpName+" already exists.");
            return false;
        }

        manager.create(warpName, player.getLocation());
        player.sendMessage("§aWarp "+warpName+" created.");
        return false;
    }
}
