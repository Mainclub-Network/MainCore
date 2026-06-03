package mainclub.network.core.manager;

import mainclub.network.core.Core;
import mainclub.network.core.configuration.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class HomeManager {
    private Core main = Core.get();
    private final Configuration configuration = main.getConfiguration();

    public List<String> getHomes(final UUID uuid) {
        return configuration.getHomesFile().getStringList("HOMES."+uuid.toString());
    }
    public boolean hasHome(final UUID uuid) {return configuration.getHomesFile().contains("HOMES."+uuid.toString());}
    public boolean hasHome(final UUID uuid, final String homeName) {
        return (hasHome(uuid) && configuration.getHomesFile().getStringList("HOMES."+uuid.toString()).stream().anyMatch(string-> string.toLowerCase().contains(homeName.toLowerCase())));
    }

    public void create(final UUID uuid, final String homeName, final Location location) {
        final List<String> homes = new ArrayList<>();

        if(hasHome(uuid)) homes.addAll(configuration.getHomesFile().getStringList("HOMES."+uuid.toString()));
        homes.add(homeName+";"+location.getWorld().getName()+";"+location.getX()+";"+location.getY()+";"+location.getZ()+";"+location.getYaw()+";"+location.getPitch());

        configuration.getHomesFile().set("HOMES."+uuid.toString(), homes);
        configuration.getHomesFile().save();
        if(main.getCooldown().isCooldown(uuid.toString()+":home:"+homeName.toLowerCase())) main.getCooldown().deleteCooldown(uuid.toString()+":home:"+homeName.toLowerCase());
        main.getCooldown().createCooldown(uuid.toString()+":home:"+homeName.toLowerCase());
    }
    public void delete(final UUID uuid, final String homeName) {
        final List<String> homes = configuration.getHomesFile().getStringList("HOMES."+uuid.toString());
        homes.removeIf(filter -> filter.toLowerCase().contains(homeName.toLowerCase()));

        if(homes.isEmpty()) configuration.getHomesFile().set("HOMES."+uuid.toString(), null);
        else configuration.getHomesFile().set("HOMES."+uuid.toString(), homes);
        configuration.getHomesFile().save();

        main.getCooldown().deleteCooldown(uuid.toString()+":home:"+homeName.toLowerCase());
    }

    public Location location(final UUID uuid, final String homeName) {
        for(final String value : configuration.getHomesFile().getStringList("HOMES."+uuid.toString())) {
            if(value.toLowerCase().contains(homeName.toLowerCase())) {
                final String[] locationValues = value.split(";");
                final Location finalLocation = new Location(Bukkit.getWorld(locationValues[1]), Double.valueOf(locationValues[2]), Double.valueOf(locationValues[3]), Double.valueOf(locationValues[4]), Float.valueOf(locationValues[5]), Float.valueOf(locationValues[6]));

                return finalLocation;
            }
        }
        return null;
    }

    public void load(final UUID uuid) {
        if(hasHome(uuid)) {
            configuration.getHomesFile().getStringList("HOMES."+uuid.toString()).stream().filter(string ->  main.getCooldown().hasCooldown(uuid+":home:"+string.split(";")[0].toLowerCase())).forEach(string -> main.getCooldown().createCooldown(uuid+":home:"+string.split(";")[0].toLowerCase()));
        }
    }
    public void unload(final UUID uuid) {
        if(hasHome(uuid)) {
            configuration.getHomesFile().getStringList("HOMES."+uuid.toString()).forEach(string -> main.getCooldown().deleteCooldown(uuid+":home:"+string.split(";")[0].toLowerCase()));
        }
    }
    public String getHomeList(final UUID uuid) {
        AtomicReference<String> list = new AtomicReference<>("§9Homes: §f");
        configuration.getHomesFile().getStringList("HOMES."+uuid.toString()).forEach(string -> list.set(list.get()+string.split(";")[0]+", "));
        return list.get();
    }
}
