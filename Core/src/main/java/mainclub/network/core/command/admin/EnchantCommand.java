package mainclub.network.core.command.admin;

import mainclub.network.core.Core;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantCommand implements CommandExecutor {
    final Core main = Core.get();
    final HashMap<Enchantment, List<String>> enchants = main.getVersion().getEnchantNames();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.enchant")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        final Player player = (Player) sender;
        if(main.getVersion().getItemInHand(player).getType() == Material.AIR) {
            sender.sendMessage("Not found item in main hand.");
            return false;
        }

        if(strings.length == 0) {
            sender.sendMessage("Use: /" + s + " <enchant> <level>");
            return false;

        } else if(strings.length == 1) {
            for (Map.Entry<Enchantment, List<String>> enchant : enchants.entrySet()) {
                if(enchant.getValue().contains(strings[0].toLowerCase())) {
                    sender.sendMessage("Use: /" + s + " "+strings[0]+" <level>");
                    return false;
                }
            }
            sender.sendMessage("Enchant "+ strings[0]+" not found.");
            return false;
        }

        for (Map.Entry<Enchantment, List<String>> enchant : enchants.entrySet()) {
            if(enchant.getValue().contains(strings[0].toLowerCase())) {
                if(!StringUtils.isNumeric(strings[1])) {
                    sender.sendMessage("Level "+strings[1]+" not found.");
                    return false;
                }

                ItemStack enchantedItem = main.getVersion().getItemInHand(player);
                enchantedItem.addUnsafeEnchantment(enchant.getKey(), Integer.parseInt(strings[1]));

                main.getVersion().setPlayerHand(player, enchantedItem);
                player.updateInventory();
                sender.sendMessage("Enchanted "+ strings[0]+" -> "+strings[1]);
                return false;
            }
        }

        try {

            return false;
        } catch (Exception e) {}

        sender.sendMessage("Use: /" + s + " <enchant> <level>");
        return false;
    }
}
