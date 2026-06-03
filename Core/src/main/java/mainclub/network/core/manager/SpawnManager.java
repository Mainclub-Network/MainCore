package mainclub.network.core.manager;

import mainclub.network.core.Core;
import mainclub.network.core.configuration.Configuration;
import mainclub.network.core.utils.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnManager {
    private final Core main = Core.get();
    private final Configuration configuration = main.getConfiguration();
    private final Cooldown cooldown = main.getCooldown();

    public boolean hasSpawn() {return configuration.getLocationsFile().contains("LOCATIONS.SPAWN") && !configuration.getLocationsFile().getString("LOCATIONS.SPAWN").isEmpty();}
    public void set(final Location location) {
        if(!cooldown.isCooldown("spawn")) cooldown.createCooldown("spawn");

        configuration.getLocationsFile().set("LOCATIONS.SPAWN", location.getWorld().getName()+";"+location.getX()+";"+location.getY()+";"+location.getZ()+";"+location.getYaw()+";"+location.getPitch());
        //clubSpawn.file().save();
    }
    public void delete() {
        configuration.getLocationsFile().set("LOCATIONS.SPAWN", null);
        //clubSpawn.file()..save();
    }

    public Location location() {
        final String[] values = configuration.getLocationsFile().getString("LOCATIONS.SPAWN").split(";");
        return new Location(Bukkit.getWorld(values[0]), Double.valueOf(values[1]), Double.valueOf(values[2]), Double.valueOf(values[3]), Float.valueOf(values[4]), Float.valueOf(values[5]));
    }
}
