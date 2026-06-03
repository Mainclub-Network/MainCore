package mainclub.network.core.command.admin;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemLoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.itemlore")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        if(strings.length == 0) {
            sender.sendMessage("Use: /itemlore <set:add:remove>");
            return false;
        }

        final Player player = (Player) sender;
        if(strings[0].equalsIgnoreCase("set")) {
            if(strings.length == 1) {
                sender.sendMessage("Use: /itemlore set <int> <text>");
                return false;
            } else if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
                sender.sendMessage("No item in your main-hand.");
                return false;
            } else if (strings.length == 2) {
                if(!StringUtils.isNumeric(strings[1])) {
                    sender.sendMessage("int(line) incorrect.");
                    return false;
                }

                sender.sendMessage("Use: /itemlore set "+strings[1]+" <text>");
                return false;
            }

            ItemMeta meta = player.getItemInHand().getItemMeta();
            List<String> lore = new ArrayList<>();
            if(meta.hasLore()) lore = meta.getLore();

            lore.set(Integer.parseInt(strings[1]), ChatColor.translateAlternateColorCodes('&', strings[2]));
            meta.setLore(lore);

            player.getItemInHand().setItemMeta(meta);
            player.updateInventory();
            sender.sendMessage("lore line setted!");
            return false;
        }
        else if(strings[0].equalsIgnoreCase("add")) {
            if(strings.length == 1) {
                sender.sendMessage("Use: /itemlore add <text>");
                return false;
            } else if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
                sender.sendMessage("No item in your main-hand.");
                return false;
            }

            String text = strings[1];
            for (int i=2; i<strings.length; i++) {
                text += " "+strings[i];
            }

            ItemMeta meta = player.getItemInHand().getItemMeta();
            List<String> lore = new ArrayList<>();
            if(meta.hasLore()) lore = meta.getLore();

            lore.add(ChatColor.translateAlternateColorCodes('&', text));
            meta.setLore(lore);

            player.getItemInHand().setItemMeta(meta);
            player.updateInventory();
            sender.sendMessage("lore added!");
            return false;
        } else if(strings[0].equalsIgnoreCase("remove")) {
            if(strings.length == 1) {
                sender.sendMessage("Use: /itemlore remove <int>");
                return false;
            }

            return false;
        }

        sender.sendMessage("Use: /itemlore <add:remove>");
        return false;
    }
}
