package mainclub.network.core.manager;

import lombok.Getter;
import mainclub.network.core.Core;
import mainclub.network.core.configuration.file.LocationsFile;
import mainclub.network.core.utils.Claim;
import mainclub.network.core.utils.cuboid.CuboidDirection;
import mainclub.network.core.utils.cuboid.CuboidManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ClaimManager {
    private final LocationsFile file = Core.get().getConfiguration().getLocationsFile();
    @Getter
    private final List<Claim> claims = new ArrayList<>();
    @Getter
    private final HashMap<UUID, Claim> map = new HashMap<>();

    public ClaimManager() {
        if(!file.getStringList("LOCATIONS.CLAIMS").isEmpty()) load();

        //claimName;1;worldName:1.1.1,2.2.2;0.0.0.0.0;false;false;false
        //claimName;priority;worldName;cuboid1,cuboid2;spawnLocation;safezone;pvp;quitTeleport
    }

    public void load() {
        file.getStringList("LOCATIONS.CLAIMS").forEach(string -> {
            final String[] values = string.split(";");

            final String claimName = values[0];
            final int priority = Integer.parseInt(values[1]);
            final String worldName = values[2];
            final String[] locationFirstValues = values[3].split(",")[0].split(":");
            final String[] locationSecondValues = values[3].split(",")[1].split(":");
            final String[] spawnValues = values[4].equals("null") ? null : values[4].split(":");
            final Location spawn = new Location(Bukkit.getWorld(worldName), Double.valueOf(spawnValues[0]), Double.valueOf(spawnValues[1]), Double.valueOf(spawnValues[2]), Float.valueOf(spawnValues[3]), Float.valueOf(spawnValues[4]));
            final Location locationFirst = new Location(Bukkit.getWorld(worldName), Double.valueOf(locationFirstValues[0]), Double.valueOf(locationFirstValues[1]), Double.valueOf(locationFirstValues[2]));
            final Location locationSecond = new Location(Bukkit.getWorld(worldName), Double.valueOf(locationSecondValues[0]), Double.valueOf(locationSecondValues[1]), Double.valueOf(locationSecondValues[2]));
            final boolean segureClaim = Boolean.parseBoolean(values[5]);
            final boolean pvp = Boolean.parseBoolean(values[6]);
            final boolean quitTeleport = Boolean.parseBoolean(values[7]);
            final String joinPermission = values[8].equals("null") ? null : values[8];
            final String[] joinCommands = values[9].equals("[]") ? new String[]{} : values[9].replace("[", "").replace("]", "").split(", ");
            final String[] quitCommands = values[10].equals("[]") ? new String[]{} : values[10].replace("[", "").replace("]", "").split(", ");
            final String[] cancelCommands = values[11].equals("[]") ? new String[]{} : values[11].replace("[", "").replace("]", "").split(", ");


            final CuboidManager cuboidManager = new CuboidManager(locationFirst, locationSecond);
            final Claim claim = new Claim(claimName, cuboidManager);
            if(spawnValues != null) claim.setSpawn(spawn);
            claim.setPriority(priority);
            claim.setSegureClaim(segureClaim);
            claim.setPvp(pvp);
            claim.setQuitTeleport(quitTeleport);
            claim.setJoinPermission(joinPermission);
            claim.setJoinCommands(joinCommands);
            claim.setQuitCommands(quitCommands);
            claim.setCancelCommands(cancelCommands);

            claims.add(claim);
        });
    }
    public void save() {
        if(claims.isEmpty()) return;
        final List<String> updated = new ArrayList<>();
        claims.forEach(claim ->
                updated.add(
                        claim.getName()+";" +
                        claim.getPriority()+";"+
                        claim.getSpawn().getWorld().getName()+";"+

                        claim.getCuboid().getLowerX()+":"+claim.getCuboid().getLowerY()+":"+claim.getCuboid().getLowerZ()
                                +","+
                        claim.getCuboid().getUpperX()+":"+claim.getCuboid().getUpperY()+":"+claim.getCuboid().getUpperZ()

                        +";"+
                        claim.getSpawn().getX()+":"+claim.getSpawn().getY()+":"+claim.getSpawn().getZ()+":"+claim.getSpawn().getYaw()+":"+claim.getSpawn().getPitch()+";"+
                        claim.isSegureClaim()+";"+claim.isPvp()+";"+claim.isQuitTeleport()+";"+claim.getJoinPermission()+";"+ Arrays.toString(claim.getJoinCommands()).replace("[[", "[").replace("]]", "]")+";"+Arrays.toString(claim.getQuitCommands()).replace("[[", "[").replace("]]", "]")+";"+Arrays.toString(claim.getCancelCommands()).replace("[[", "[").replace("]]", "]")));
        file.set("LOCATIONS.CLAIMS", updated);
        file.save();
    }

    public void create(final String claimName, final Location locationFirst, final Location locationSecond) {
        CuboidManager cuboidManager = null;

        if(claimName.equalsIgnoreCase("Spawn")) {
            cuboidManager = new CuboidManager(locationFirst, locationSecond).expand(CuboidDirection.UP, 500).expand(CuboidDirection.DOWN, 500);

            final CuboidManager cuboidWarzone = new CuboidManager(locationFirst, locationSecond).expandAll(120);
            final Claim warzone = new Claim("WarZone", cuboidWarzone);
            warzone.setSpawn(cuboidWarzone.getCenter());
            warzone.setPriority(0);
            warzone.setSegureClaim(true);
            warzone.setPvp(true);
            warzone.setQuitTeleport(false);
            warzone.setJoinPermission(null);
            warzone.setJoinCommands(new String[]{});
            warzone.setQuitCommands(new String[]{});
            warzone.setCancelCommands(new String[]{});

            claims.add(warzone);
        }

        if(cuboidManager == null) cuboidManager = new CuboidManager(locationFirst, locationSecond);
        final Claim claim = new Claim(claimName, cuboidManager);
        claim.setSpawn(cuboidManager.getCenter());
        claim.setPriority(claimName.equalsIgnoreCase("Spawn") ? 1 : 0);
        claim.setSegureClaim(claimName.equalsIgnoreCase("Spawn"));
        claim.setPvp(false);
        claim.setQuitTeleport(false);
        claim.setJoinPermission(null);
        claim.setJoinCommands(new String[]{});
        claim.setQuitCommands(new String[]{});
        claim.setCancelCommands(new String[]{});

        claims.add(claim);

        final List<String> updated = new ArrayList<>();
        claims.forEach(claimList ->
                updated.add(claimList.getName()+";"+claimList.getPriority()+";"+claimList.getSpawn().getWorld().getName()+";"+
                        claimList.getCuboid().getLowerX()+":"+claimList.getCuboid().getLowerY()+":"+claimList.getCuboid().getLowerZ()+","+
                        claimList.getCuboid().getUpperX()+":"+claimList.getCuboid().getUpperY()+":"+claimList.getCuboid().getUpperZ()+";"+
                        claimList.getSpawn().getX()+":"+claimList.getSpawn().getY()+":"+claimList.getSpawn().getZ()+":"+claimList.getSpawn().getYaw()+":"+claimList.getSpawn().getPitch()+";"+
                        claimList.isSegureClaim()+";"+claimList.isPvp()+";"+claimList.isQuitTeleport()+";"+claimList.getJoinPermission()+";"+ Arrays.toString(claimList.getJoinCommands())+";"+Arrays.toString(claimList.getQuitCommands())+";"+Arrays.toString(claimList.getCancelCommands())));
        file.set("LOCATIONS.CLAIMS", updated);
        file.save();
    }
    public void delete(final String claimName) {
        claims.removeIf(claim -> claim.getName().equals(claimName));
    }
    public boolean has(final String claimName) {
        return claims.stream().anyMatch(filter -> filter.getName().equals(claimName));
    }
    public boolean has(final Block block) {
        return claims.stream().anyMatch(filter -> filter.getCuboid().contains(block));
    }
    public boolean nearbyClaim(final Chunk chunk) {
        for(final Claim claim : claims) {
            if(claim.getCuboid().getChunks().contains(chunk)) return true;
        }
        return false;
    }
    public int size() {return claims.size();}
    public String list() {
        String claimList = "";
        for(Claim claim : claims) {
            claimList += (claimList.equals("") ? claim.getName() : ", "+claim.getName());
        }
        return claimList;
    }
    public Claim get(final String claimName) {
        AtomicReference<Claim> claim = new AtomicReference<>();
        claims.stream().filter(filter -> filter.getName().equalsIgnoreCase(claimName)).findFirst().ifPresent(claim::set);
        return claim.get();
    }

    public Claim get(final Block block) {
        Claim claim = null;
        int priority = -1;

        for(final Claim searchClaim : claims) {
            if(block.getWorld().getName().equals(searchClaim.getCuboid().getWorld().getName()) && searchClaim.getCuboid().contains(block.getX(), block.getY(), block.getZ()) && searchClaim.getPriority() > priority) {
                priority = searchClaim.getPriority();
                claim = searchClaim;
            }
        }

        return claim;
    }
}
