package mainclub.network.core.command.admin;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!sender.hasPermission("core.command.clearchat")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        Bukkit.getOnlinePlayers().forEach(player-> player.sendMessage(StringUtils.repeat(" \n", 120)));
        Bukkit.broadcastMessage("§aChat cleared by "+sender.getName()+".");
        return false;
    }
}
