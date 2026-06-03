package mainclub.network.core.listener;

import mainclub.network.core.Core;
import mainclub.network.core.utils.Moderation;
import mainclub.network.core.utils.event.list.CooldownExpiredEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CoreListener implements Listener {
    private Core clubCore = Core.get();
    private Moderation moderation = clubCore.getModeration();
    //private String chat_format = clubCore.getConfiguration().getStrings().getChatFormat();
    //private Cooldown cooldown = clubCore.cooldowns();

    @EventHandler (priority = EventPriority.HIGH)
    public void chat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        if(moderation.isFrozen(player.getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§4§l[SS] §b%1$s§f: %2$s");
            moderation.broadcastToStaff("§4§l[SS] §b%1$s§f: %2$s");
            return;
        } else if(moderation.isMuteChat() && !player.hasPermission("core.mutechat.bypass")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cChat muteado");
            return;
        }


        String groupPrefix = clubCore.getVault().chat().getGroupPrefix(player.getWorld(), clubCore.getVault().chat().getPrimaryGroup(player));
        String groupSuffix = clubCore.getVault().chat().getGroupSuffix(player.getWorld(), clubCore.getVault().chat().getPrimaryGroup(player));
        boolean groupPrefixPlus = groupPrefix.contains("&l");

        String format = PlaceholderAPI.setPlaceholders(player, clubCore.getConfiguration().getStrings().getChatFormat())
                .replace("<player>", "%1$s")
                .replace("<message>", "%2$s")
                .replace("<vault_group_prefix>", groupPrefix)
                .replace("<vault_group_suffix>", groupSuffix)
                .replace("<vault_group_color>", groupPrefix.substring(groupPrefix.length() -2))
                .replace("<vault_group_color+>", groupPrefix.substring(groupPrefix.length() -2) + (groupPrefixPlus ? "§l" : ""));
        event.setFormat(clubCore.getVersion().color(format));


    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        if(moderation.isFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            moderation.broadcastToStaff("§4§l[SS] §3"+event.getPlayer().getName()+" §buso §7("+event.getMessage()+")§b.");
            return;
        }
        /*if(event.getMessage().startsWith("/")) {
            final String command = event.getMessage().toLowerCase().split(" ")[0] == null ? event.getMessage().toLowerCase() : event.getMessage().toLowerCase().split(" ")[0]);

            if(command.equalsIgnoreCase("/msg") ||
                    command.equalsIgnoreCase("/msg") ||)
        }*/
        if(event.getMessage().startsWith("/") && !event.getPlayer().isOp()) {
            if (event.getMessage().startsWith("/menu") || event.getMessage().startsWith("/msg") || event.getMessage().startsWith("/message") || event.getMessage().startsWith("/r") || event.getMessage().startsWith("/reply")) return;

            final List<String> commands = Arrays.asList("plugins", "pl", "bukkit", "spigot", "minecraft", "about", "version", "ver", "worldedit", "?", "me", "help", "tell");
            for(final String command : commands) {
                if(event.getMessage().toLowerCase().split(" ")[0].contains(command)) {
                    if((command.toLowerCase().equals("me") && !event.getMessage().toLowerCase().startsWith("/me")) ||
                            (command.toLowerCase().equals("pl") && !event.getMessage().toLowerCase().equals("/pl"))  ||
                            (command.toLowerCase().equals("menu") && !event.getMessage().toLowerCase().equals("/menu"))) return;
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§cNo permission.");
                    return;
                }
            }
        }
    }

    @EventHandler
    public void expired(final CooldownExpiredEvent event) {
        if(event.getName().equalsIgnoreCase("back")) {
            final Location location = clubCore.getBase().get(event.getPlayer().get().getUniqueId().toString()).getBack();
            if(location.getWorld().getName().equalsIgnoreCase("builds")) return;
            event.getPlayer().get().teleport(location);
            event.getPlayer().get().sendMessage("§e§lREGRESADO§e!");
            return;
        }
        if(event.getName().equalsIgnoreCase("warp")) {
            event.getPlayer().get().teleport(clubCore.getWarps().location(clubCore.getWarps().teleporting().get(event.getPlayer().get().getName())));
            clubCore.getWarps().teleporting().remove(event.getPlayer().get().getName());
            event.getPlayer().get().sendMessage("§a§lTRANSPORTADO§a!");
            return;
        }
        if(event.getName().equalsIgnoreCase("spawn")) {
            event.getPlayer().get().teleport(clubCore.getSpawn().location());
            event.getPlayer().get().sendMessage("§2§lTRANSPORTADO§2!");
            return;
        }
        if(event.getName().contains(":home:")) {
            final UUID uuid = UUID.fromString(event.getName().split(":")[0]);
            if(!Bukkit.getPlayer(uuid).isOnline()) return;

            event.getPlayer().get().teleport(clubCore.getHomes().location(uuid, event.getName().split(":")[2]));
            event.getPlayer().get().sendMessage("§9§lTRANSPORTADO§9!");
            return;
        }
        if(clubCore.getKits().isKit(event.getName())) event.getPlayer().get().sendMessage("§aYa podes reclamar de nuevo tú kit §b"+event.getName()+"§a.");

    }

}
