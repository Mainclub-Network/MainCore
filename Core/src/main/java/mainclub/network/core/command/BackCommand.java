package mainclub.network.core.command;

import mainclub.network.core.Core;
import mainclub.network.core.database.Profile;
import mainclub.network.core.utils.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BackCommand implements CommandExecutor {
    private final Core main = Core.get();
    private final Cooldown cooldown = main.getCooldown();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final Profile profile = main.getBase().get(Bukkit.getPlayer(sender.getName()).getUniqueId().toString());

        if (profile.getBack() == null) {
            sender.sendMessage("§cNo hay ubicación anterior.");
            return false;
        }

        if (cooldown.isOnCooldown("back", sender.getName())) {
            sender.sendMessage("§cYa estás siendo transportado!");
            return false;
        }
        final int time = (sender.hasPermission("group.hero") || sender.hasPermission("group.mini-media"))? 3 : sender.hasPermission("group.elite") ? 2 : sender.hasPermission("group.legend") ? 1 : 5;
        cooldown.addCooldown("back", sender.getName(), time);
        sender.sendMessage("§fVolveras  a tu ubicación §danterior§f en §b"+time+" seg§f...");
        return false;
    }
}