package mainclub.network.core.utils;

import lombok.Getter;
import lombok.Setter;
import mainclub.network.core.utils.cuboid.CuboidManager;
import org.bukkit.Location;

public class Claim {
    @Getter
    private String name;
    @Setter
    @Getter
    private Location spawn;
    @Setter
    @Getter
    private boolean segureClaim = false;
    @Setter
    @Getter
    private boolean quitTeleport = false;
    @Setter
    @Getter
    private boolean pvp = false;
    @Setter
    @Getter
    private int priority = 0;
    @Getter
    private CuboidManager cuboid;
    @Getter
    @Setter
    private String joinPermission;
    @Setter
    @Getter
    private String[] joinCommands;
    @Getter
    @Setter
    private String[] quitCommands;
    @Getter
    @Setter
    private String[] cancelCommands;



    public Claim(final String name, CuboidManager cuboid) {
        this.name = name;
        this.cuboid = cuboid;
    }
}
