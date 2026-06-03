package mainclub.network.core.command.admin;

import mainclub.network.core.Core;
import mainclub.network.core.manager.SpawnManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCommand implements CommandExecutor {
    private final SpawnManager manager = Core.get().getSpawn();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission("core.command.setspawn")) {
            sender.sendMessage("§cUse: /" + s + ".");
            return false;
        }

        manager.set(((Player) sender).getLocation());
        sender.sendMessage("§aSpawn created!");
        return false;
    }
}
