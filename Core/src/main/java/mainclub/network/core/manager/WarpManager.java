package mainclub.network.core.manager;

import mainclub.network.core.Core;
import mainclub.network.core.configuration.Configuration;
import mainclub.network.core.utils.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class WarpManager {
    private final Core main = Core.get();
    private final Configuration configuration = main.getConfiguration();
    private final Cooldown cooldown = main.getCooldown();
    private HashMap<String, String> teleporting = new HashMap<>();
    public HashMap<String, Location> warps = new HashMap<>();

    public WarpManager() {
        if(!configuration.getLocationsFile().contains("LOCATIONS.WARPS") || configuration.getLocationsFile().getStringList("LOCATIONS.WARPS").isEmpty()) return;

        for(String warp : configuration.getLocationsFile().getStringList("LOCATIONS.WARPS")) {
            final String warpName = warp.split(";")[0];
            final String[] locationValues = warp.split(";");
            final Location warpLocation = new Location(Bukkit.getWorld(locationValues[1]), Double.valueOf(locationValues[2]), Double.valueOf(locationValues[3]), Double.valueOf(locationValues[4]), Float.valueOf(locationValues[5]), Float.valueOf(locationValues[6]));

            warps.put(warpName, warpLocation);
        }

        cooldown.createCooldown("warps");
    }

    public void create(final String warpName, final Location location) {
        final List<String> update = configuration.getLocationsFile().getStringList("LOCATIONS.WARPS");
        update.add(warpName+";"+location.getWorld().getName()+";"+location.getX()+";"+location.getY()+";"+location.getZ()+";"+location.getYaw()+";"+location.getPitch());

        configuration.getLocationsFile().set("LOCATIONS.WARPS", update);
        configuration.getLocationsFile().save();

        warps.put(warpName, location);
        if(!cooldown.isCooldown("warps")) cooldown.createCooldown("warps");
    }
    public void moveLocation(final String warpName, final Location location) {
        final List<String> update = new ArrayList<>();
        for (String warp : configuration.getLocationsFile().getStringList("LOCATIONS.WARPS")) {
            if(warp.equals(warpName)) warp = warpName+";"+location.getWorld().getName()+";"+location.getX()+";"+location.getY()+";"+location.getZ()+";"+location.getYaw()+";"+location.getPitch();
            update.add(warp);
        }

        configuration.getLocationsFile().set("LOCATIONS.WARPS", update);
        configuration.getLocationsFile().save();

        warps.replace(warpName, location);
    }
    public void delete(final String warpName) {
        if(!warps.containsKey(warpName)) return;
        warps.remove(warpName);

        final List<String> update = new ArrayList<>();
        warps.forEach((key, location) -> update.add(key+";"+location.getWorld().getName()+";"+location.getX()+";"+location.getY()+";"+location.getZ()+";"+location.getYaw()+";"+location.getPitch()));
        configuration.getLocationsFile().set("LOCATIONS.WARPS", update);
        configuration.getLocationsFile().save();
    }
    public Location location(final String warpName) {
        AtomicReference<Location> finalLocation = new AtomicReference<>();
        warps.forEach((name, location) -> {
            if(name.equals(warpName)) finalLocation.set(location);
        });
        return finalLocation.get();
    }
    public boolean has(final String warpName) {
        final List<String> copyWarps = new ArrayList<>();
        warps.keySet().forEach(warp-> copyWarps.add(warp.toLowerCase()));
        return copyWarps.contains(warpName.toLowerCase());
    }

    public HashMap<String, String> teleporting() {return teleporting;}
    public String getWarpsList(final Player player) {

        String kits = "";
        for(String warp : warps.keySet()) {
            final String warpToColor = player.hasPermission("core.warp."+warp) ? "§a"+warp+"§f" : "§c"+warp+"§f";
            kits += (kits.equals("") ? warpToColor : ", "+warpToColor);
        }
        return kits;
    }
    public String toEquals(final String warpName) {
        final String warp = warps.keySet().stream().filter(filter -> filter.equalsIgnoreCase(warpName)).findFirst().get();
        return warp == null ? "null" : warp;
    }
}
