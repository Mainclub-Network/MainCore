package mainclub.network.core.command.admin.gamemode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.gamemode")) {
            sender.sendMessage("Comando denegado.");
            return false;
        }

        if(strings.length == 0) {
            sender.sendMessage("§cUse: /"+s+" <mode>");
            return false;
        }

        final Player player = (Player) sender;
        final String currentMode = player.getGameMode().toString().charAt(0)+player.getGameMode().toString().substring(1).toLowerCase();

        if(strings[0].equalsIgnoreCase("0") || strings[0].equalsIgnoreCase("survival")) {
            sender.sendMessage("§bGameMode: §7"+currentMode+" -> §aSurvival");
            player.setGameMode(GameMode.SURVIVAL);
            return false;
        } else if(strings[0].equalsIgnoreCase("1") || strings[0].equalsIgnoreCase("creative")) {
            sender.sendMessage("§bGameMode: §7"+currentMode+" -> §eCreative");
            player.setGameMode(GameMode.CREATIVE);
            return false;
        } else if (strings[0].equalsIgnoreCase("2") || strings[0].equalsIgnoreCase("adventure")) {
            sender.sendMessage("§bGameMode: §7"+currentMode+" -> §2Adventure");
            player.setGameMode(GameMode.ADVENTURE);
            return false;
        } else if (strings[0].equalsIgnoreCase("3") || strings[0].equalsIgnoreCase("spectator")) {
            sender.sendMessage("§bGameMode: §7"+currentMode+" -> §cSpectator");
            player.setGameMode(GameMode.SPECTATOR);
            return false;
        }

        sender.sendMessage("§cUse: /"+s+" <mode>");
        return false;

    }
}
