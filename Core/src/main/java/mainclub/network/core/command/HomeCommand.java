package mainclub.network.core.command;

import mainclub.network.core.Core;
import mainclub.network.core.manager.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeCommand implements CommandExecutor {
    private final HomeManager manager = Core.get().getHomes();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final Player player  = (Player) sender;


       // final int time = (player.hasPermission("group.hero") || player.hasPermission("group.mini-media"))? 3 : player.hasPermission("group.elite") ? 2 : player.hasPermission("group.legend") ? 1 : 5;
        final int time = 5;


        if(!manager.hasHome(player.getUniqueId())) {
            player.sendMessage("§cNo creaste un home. §7(/sethome)");
            return false;
        } else if(strings.length == 0) {
            Core.get().getCooldown().addCooldown(player.getUniqueId().toString() + ":home:" + manager.getHomes(player.getUniqueId()).get(0).split(";")[0], player.getName(), time);
            player.sendMessage("§fTransportando a tú §aHome§f en §b" + time + " seg§f...");
            return false;
        } else if(strings[0].equalsIgnoreCase("list")) {
            player.sendMessage(manager.getHomeList(player.getUniqueId()));
            return false;
        } else if(!manager.hasHome(player.getUniqueId(), strings[0])) {
            player.sendMessage("§cHome no creado. §e(/sethome "+strings[0]+")");
            return false;
        }


        Core.get().getCooldown().addCooldown(player.getUniqueId().toString()+":home:"+strings[0].toLowerCase(), player.getName(), time);
        player.sendMessage("§fTransportando a tú §aHome§f en §b"+time+" seg§f...");
        return false;
    }
}
