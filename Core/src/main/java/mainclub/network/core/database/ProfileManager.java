package mainclub.network.core.database;

import mainclub.network.core.Core;
import mainclub.network.core.configuration.file.ProfilesFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProfileManager {
    private final Core main = Core.get();
    private final ProfilesFile database = main.getConfiguration().getProfileFile();
    public final List<Profile> profiles = new ArrayList<>();

    public ProfileManager(){
        load();
    }

    public boolean has(final String offlinePlayerUUID){
        return profiles.stream().anyMatch(filter->filter.getUUID().toString().equals(offlinePlayerUUID));
    }

    public void create(final Player player){
        final Profile profile = new Profile(player.getUniqueId(), player.getName());
        profile.setJoinDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        profile.setJoinNumber(profiles.size() + 1);
        profile.setTimePlayed(0);
        profile.setNick("none");
        profile.setBack(null);

        profile.setKills(0);
        profile.setDeaths(0);
        profile.setStreak(0);
        profile.setMaxStreak(0);

        profile.setJoinTimePlaying(0);
        profile.setDayJoins(profile.getDayJoins()+1);

        profiles.add(profile);
    }


    public Profile get(final String UUID) {
        return profiles.stream().filter(filter->filter.getUUID().toString().equals(UUID)).findFirst().get();
    }
    public Profile get(final UUID UUID) {
        return profiles.stream().filter(filter->filter.getUUID().equals(UUID)).findFirst().orElse(null);
    }

    private void load() {
        final List<String> profileFile = database.getStringList("PROFILES");
        if (profileFile.isEmpty()) return;

        profileFile.forEach(string -> {
            final String[] values = string.split(";");
            final String uuid = values[0];
            final String name = values[1];
            final String nick = values[2];
            final String joinDate = values[3];
            final int timePlayed = Integer.parseInt(values[4]);
            final int joinNumber = Integer.parseInt(values[5]);
            final int kills = Integer.parseInt(values[6]);
            final int deaths = Integer.parseInt(values[7]);
            final int streak = Integer.parseInt(values[8]);
            final int maxStreak = Integer.parseInt(values[9]);
            final String[] location = values[10].split(":");
            final Location back = values[10].equals("null") ? null : new Location(Bukkit.getWorld(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]), Double.parseDouble(location[3]));


            final Profile profile = new Profile(UUID.fromString(uuid), name);
            profile.setJoinDate(joinDate);
            profile.setJoinNumber(joinNumber);
            profile.setTimePlayed(timePlayed);
            profile.setNick(nick);
            profile.setBack(back);
            profile.setKills(kills);
            profile.setDeaths(deaths);
            profile.setStreak(streak);
            profile.setMaxStreak(maxStreak);
            profiles.add(profile);
        });
    }
    public void save() {
        final List<String> update = new ArrayList<>();

        profiles.forEach(profile -> {
            final Location back = profile.getBack();
            update.add(profile.getUUID()+";"+
                    profile.getName()+";"+
                    profile.getNick()+";"+
                    profile.getJoinDate()+";"+
                    profile.getTimePlayed()+";"+
                    profile.getJoinNumber()+";"+
                    profile.getKills()+";"+
                    profile.getDeaths()+";"+
                    profile.getStreak()+";"+
                    profile.getMaxStreak()+";"+
                    (back == null ? "null" : back.getWorld().getName()+":"+back.getX()+":"+back.getY()+":"+back.getZ()));
        });

        database.set("PROFILES", update);
        database.save();
    }
}
