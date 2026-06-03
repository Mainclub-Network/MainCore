package mainclub.network.core.command.message;

import mainclub.network.core.Core;
import mainclub.network.core.manager.CoreManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageCommand implements CommandExecutor {
    private final Core main = Core.get();
    private final CoreManager manager = main.getManager();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("clubcore.command.message")) {
            sender.sendMessage("Comando denegado.");
            return false;
        }

        if(strings.length == 0) {
            sender.sendMessage("Use /"+s+" <player> <text>");
            return false;
        }

        final Player target = Bukkit.getPlayer(strings[0]);
        if (target == null || !target.isOnline() || main.getModeration().isVanish(target.getUniqueId())) {
            sender.sendMessage("Player no encontrado.");
            return false;
        } else if (target.getName().equals(sender.getName())) {
            sender.sendMessage("No podes enviarte mensajes a ti mismo.");
            return false;
        } else if(manager.messagesToggled().contains(target.getUniqueId())) {
            sender.sendMessage(target.getName()+" tiene los mensajes desactivados.");
            return false;
        } else if (manager.messagesIgnore().containsKey(target.getUniqueId()) && manager.messagesIgnore().get(target.getUniqueId()).contains(((Player)sender).getUniqueId())) {
            sender.sendMessage("Mensaje cancelado, "+target.getName()+" te está ignorando.");
            return false;
        } else if (strings.length == 1) {
            sender.sendMessage("Use /"+s+" "+target.getName()+" <text>");
            return false;
        }
        String message = "";
        for(int i = 1; i < strings.length; i++) message += strings[i]+" ";

        sender.sendMessage("(Yo -> "+target.getName()+") "+message);
        target.sendMessage("("+sender.getName()+" -> Yo) "+message);

        manager.messageReply().put(((Player)sender).getUniqueId(), target.getUniqueId());
        return false;
    }
}
