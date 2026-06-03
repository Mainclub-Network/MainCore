package mainclub.network.core.command.admin;

import mainclub.network.core.Core;
import mainclub.network.core.configuration.Configuration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WhitelistCommand implements CommandExecutor {
    private final Configuration configuration = Core.get().getConfiguration();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.whitelist")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        if(strings.length == 0) {
            sender.sendMessage("§cUse: /whitelist <on:off:add:remove:clear>");
            return false;
        }

        if(strings[0].equalsIgnoreCase("on")) {
            configuration.getBooleans().setWhitelist(true);
            sender.sendMessage("§cWhitelist enabled.");
            return false;
        } else if(strings[0].equalsIgnoreCase("off")) {
            configuration.getBooleans().setWhitelist(false);
            sender.sendMessage("§aWhitelist disabled.");
            return false;
        } else if(strings[0].equalsIgnoreCase("add")) {
            if(strings.length == 1) {
                sender.sendMessage("§cUse: /whitelist add <player>");
                return false;
            } else if(configuration.getStringLists().getWhitelist().contains(strings[1])) {
                sender.sendMessage("§c"+strings[1]+ " already contains on whitelist.");
                return false;
            }

            configuration.getStringLists().getWhitelist().add(strings[1]);
            sender.sendMessage("§a"+strings[1]+" added to whitelist.");
            return false;
        } else if(strings[0].equalsIgnoreCase("remove")) {
            if(strings.length == 1) {
                sender.sendMessage("§cUse: /whitelist remove <player>");
                return false;
            } else if(!configuration.getStringLists().getWhitelist().contains(strings[1])) {
                sender.sendMessage("§c"+strings[1]+ " not contains on whitelist.");
                return false;
            }

            configuration.getStringLists().getWhitelist().remove(strings[1]);
            sender.sendMessage("§c"+strings[1]+" removed from the whitelist.");
            return false;
        } else if(strings[0].equalsIgnoreCase("clear")) {
            configuration.getStringLists().getWhitelist().clear();
            sender.sendMessage("§aWhitelist cleared.");
            return false;
        }

        sender.sendMessage("§cUse: /whitelist <on:off:add:remove:clear>");
        return false;
    }
}
