package mainclub.network.core.command.donation;

import mainclub.network.core.Core;
import mainclub.network.core.hook.VaultAPI;
import mainclub.network.core.database.ProfileManager;
import mainclub.network.core.database.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CommandExecutor {
    private Core main = Core.get();
    private ProfileManager database = main.getBase();
    private VaultAPI vault = main.getVault();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!sender.hasPermission("core.command.nick")) {
            sender.sendMessage("§cNo permission.");
            return false;
        }

        if(strings.length == 0) {
            sender.sendMessage("§cUse: /nick <name>");
            return false;
        } else if(strings.length > 1) {
            sender.sendMessage("§cUse: /nick <name>");
            return false;
        }

        final String customName = strings[0];
        if(customName.length() < 6) {
            sender.sendMessage("§cNick muy corto.");
            return false;
        } else if(customName.length() > 16) {
            sender.sendMessage("§cNick muy largo.");
            return false;
        }

        final Player player = (Player)sender;


        final Profile profile = database.get(((Player) sender).getUniqueId().toString());
        profile.setNick(
                player.hasPermission("core.nick.colors") ? ChatColor.translateAlternateColorCodes('&',
                        (!player.hasPermission("core.nick.colors.special") ? customName.replace("&l", "").replace("&k", "").replace("&n", "").replace("&o", "") : customName))
                        : customName);
        player.sendMessage("§aNick changed.");
        return false;
    }
}
