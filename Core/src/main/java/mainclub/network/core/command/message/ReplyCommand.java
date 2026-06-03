package mainclub.network.core.command.message;

import mainclub.network.core.Core;
import mainclub.network.core.manager.CoreManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReplyCommand implements CommandExecutor {
    private final CoreManager manager = Core.get().getManager();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("clubcore.command.messagereply")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        if(strings.length == 0) {
            sender.sendMessage("Use: /"+s+" <text>");
            return false;
        } else if(!manager.messageReply().containsKey(((Player)sender).getUniqueId())) {
            sender.sendMessage("No tenes mensajes para responder.");
            return false;
        }

        String message = "";
        for(int i = 0; i < strings.length; i++) message += strings[i]+" ";

        final Player target = Bukkit.getPlayer(manager.messageReply().get(((Player)sender).getUniqueId()));

        sender.sendMessage("(Yo -> "+target.getName()+") "+message);
        target.sendMessage("("+sender.getName()+" -> Yo) "+message);

        return false;
    }
}
