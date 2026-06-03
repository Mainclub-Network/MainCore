package mainclub.network.core.leaderboards;

import com.google.common.collect.Iterables;
import mainclub.network.core.Core;
import mainclub.network.core.database.ProfileManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LeaderboardMaker {
    private Core main = Core.get();
    private final ProfileManager database = main.getBase();
    private HashMap<String, Integer>
            kills = new HashMap<>(),
            deaths = new HashMap<>(),
            streaks = new HashMap<>(),
            maxstreaks = new HashMap<>();

    public LeaderboardMaker() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::updater, 0, 15, TimeUnit.MINUTES);
    }


    public void updater() {
        kills.clear();
        deaths.clear();
        streaks.clear();
        maxstreaks.clear();

        if(!database.profiles.isEmpty()) {
            database.profiles.forEach(profile -> {
                kills.put(profile.getName(), profile.getKills());
                deaths.put(profile.getName(), profile.getDeaths());
                streaks.put(profile.getName(), profile.getStreak());
                maxstreaks.put(profile.getName(), profile.getMaxStreak());
            });
        }
    }

    public Map<String, Integer> getRanking(final LeaderboardType type) {
        if(type == LeaderboardType.KILLS) {
            return kills;
        } else if(type == LeaderboardType.DEATHS) {
            return deaths;
        } else if(type == LeaderboardType.STREAKS) {
            return streaks;
        } else if(type == LeaderboardType.MAX_STREAKS) {
            return maxstreaks;
        }
        return null;
    }
    public int getRanking(final LeaderboardType type, final Player player) {
        Map<String, Integer> map = null;

        switch (type) {
            case KILLS:
                map = kills;
                break;
            case DEATHS:
                map = deaths;
                break;
            case STREAKS:
                map = streaks;
                break;
            case MAX_STREAKS:
                map = maxstreaks;
                break;
        }

        final List<Map.Entry<String, Integer>> sorted = map.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toList());
        for(int i = 0; i < sorted.size(); i++) {
            if(sorted.get(i).getKey().equalsIgnoreCase(player.getName())) return i + 1;
        }

        return -1;
    }
}