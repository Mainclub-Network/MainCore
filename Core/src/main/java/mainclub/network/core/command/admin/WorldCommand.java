package mainclub.network.core.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.world")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        if(strings.length == 0) {
            sender.sendMessage("§cUse: /world <list;teleport>");
            return false;
        }

        if(strings[0].equalsIgnoreCase("list")) {
            StringBuilder worldNames = new StringBuilder();
            for (int i=0; i<Bukkit.getWorlds().size(); i++) {
                worldNames.append(Bukkit.getWorlds().get(i).getName());
                if(i != Bukkit.getWorlds().size()-1) worldNames.append(", ");
            }
            sender.sendMessage("§9Worlds:§f "+ worldNames);
            return false;
        } else if(strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("tp") || strings[0].equalsIgnoreCase("go")) {
            if(Bukkit.getWorld(strings[1]) == null) {
                sender.sendMessage("§cWorld not found.");
                return false;
            }

            ((Player)sender).teleport(Bukkit.getWorld(strings[1]).getSpawnLocation());
            return false;
        }

        sender.sendMessage("§cUse: /world <list;teleport>");
        return false;
    }
}
