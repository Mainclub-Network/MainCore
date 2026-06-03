package mainclub.network.core.utils;

import lombok.Getter;
import mainclub.network.core.Core;
import mainclub.network.core.database.ProfileManager;
import mainclub.network.core.database.Profile;
import mainclub.network.core.utils.event.list.CooldownExpiredEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Set;

public class RunnableTask extends BukkitRunnable {

    private final Core clubCore = Core.get();
    private final ProfileManager base = clubCore.getBase();
    private final Cooldown cooldown = clubCore.getCooldown();

    @Getter
    private final HashMap<String, Integer> updaterStrings = new HashMap<>();

    private double second = 0;
    private double lagg = 0;

    public RunnableTask() {
        runTaskTimer(clubCore, 0L, 10L); // 10 ticks = 500ms
    }

    @Override
    public void run() {

        // =========================
        // PLAYTIME
        // =========================
        if(second == 2.0) {
            second = 0;
            Bukkit.getOnlinePlayers().forEach(player -> {
                Profile profile = base.get(player.getUniqueId());
                if(profile == null) return;

                profile.setTimePlayed(profile.getTimePlayed() + 1);
                profile.setJoinTimePlaying(profile.getJoinTimePlaying() + 1);
            });
        }


        // =========================
        // CLEAR LAG
        // =========================
        if(lagg == 840.0) Bukkit.getOnlinePlayers().forEach(p-> p.sendMessage("§7Entidades eliminadas en 1 minuto."));
        //else if(lagg == 870.0) Bukkit.broadcastMessage("§7Entidades eliminadas en 30 segundos.");
        else if(lagg == 890.0) Bukkit.getOnlinePlayers().forEach(p-> p.sendMessage("§7Entidades eliminadas en 10 segundos."));

        if(lagg == 900.0) { //15m
            lagg = 0;

            for(World world : Bukkit.getWorlds()) {
                for(Chunk chunk : world.getLoadedChunks()) {
                    for(Entity entity : chunk.getEntities()) {
                        if (entity instanceof Item) {
                            entity.remove();
                            continue;
                        }

                        if (entity instanceof Monster) {
                            if (entity.getCustomName() != null) continue;
                            entity.remove();
                        }
                    }
                }
            }
            Bukkit.getOnlinePlayers().forEach(p-> p.sendMessage("§7§lEntidades eliminadas§7!"));
        }


        // =========================
        // TIMERS
        // =========================
        second += 0.5;
        lagg += 0.5;


        // =========================
        // COOLDOWNS
        // =========================
        try {
            Set<String> cooldownNames = cooldown.hashMapCooldown().keySet();
            cooldownNames.forEach(cooldownName -> {
                cooldown.getCooldownMap(cooldownName).forEach((player, timeLeft) -> {
                    if(timeLeft - System.currentTimeMillis() <= 0L) {
                        cooldown.removeCooldown(cooldownName, player);

                        Player online = Bukkit.getPlayer(player);
                        if(online != null) new CooldownExpiredEvent(online, cooldownName).create();
                    }
                });
            });

        } catch(Throwable throwable) {
            System.out.println("RunnableTask Fail: " + throwable);
        }
    }
}