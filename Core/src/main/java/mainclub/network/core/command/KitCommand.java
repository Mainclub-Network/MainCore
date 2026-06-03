package mainclub.network.core.command;

import mainclub.network.core.Core;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KitCommand implements CommandExecutor {
    private final Core main = Core.get();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) return false;
        final Player player = (Player)sender;

        if (strings.length == 0) {
            final String kitsList = main.getKits().getKitsList(player);
            if(kitsList.equals("")) {

                sender.sendMessage("§cKit not found.");
                return false;
            }
            sender.sendMessage("§b§lKITS§b: §f"+kitsList);
            return false;
        }

        if(strings[0].equalsIgnoreCase("list")) {
            final String kitsList = main.getKits().getKitsList(player);
            if(kitsList.equals("")) {
                sender.sendMessage("§cKit not found.");
                return false;
            }
            sender.sendMessage("§b§lKITS§b: §f"+kitsList);
            return false;
        } else if(strings[0].equalsIgnoreCase("view")) {
            if(strings.length == 1) {
                sender.sendMessage("§cUse: /kit view <kit>");
                return false;
            }

            if(main.getKits().isKit(strings[1])) {
                main.getKits().viewKit(player, main.getKits().equalsIgnoreCaseKit(strings[1]));
                return false;
            }
            sender.sendMessage("§fKit unknown.");
            return false;
        } else if (strings.length == 1 && main.getKits().isKit(strings[0])) {
            if (!sender.hasPermission("core.kit." + main.getKits().equalsIgnoreCaseKit(strings[0]))) {
                sender.sendMessage("§cAcceso al kit " + main.getKits().equalsIgnoreCaseKit(strings[0]) + " denegado.");
                return false;
            } else if (strings.length < 1 || strings.length > 1) {
                sender.sendMessage("§cUse: /kit <kit>");
                return false;
            } else if (main.getKits().isCooldownKit((Player) sender, main.getKits().equalsIgnoreCaseKit(strings[0])) && !player.isOp()) {
                sender.sendMessage("§cEspera, §e" + main.getKits().getPlayerCooldownKit(player, main.getKits().equalsIgnoreCaseKit(strings[0])) + " §cpara reclamar el kit.");
                return false;
            }

            if (!sender.isOp()) main.getKits().addCooldownKit(player, main.getKits().equalsIgnoreCaseKit(strings[0]));

            main.getKits().sendPlayerKit(player, main.getKits().equalsIgnoreCaseKit(strings[0]));
            sender.sendMessage("§eKit §9" + main.getKits().equalsIgnoreCaseKit(strings[0]) + "§e reclamado.");
            return false;
        }

        if (sender.hasPermission("core.manage.kit")) {
            if (strings[0].equalsIgnoreCase("create")) {
                if (strings.length < 3 || strings.length > 3) {
                    sender.sendMessage("§cUse: /kit create <name> <minutes>");
                    return false;
                } else if (main.getKits().isKit(strings[1])) {
                    sender.sendMessage("§cYa hay un kit creado con ese nombre.");
                    return false;
                } else if (!StringUtils.isNumeric(strings[2])) {
                    sender.sendMessage("§cEl cooldown del kit debe ser un número.");
                    return false;
                }

                main.getKits().createKit(strings[1], Integer.valueOf(strings[2])*60, ((Player) sender).getInventory());
                sender.sendMessage("§eKit §9" + strings[1] + "§e creado.");
                return false;
            } else if (strings[0].equalsIgnoreCase("delete")) {
                if (strings.length < 2 || strings.length > 2) {
                    sender.sendMessage("§cUse: /kit delete <name>");
                    return false;
                } else if (!main.getKits().isKit(main.getKits().equalsIgnoreCaseKit(strings[1]))) {
                    sender.sendMessage("§cNo hay un kit creado con ese nombre.");
                    return false;
                }

                sender.sendMessage("§eKit §c" + main.getKits().equalsIgnoreCaseKit(strings[1]) + "§e borrado.");
                main.getKits().deleteKit(main.getKits().equalsIgnoreCaseKit(strings[1]));
                return false;
            }
        }


        sender.sendMessage("§cUse: /kit"+(player.isOp() ? " <create:delete:view>": ""));
        return false;
    }
}
