package mainclub.network.core.command.admin;

import mainclub.network.core.Core;
import mainclub.network.core.utils.Moderation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {
    private Moderation moderation = Core.get().getModeration();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!sender.hasPermission("core.command.freeze")) {
            sender.sendMessage("§cNo permission.");
            return false;
        } else if(strings.length == 0) {
            sender.sendMessage("Use: /freeze <player>");
            return false;
        } else if(Bukkit.getPlayer(strings[0]) == null || !Bukkit.getPlayer(strings[0]).isOnline()) {
            sender.sendMessage("Player "+strings[0]+" no encontrado.");
            return false;
        } else if (Bukkit.getPlayer(strings[0]).hasPermission("core.command.freeze") || strings[0].equalsIgnoreCase(sender.getName())) return false;


        final Player target = Bukkit.getPlayer(strings[0]);
        final boolean value = moderation.isFrozen(target.getUniqueId());
        moderation.setFrozen(target.getUniqueId(), !value);
        moderation.broadcastToStaff("§4§l[SS] "+ (value ? "§a"+target.getName()+" fue descongelado. by §e"+sender.getName()+"§a.":"§3"+target.getName()+" §bahora está congelado. by §e"+sender.getName()+"§b."));
        if(!value) target.sendMessage("§4§l[SS] §c"+"Ahora estás congelado!\n§e Sigue las instrucciones de §b"+sender.getName()+"§e.");
        return false;
    }
}