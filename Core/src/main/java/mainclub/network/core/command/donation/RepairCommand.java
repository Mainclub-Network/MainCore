package mainclub.network.core.command.donation;

import mainclub.network.core.Core;
import mainclub.network.core.utils.Cooldown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class RepairCommand implements CommandExecutor {
    private final Core main = Core.get();
    private final Cooldown cooldown = main.getCooldown();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.repair")) {
            sender.sendMessage("Comando denegado.");
            return false;
        }

        final Player player = (Player) sender;
        if(strings.length == 0) {
            final ItemStack hand = player.getItemInHand();
            if(hand == null) {
                sender.sendMessage("§cNecesitas un item en la mano.");
                return false;
            } else if (cooldown.isOnCooldown("repair", player.getName())) {
                sender.sendMessage("§cEspera §e"+cooldown.toFormat("repair", player.getName())+" §cpara volver a reparar tu inventario.");
                return false;
            } else if (main.getVault().economy().getBalance(player) < 10000) {
                sender.sendMessage("§cNecesitas §e$10K§c para reparar el item.");
                return false;
            }
            hand.setDurability((short)0);
            player.updateInventory();
            cooldown.addCooldown("repair", player.getName(), 60*1);

            sender.sendMessage("§aITEM REPARADO!");
            return false;
        }

        if(strings.length == 1 && (strings[0].equalsIgnoreCase("all")) || strings[0].equalsIgnoreCase("todo")) {
            if(!sender.hasPermission("core.command.repair.all")) {
                sender.sendMessage("Comando denegado.");
                return false;
            } else if (cooldown.isOnCooldown("repair_all", player.getName())) {
                sender.sendMessage("§cEspera §e"+cooldown.toFormat("repair_all", player.getName())+" §cpara volver a reparar tu inventario.");
                return false;
            } else if (main.getVault().economy().getBalance(player) < 50000) {
                sender.sendMessage("§cNecesitas §e$50K§c para reparar el item.");
                return false;
            }

                player.getInventory().forEach(itemStack -> {
                    if(itemStack != null && (!itemStack.getType().toString().contains("SKULL") && !itemStack.getType().toString().contains("HEAD")) && itemStack.hasItemMeta() && itemStack.getDurability() > 0) {
                        itemStack.setDurability((short)0);
                    }
                });
                cooldown.addCooldown("repair_all", player.getName(), 60*5);

            sender.sendMessage("§aITEMS REPARADOS!");
            return false;
        }
        return false;
    }
}
