package mainclub.network.core.command.teleport;

import mainclub.network.core.Core;
import mainclub.network.core.manager.CoreManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AcceptCommand implements CommandExecutor {
    private CoreManager manager = Core.get().getManager();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        final Player player  = (Player) sender;
        if(!manager.hasTPARequest(player.getUniqueId())) {
            sender.sendMessage("No tenes solicitudes de teletransportacion.");
            return false;
        }

        final Player target = Bukkit.getPlayer(manager.tpaRequest().get(player.getUniqueId()).keySet().stream().findAny().get());
        if(target == null || !target.isOnline()) {
            sender.sendMessage(target.getName()+" ya no está conectado.");
            manager.tpaRequest().remove(player.getUniqueId());
            return false;
        }

        player.sendMessage("Solicitud de teletransporte aceptada.");
        target.sendMessage("Solicitud aceptada, teletransportado!");
        if(manager.tpaRequest().get(player.getUniqueId()).get(target.getUniqueId())) player.teleport(target.getLocation());
        else target.teleport(player);
        manager.tpaRequest().remove(player.getUniqueId());
        return false;
    }
}
