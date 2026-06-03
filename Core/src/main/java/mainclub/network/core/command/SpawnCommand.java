package mainclub.network.core.command;

import mainclub.network.core.Core;
import mainclub.network.core.manager.SpawnManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
    private final Core main = Core.get();
    private final SpawnManager manager = main.getSpawn();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission("core.command.spawn")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        final Player player = (Player) sender;
            if (manager.hasSpawn()) {
                if (main.getCooldown().isOnCooldown("spawn", player.getName())) {
                    player.sendMessage("§cYa estás siendo teletransportado al Spawn.");
                    return false;
                } else if (main.getCooldown().isOnCooldown("warps", player.getName())) {
                    player.sendMessage("§cEstás siendo teletransportado a un warp.");
                    return false;
                }

                if (player.isOp() || player.hasPermission("core.bypassed.cooldown")) {
                    player.teleport(manager.location());
                    player.sendMessage("§2§lTRANSPORTADO§2!");
                    return false;
                } else {
                    if(!main.getCooldown().isCooldown("spawn")) main.getCooldown().createCooldown("spawn");

                    final int time = (player.hasPermission("group.hero") || player.hasPermission("group.mini-media")) ? 3 : player.hasPermission("group.elite") ? 2 : player.hasPermission("group.legend") ? 1 : 5;
                    main.getCooldown().addCooldown("spawn", player.getName(), time);
                    player.sendMessage("§fApareceras en el §9Spawn§f en §b" + time + " seg§f...");
                    return false;
                }
            }
            player.sendMessage("§cSpawn not found." + (player.isOp() ? " §7(/spawn set)" : ""));
            return false;

    }
}
