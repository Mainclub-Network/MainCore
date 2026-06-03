package mainclub.network.core.utils;

import org.bukkit.Bukkit;

import java.util.HashMap;

public class Cooldown {
    private HashMap<String, HashMap<String, Long>> cooldown = new HashMap<>();

    public Cooldown() {
        createCooldown("back");
        createCooldown("warp");
        createCooldown("combat");
        createCooldown("home");
        createCooldown("fall");
        createCooldown("repair");
        createCooldown("repair_all");
    }
    
    public HashMap<String, HashMap<String, Long>> hashMapCooldown() {
        return cooldown;
    }
    public void createCooldown(final String cooldownName) {
        if (cooldown.containsKey(cooldownName)) {
            throw new IllegalArgumentException("Cooldown "+cooldownName+" already exists");
        } else {
            cooldown.put(cooldownName, new HashMap<String, Long>());
        }
    }
    public void deleteCooldown(final String cooldownName) {
        if (!cooldown.containsKey(cooldownName)) throw new IllegalArgumentException("Not exists");
        cooldown.remove(cooldownName);
    }
    public boolean isCooldown(final String cooldownName) {return cooldown.containsKey(cooldownName);}
    public void clearCooldowns() { cooldown.clear(); }
    public HashMap<String, Long> getCooldownMap(final String cooldownName) {
        if (cooldown.containsKey(cooldownName)) return cooldown.get(cooldownName);
        return null;
    }
    public void addCooldown(final String cooldownName, final String playerName, final int seconds) {
        if (!cooldown.containsKey(cooldownName)) throw new IllegalArgumentException(cooldownName + " doesn't exist");
        final long next = System.currentTimeMillis() + seconds * 1000L;
        cooldown.get(cooldownName).put(playerName, next);
    }
    public boolean isOnCooldown(final String cooldownName, final String playerName) {return cooldown.containsKey(cooldownName) && cooldown.get(cooldownName).containsKey(playerName) && System.currentTimeMillis() <= cooldown.get(cooldownName).get(playerName);}
    public boolean hasCooldown(final String playerName) {
        for(final String cooldowns : cooldown.keySet()) {
            if(cooldown.get(cooldowns).containsKey(playerName)) return true;
        }
        return false;
    }

    public void playerClearCooldown(final String playerName) {
        cooldown.entrySet().forEach(c-> {
            if(cooldown.get(c.getKey()).containsKey(playerName)) cooldown.get(c.getKey()).remove(playerName);
        });
    }
    public int getCooldownForPlayerInt(final String cooldownName, final String playerName) {return (int)((cooldown.get(cooldownName).get(playerName) - System.currentTimeMillis()) / 1000L);}
    public long getCooldownForPlayerLong(final String cooldownName, final String playerName) {return cooldown.get(cooldownName).get(playerName) - System.currentTimeMillis();}
    public void removeCooldown(final String cooldownName, final String playerName) {
        if (!cooldown.containsKey(cooldownName)) throw new IllegalArgumentException(cooldownName + " doesn't exist");
        cooldown.get(cooldownName).remove(playerName);
    }
    public String toFormat(final String cooldownName, final String playerName) {

        final int toSeconds = (int) (getCooldownForPlayerLong(cooldownName, playerName) / 1000L);
        if (toSeconds < 60) {
            return toSeconds + "s";
        } else if (toSeconds < 3600) {
            int minutes = toSeconds / 60;
            int seconds = toSeconds % 60;
            return minutes + "m " + seconds + "s";
        } else if (toSeconds < 86400) {
            int hours = toSeconds / 3600;
            int minutes = (toSeconds % 3600) / 60;
            int seconds = toSeconds % 60;
            return hours + "h " + minutes + "m " + seconds + "s";
        } else {
            int days = toSeconds / 86400;
            int hours = (toSeconds % 86400) / 3600;
            int minutes = (toSeconds % 3600) / 60;
            return days + "d " + hours + "h " + minutes + "m";
        }
    }
}
