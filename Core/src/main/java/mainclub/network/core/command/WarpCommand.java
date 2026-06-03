package mainclub.network.core.command;

import mainclub.network.core.Core;
import mainclub.network.core.manager.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor  {
    private final WarpManager manager = Core.get().getWarps();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!sender.hasPermission("core.command.warp")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        if(strings.length == 0) {
            if(!sender.hasPermission("core.command.warp.admin")) {
                sender.sendMessage("§cUso: /"+s+" <nombre del warp>");
                return false;
            }
            sender.sendMessage("§cUso: /warp <warp:create:movehere:delete>.");
            return false;
        }

        final Player player = (Player) sender;
        final String label1 = strings[0];
        if(manager.has(label1)) {
            final String warpName = manager.toEquals(label1);
            if(!player.hasPermission("core.warp."+warpName)) {
                sender.sendMessage("§cWarp denegado.");// §e(/rankup)");
                return false;
            } else if(Core.get().getCooldown().isOnCooldown("warps", sender.getName())) {
                sender.sendMessage("§cYa estás siendo teletransportado a un warp.");
                return false;
            } else if(Core.get().getCooldown().isOnCooldown("spawn", sender.getName())) {
                sender.sendMessage("§cEstás siendo teletransportado al Spawn.");
                return false;
            }

            if (player.isOp()) {
                player.teleport(manager.location(warpName));
                player.sendMessage("§a§lTRANSPORTADO§a!");
                return false;
            } else {
                final int time = (player.hasPermission("group.hero") || player.hasPermission("group.mini-media"))? 3 : player.hasPermission("group.elite") ? 2 : player.hasPermission("group.legend") ? 1 : 5;
                Core.get().getCooldown().addCooldown("warp", player.getName(), time);
                manager.teleporting().put(player.getName(), warpName);
                player.sendMessage("§fApareceras en §e" + warpName + "§f en §b"+time+" seg§f...");
                return false;
            }

        }

        if(strings.length == 1) {
            if (label1.equalsIgnoreCase("list")) {
                player.sendMessage("§a§lWarps§a:§f "+manager.getWarpsList(player));
                return false;
            }
            player.chat("/warp");
            return false;
        }

        if(!sender.hasPermission("core.command.warp.admin")) {
            sender.sendMessage("§cUso: /"+s+" <warp:list>");
            return false;
        }

        if (label1.equalsIgnoreCase("create") || label1.equalsIgnoreCase("set")) {
            if(manager.has(strings[1])) {
                player.sendMessage("§cWarp "+strings[1]+" already exists.");
                return false;
            }

            manager.create(strings[1], player.getLocation());
            player.sendMessage("§aWarp "+strings[1]+" created.");
            return false;
        }

        if (label1.equalsIgnoreCase("movehere")) {
            if(!manager.has(strings[1])) {
                player.sendMessage("§cWarp "+strings[1]+" not exists.");
                return false;
            }

            manager.moveLocation(strings[1], player.getLocation());
            player.sendMessage("§aWarp "+strings[1]+" location move to here.");
            return false;
        }

        if (label1.equalsIgnoreCase("delete") || label1.equalsIgnoreCase("remove") || label1.equalsIgnoreCase("del")) {
            if(!manager.has(strings[1])) {
                player.sendMessage("§cWarp "+strings[1]+" not exists.");
                return false;
            }

            manager.delete(strings[1]);
            player.sendMessage("§4Warp "+strings[1]+" deleted.");
            return false;
        }

        if(!sender.hasPermission("core.command.warp.admin")) {
            sender.sendMessage("§cUso: /"+s+" <nombre del warp>");
            return false;
        }
        player.sendMessage("§cUso: /warp <warp:create:delete:movehere>");
        return false;
    }
}
