package mainclub.network.core.command.admin.claim;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.regions.Region;
import mainclub.network.core.Core;
import mainclub.network.core.manager.ClaimManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ClaimCommand implements CommandExecutor {
    private final Core main = Core.get();
    private final ClaimManager claims = main.getClaims();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission("core.command.claim")) {
            sender.sendMessage("§cNo permission.");
            return false;
        } else if(strings.length == 0) {
           // sender.sendMessage("Claim: "+claims.get(((Player)sender).getLocation().getBlock()).getName());
            sender.sendMessage("§cUse: /claim <list;info;tp;create;delete;setspawn;setpvp;setSafezone;quitteleport;...>");
            return false;
        }

        final Player player = (Player) sender;
        if(strings[0].equalsIgnoreCase("list")) {
            sender.sendMessage("§eClaims§7[§f"+ claims.size()+"§7]§f: "+claims.list());
            return false;
        }
        else if (strings[0].equalsIgnoreCase("create")) {
            if (strings.length == 1) {
                sender.sendMessage("§cUse: /claim create <claim>");
                return false;
            }
            else if (claims.has(strings[1])) {
                player.sendMessage("§cClaim already exist.");
                return false;
            }

            try {
                claims.create(strings[1], main.getVersion().getFirstWorldEditSelection(player), main.getVersion().getLastWorldEditSelection(player));
            } catch (Exception e) {
                sender.sendMessage("§cSelect the region with WorldEdit Axe.");
                return false;
            }

            player.sendMessage("§aClaim §e" + strings[1] + " §acreated.");

            return false;
        }
        else if (strings[0].equalsIgnoreCase("info")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim info <claim>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            }

            player.sendMessage("§b§lViewing §3§l"+claims.get(strings[1]).getName());
            player.sendMessage(" - Priority: §7"+claims.get(strings[1]).getPriority());
            player.sendMessage(" - SafeZone: §7"+claims.get(strings[1]).isSegureClaim());
            player.sendMessage(" - QuitTeleport: §7"+claims.get(strings[1]).isQuitTeleport());
            player.sendMessage(" - PvP: §7"+claims.get(strings[1]).isPvp());
            player.sendMessage(" - JoinPermission: §7"+claims.get(strings[1]).getJoinPermission());
            player.sendMessage(" - JoinCommands: §7"+Arrays.asList(claims.get(strings[1]).getJoinCommands()));
            player.sendMessage(" - QuitCommands: §7"+ Arrays.asList(claims.get(strings[1]).getQuitCommands()));
            player.sendMessage(" - CancelCommands: §7"+ Arrays.asList(claims.get(strings[1]).getCancelCommands()));
            return false;
        }
        else if (strings[0].equalsIgnoreCase("setspawn")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim setSpawn <claim>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            }

            claims.get(strings[1]).setSpawn(player.getLocation());
            player.sendMessage("§aClaim §e" + strings[1] + " §aspawn setted.");
            return false;
        }
        else if (strings[0].equalsIgnoreCase("tp")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim tp <claim>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            }

            player.teleport(claims.get(strings[1]).getSpawn());
            return false;
        }
        else if (strings[0].equalsIgnoreCase("quitTeleport")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim quitTeleport <claim>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            }

            boolean toggled = claims.get(strings[1]).isQuitTeleport();
            claims.get(strings[1]).setQuitTeleport(!toggled);

            player.sendMessage("§aClaim §e" + strings[1] + " §aquitTeleport is now "+!toggled);
            return false;
        }
        else if (strings[0].equalsIgnoreCase("setsafezone")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim setsafezone <claim>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            }

            boolean toggled = claims.get(strings[1]).isSegureClaim();
            claims.get(strings[1]).setSegureClaim(!toggled);

            player.sendMessage("§aClaim §e" + strings[1] + " §asafezone is now "+!toggled);
            return false;
        }
        else if (strings[0].equalsIgnoreCase("setpvp")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim pvp <claim>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            }
            boolean toggled = claims.get(strings[1]).isPvp();
            claims.get(strings[1]).setPvp(!toggled);

            player.sendMessage("§aClaim §e" + strings[1] + " §apvp is now "+!toggled);
            return false;
        }
        else if (strings[0].equalsIgnoreCase("setpriority")) {
            if (strings.length <= 2) {
                player.sendMessage("§cUse: /claim setpriority <claim> <integer>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            } else if(!StringUtils.isNumeric(strings[2])) {
                player.sendMessage("§cInteger not valid");
                return false;
            }

            claims.get(strings[1]).setPriority(Integer.valueOf(strings[2]));
            player.sendMessage("§aClaim §e" + strings[1] + " §apriority is now "+strings[2]);
            return false;
        }
        else if (strings[0].equalsIgnoreCase("addcommand")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim addcommand <claim> <command>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            } else if (strings.length < 3) {
                player.sendMessage("§cUse: /claim addcommand "+strings[1]+" <command>");
                return false;
            }

            String newCommand = "";
            for(int index= 2; index<strings.length; index++) {
                newCommand += newCommand.equals("") ? strings[index] : " "+strings[index];
            }

            String[] currents = claims.get(strings[1]).getJoinCommands();
            String[] commands = Arrays.copyOf(currents, currents.length+1);
            commands[commands.length-1] = newCommand;

            claims.get(strings[1]).setJoinCommands(commands);
            player.sendMessage("§aClaim §e'" + newCommand + "' §aadd command "+strings[1]);
            System.out.println(Arrays.toString(claims.get(strings[1]).getJoinCommands()));
            return false;
        }
        else if (strings[0].equalsIgnoreCase("addcancelcommand")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim addcancelcommand <claim> <command>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            } else if (strings.length < 3) {
                player.sendMessage("§cUse: /claim addcalcenlcommand "+strings[1]+" <command>");
                return false;
            }

            String newCancelCommand = "";
            for(int index= 2; index<strings.length; index++) {
                newCancelCommand += newCancelCommand.equals("") ? strings[index] : " "+strings[index];
            }

            String[] currents = claims.get(strings[1]).getCancelCommands();
            String[] commands = Arrays.copyOf(currents, currents.length+1);
            commands[commands.length-1] = newCancelCommand;

            claims.get(strings[1]).setCancelCommands(commands);
            player.sendMessage("§aClaim §e'" + newCancelCommand + "' §aadd cancel command "+strings[1]);
            return false;
        }
        else if (strings[0].equalsIgnoreCase("removecommand")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim removecommand <claim>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            } else if (strings.length < 3) {
                player.sendMessage("§cUse: /claim removecommand "+strings[1]+" <command>");
                return false;
            }

            String newCommand = "";
            for(int index=strings.length; index>2; index++) {
                newCommand += strings[index];
            }

            String[] commands = Arrays.copyOf(claims.get(strings[1]).getQuitCommands(), claims.get(strings[1]).getQuitCommands().length+1);
            commands[commands.length-1] = newCommand;

            claims.get(strings[1]).setQuitCommands(commands);
            player.sendMessage("§aClaim §e" + strings[1] + " §aremove command "+strings[1]);
            return false;
        }
        else if (strings[0].equalsIgnoreCase("joinpermission")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim joinPermission <perm>");
                return false;
            } else if (!claims.has(strings[1])) {
                player.sendMessage("§cClaim not exist.");
                return false;
            }

            claims.get(strings[1]).setJoinPermission(strings[2]);
            player.sendMessage("§aClaim §e" + strings[1] + " §ajoin permission: "+strings[2]);
            return false;

        }
        else if (strings[0].equalsIgnoreCase("delete") || strings[0].equalsIgnoreCase("remove")) {
            if (strings.length == 1) {
                player.sendMessage("§cUse: /claim " + strings[0] + " <claim>");
                return false;
            } else if (!claims.has(strings[1])) {
                sender.sendMessage("§cThe claim not exist.");
                return false;
            }

            claims.delete(strings[1]);claims.delete(strings[1]);
            player.sendMessage("§cClaim §e" + strings[1] + " §cdeleted.");
            return false;
        }

        sender.sendMessage("§cUse: /claim <list;info;tp;create;delete;setspawn;setpvp;setSafezone;quitteleport;...>");
        return false;
    }
}