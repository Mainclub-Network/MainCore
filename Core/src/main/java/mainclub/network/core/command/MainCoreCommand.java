package mainclub.network.core.command;

import d2mbo.world.api.utils.TextMaker;
import mainclub.network.core.Core;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!sender.hasPermission("core.command.maincore")) {
            final TextComponent message = new TextMaker("&aMainCore v1.0 &edeveloped by ").getText();
            message.addExtra(new TextMaker("&b2MBO&e.").setHover("&6&l2MBO WORLD&6 Link").runLink("https://discord.com/invite/Nh9Tkz7VvJ").getText());

            final Player player = (Player)sender;
            player.spigot().sendMessage(message);
            return false;
        }

        Core.get().getConfiguration().reload();
        sender.sendMessage("§aConfiguration files reloaded!");
        return false;
    }
}
