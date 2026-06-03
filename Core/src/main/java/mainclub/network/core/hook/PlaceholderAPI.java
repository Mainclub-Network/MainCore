package mainclub.network.core.hook;

import d2mbo.world.teams.Teams;
import mainclub.network.core.Core;
import mainclub.network.core.database.Profile;
import mainclub.network.core.leaderboards.LeaderboardMaker;
import mainclub.network.core.leaderboards.LeaderboardType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlaceholderAPI extends PlaceholderExpansion {

    public Core plugin;

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getRequiredPlugin() {
        return "MainCore";
    }

    @Override
    public boolean canRegister() {
        return (plugin = (Core) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }

    @Override
    public String getAuthor() {
        return "2MBO";
    }

    @Override
    public String getIdentifier() {
        return "maincore";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";
        Profile profile = plugin.getBase().get(player.getUniqueId());
        if (profile == null) return "";

        if (identifier.equals("user_playing")) return profile.formatTime(profile.getJoinTimePlaying());
        if (identifier.equals("user_played")) return profile.formatTime(profile.getTimePlayed());
        if (identifier.equals("user_kills")) return String.valueOf(profile.getKills());
        if (identifier.equals("user_deaths")) return String.valueOf(profile.getDeaths());
        if (identifier.equals("user_streak")) return String.valueOf(profile.getStreak());
        if (identifier.equals("user_maxstreak")) return String.valueOf(profile.getMaxStreak());
        if (identifier.equals("user_number")) return String.valueOf(profile.getJoinNumber());
        if (identifier.equals("user_nick")) return profile.getNick();
        if (identifier.equals("user_joindate")) return profile.getJoinDate();

        if (identifier.equals("user_rank")) {

            if (player.isOp() || player.hasPermission("group.owner")) return "§4Dueño";
            else if (player.hasPermission("group.coowner")) return "§cC. Owner";
            else if (player.hasPermission("group.headadmin")) return "§cH. Admin";
            else if (player.hasPermission("group.admin")) return "§cAdmin";
            else if (player.hasPermission("group.manager")) return "§dManager";
            else if (player.hasPermission("group.comunnitymanager")) return "§9C. Manager";
            else if (player.hasPermission("group.seo")) return "§dSEO";
            else if (player.hasPermission("group.developer")) return "§bDev";
            else if (player.hasPermission("group.configurator")) return "§9Config";
            else if (player.hasPermission("group.headmod")) return "§3H. Mod";
            else if (player.hasPermission("group.mod")) return "§bMod";
            else if (player.hasPermission("group.trial-mod")) return "§eT. Mod";
            else if (player.hasPermission("group.builder")) return "§2Builder";
            else if (player.hasPermission("group.helper")) return "§9Helper";
            else if (player.hasPermission("group.media")) return "§5Famous";
            else if (player.hasPermission("group.media")) return "§dMedia";
            else if (player.hasPermission("group.mini-media")) return "§dMini Media";
            else if (player.hasPermission("group.legend")) return "§5LEGEND";
            else if (player.hasPermission("group.elite")) return "§bELITE";
            else if (player.hasPermission("group.hero")) return "§fHERO";
            else return "§7Miembro";
        }
        if (identifier.equals("user_rank_color")) {
            if (player.isOp() || player.hasPermission("group.owner")) return "§4";
            else if (player.hasPermission("group.coowner")) return "§c";
            else if (player.hasPermission("group.headadmin")) return "§c";
            else if (player.hasPermission("group.admin")) return "§c";
            else if (player.hasPermission("group.manager")) return "§d";
            else if (player.hasPermission("group.comunnitymanager")) return "§9";
            else if (player.hasPermission("group.seo")) return "§d";
            else if (player.hasPermission("group.developer")) return "§b";
            else if (player.hasPermission("group.configurator")) return "§9";
            else if (player.hasPermission("group.headmod")) return "§3";
            else if (player.hasPermission("group.mod")) return "§b";
            else if (player.hasPermission("group.trial-mod")) return "§e";
            else if (player.hasPermission("group.builder")) return "§2";
            else if (player.hasPermission("group.helper")) return "§9";
            else if (player.hasPermission("group.famous")) return "§5";
            else if (player.hasPermission("group.media")) return "§d";
            else if (player.hasPermission("group.mini-media")) return "§d";
            else if (player.hasPermission("group.legend")) return "§5";
            else if (player.hasPermission("group.elite")) return "§b";
            else if (player.hasPermission("group.hero")) return "§f";
            else return "§7";
        }


        final LeaderboardMaker leaderboard = plugin.getLeaderboards();
        if(identifier.contains("user_position_kills")) {
            return ""+leaderboard.getRanking(LeaderboardType.KILLS, player);
        } else if(identifier.contains("user_position_deaths")) {
            return ""+leaderboard.getRanking(LeaderboardType.DEATHS, player);
        } else if(identifier.contains("user_position_streak")) {
            return ""+leaderboard.getRanking(LeaderboardType.STREAKS, player);
        } else if(identifier.contains("user_position_maxstreak")) {
            return ""+leaderboard.getRanking(LeaderboardType.MAX_STREAKS, player);
        }

        String team = Teams.get().manager().hasTeam(player.getUniqueId()) ? "["+Teams.get().manager().getTeam(player.getUniqueId()).getName()+"] " : "";
        if(identifier.contains("leaderboard_kills_")) {
            final int position = Integer.parseInt(identifier.split("_")[2]);
            final List<Map.Entry<String, Integer>> ranking = leaderboard.getRanking(LeaderboardType.KILLS).entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toList());

            if(ranking.size() < position) return "";
            return ("<ranking>§r. "+team+"<player>: <value>").replace("<ranking>", String.valueOf(position)).replace("<player>", ranking.get(position-1).getKey()).replace("<value>", String.valueOf(ranking.get(position-1).getValue()));
        }
        else if(identifier.contains("leaderboard_deaths_")) {
            final int position = Integer.parseInt(identifier.split("_")[2]);
            final List<Map.Entry<String, Integer>> ranking = leaderboard.getRanking(LeaderboardType.DEATHS).entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toList());

            if(ranking.size() < position) return "";
            return ("<ranking>§r. "+team+"<player>: <value>").replace("<ranking>", String.valueOf(position)).replace("<player>", ranking.get(position-1).getKey()).replace("<value>", String.valueOf(ranking.get(position-1).getValue()));
        }
        else if(identifier.contains("leaderboard_streaks_")) {
            final int position = Integer.parseInt(identifier.split("_")[2]);
            final List<Map.Entry<String, Integer>> ranking = leaderboard.getRanking(LeaderboardType.STREAKS).entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toList());

            if(ranking.size() < position) return "";
            return ("<ranking>§r. "+team+"<player>: <value>").replace("<ranking>", String.valueOf(position)).replace("<player>", ranking.get(position-1).getKey()).replace("<value>", String.valueOf(ranking.get(position-1).getValue()));
        }
        else if(identifier.contains("leaderboard_maxstreaks_")) {
            final int position = Integer.parseInt(identifier.split("_")[2]);
            final List<Map.Entry<String, Integer>> ranking = leaderboard.getRanking(LeaderboardType.MAX_STREAKS).entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toList());

            if(ranking.size() < position) return "";
            return ("<ranking>§r. "+team+"<player>: <value>").replace("<ranking>", String.valueOf(position)).replace("<player>", ranking.get(position-1).getKey()).replace("<value>", String.valueOf(ranking.get(position-1).getValue()));
        }

        return null;
    }
}