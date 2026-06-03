package mainclub.network.core.database;

import lombok.Getter;
import lombok.Setter;
import mainclub.network.core.Core;
import mainclub.network.core.hook.VaultAPI;
import mainclub.network.core.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Profile {
    @Getter private UUID UUID;
    @Getter @Setter private String name, nick;

    @Getter @Setter private int joinNumber;
    @Getter @Setter private String joinDate;

    @Getter @Setter private int timePlayed = 0;
    @Getter @Setter private int dayJoins = 0;
    @Getter @Setter private int joinTimePlaying = 0;

    @Getter @Setter private int kills, deaths, streak, maxStreak;

    @Getter @Setter private Location back;

    public Profile(final UUID uuid, final String name) {
        this.UUID = uuid;
        this.name = name;
    }

    public String getNick() {
        return nick.equals("none") ? name : nick;
    }

    public String getRank(final boolean onlyColor) {//ADD METHOD ASYC FOR LEADERBOARDS
        AtomicReference<String> format = new AtomicReference<>("asd");
        //Bukkit.getScheduler().runTaskAsynchronously(ClubSurvival.get(), () -> {
        final VaultAPI vault = Core.get().getVault();
        final OfflinePlayer player = Bukkit.getOfflinePlayer(UUID);
        String verified = vault.permission().playerHas(null, player, "verify") ? onlyColor ? "":"+" : "";

        if (player.isOp() || vault.permission().playerHas(null, player, "group.owner")) format.set(onlyColor ? "§4" : "§4Dueño"+verified);
        else if (vault.permission().playerHas(null, player, "group.coowner")) format.set(onlyColor ? "§c" : "§cC. Owner"+verified);
        else if (vault.permission().playerHas(null, player, "group.headadmin")) format.set(onlyColor ? "§c" : "§cH. Admin"+verified);
        else if (vault.permission().playerHas(null, player, "group.admin")) format.set(onlyColor ? "§c" : "§cAdmin"+verified);
        else if (vault.permission().playerHas(null, player, "group.manager")) format.set(onlyColor ? "§d" : "§dManager"+verified);
        else if (vault.permission().playerHas(null, player, "group.comunnitymanager")) format.set(onlyColor ? "§9" : "§9C. Manager"+verified);
        else if (vault.permission().playerHas(null, player, "group.seo")) format.set(onlyColor ? "§d" : "§dSEO"+verified);
        else if (vault.permission().playerHas(null, player, "group.developer")) format.set(onlyColor ? "§b" : "§bDev"+verified);
        else if (vault.permission().playerHas(null, player, "group.configurator")) format.set(onlyColor ? "§9" : "§9Config"+verified);
        else if (vault.permission().playerHas(null, player, "group.headmod")) format.set(onlyColor ? "§3" : "§3H. Mod"+verified);
        else if (vault.permission().playerHas(null, player, "group.mod")) format.set(onlyColor ? "§b" : "§bMod"+verified);
        else if (vault.permission().playerHas(null, player, "group.trial-mod")) format.set(onlyColor ? "§b" : "§eT. Mod"+verified);
        else if (vault.permission().playerHas(null, player, "group.builder")) format.set(onlyColor ? "§2" : "§2Builder"+verified);
        else if (vault.permission().playerHas(null, player, "group.helper")) format.set(onlyColor ? "§9" : "§9Helper"+verified);
        else if (vault.permission().playerHas(null, player, "group.famous")) format.set(onlyColor ? "§d" : "§5Famous"+verified);
        else if (vault.permission().playerHas(null, player, "group.media")) format.set(onlyColor ? "§d" : "§dMedia"+verified);
        else if (vault.permission().playerHas(null, player, "group.mini-media")) format.set(onlyColor ? "§d" : "§dMiniMedia"+verified);
        else if (vault.permission().playerHas(null, player, "group.legend")) format.set(onlyColor ? "§5" : "§5LEGEND"+verified);
        else if (vault.permission().playerHas(null, player, "group.elite")) format.set(onlyColor ? "§b" : "§bELITE"+verified);
        else if (vault.permission().playerHas(null, player, "group.hero")) format.set(onlyColor ? "§f" : "§fHERO"+verified);
        else format.set(onlyColor ? "§7" : "§7Miembro"+verified);
        //});

        return format.get();
    }
    public String formatTime(final int time) {
        return new TimeUtil(time).format();
    }
}
