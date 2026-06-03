package mainclub.network.core.command.donation;

import mainclub.network.core.Core;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReNameCommand implements CommandExecutor {
    private final Core main = Core.get();
    private final int cost = 5000;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("core.command.rename")) {
            sender.sendMessage("§cNo permission.");
            return false;
        } else if(args.length == 0) {
            sender.sendMessage("§cUse: /"+label+" <name>");
            return false;
        }

        final Player player = (Player) sender;
        ItemStack itemHand = main.getVersion().getItemInHand(player);
        ItemMeta itemmeta = itemHand.getItemMeta();

        if(itemHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tenes un item en la mano."));
            return false;
        } else if((itemHand.getType().toString().contains("SPAWNER") || itemHand.getType().toString().contains("CHEST")) && !itemHand.getType().toString().contains("CHESTPLATE")) {
            player.sendMessage("§cNo puedes renombrar este articulo.");
            return false;
        } else if (!player.isOp() && main.getVault().economy().getBalance(player) < cost) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNececistas &e$"+cost+" &cpara cambiar el nombre de un item."));
            return false;
        } else {
            String name = "";
            for (int i = 0; i < args.length; ++i) {
                name = name + args[i] + " ";
            }
            itemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            itemHand.setItemMeta(itemmeta);
            if(!player.isOp()) {
                main.getVault().economy().withdrawPlayer(player, cost);
                player.sendMessage("§aNombre del item cambiado. §7(-$"+cost+")");
            } else player.sendMessage("§aNombre del item cambiado.");
                player.updateInventory();
                return false;
            }

    }
}