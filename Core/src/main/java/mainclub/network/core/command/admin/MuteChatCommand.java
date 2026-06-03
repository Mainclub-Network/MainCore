package mainclub.network.core.command.admin;

import mainclub.network.core.Core;
import mainclub.network.core.utils.Moderation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MuteChatCommand implements CommandExecutor {
    private final Moderation moderation = Core.get().getModeration();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
       if(!sender.hasPermission("core.command.mutechat")) {
           sender.sendMessage("§cNo permission.");
           return false;
       }

        final boolean muted = moderation.isMuteChat();
        moderation.setMuteChat(!muted);
        Bukkit.broadcastMessage((muted ? "§aChat unmuted by ":"§cChat muted by ") +sender.getName()+".");
        return false;
    }
}
