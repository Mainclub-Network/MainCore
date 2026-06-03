package mainclub.network.core.command.admin.gamemode.argument;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SurvivalArgument implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.gamemode")) {
            sender.sendMessage("Comando denegado.");
            return false;
        }

        sender.sendMessage("§bGameMode: §7"+((Player)sender).getGameMode()+" -> §aSurvival");
        ((Player)sender).setGameMode(GameMode.SURVIVAL);
        return false;
    }
}