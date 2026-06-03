package mainclub.network.core.utils;

import mainclub.network.core.Core;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Moderation {
    private boolean muteChat = false;
    private ArrayList<UUID> modMode = new ArrayList<>();
    private ArrayList<UUID> staffChat = new ArrayList<>();
    private ArrayList<UUID> vanish = new ArrayList<>();
    private ArrayList<UUID> god = new ArrayList<>();
    private HashMap<UUID, BukkitRunnable> frozen = new HashMap<>();


    private HashMap<UUID, ItemStack> hat = new HashMap<>();
    private HashMap<UUID, ItemStack[]> armor = new HashMap<>();
    private HashMap<UUID, ItemStack[]> inventory = new HashMap<>();
    private HashMap<UUID, Integer> exp = new HashMap<>();
    private HashMap<UUID, Double> health = new HashMap<>();
    private HashMap<UUID, Double> maxHealth = new HashMap<>();
    private HashMap<UUID, GameMode> gamemode = new HashMap<>();


    public boolean isMuteChat(){return muteChat;}
    public void setMuteChat(final boolean value) {
        muteChat = value;
    }

    public boolean isModMode(final UUID uuid) {
        return modMode.contains(uuid);
    }
    public void setModMode(final UUID uuid, final boolean value) {
        if(value) {
            modMode.add(uuid);
            saveInv(uuid);
            clearInv(uuid);

            final Player player = Bukkit.getPlayer(uuid);
            player.setGameMode(GameMode.CREATIVE);
            ItemStack[] item = modTools(uuid);
            player.getInventory().setItem(0, item[0]);
            player.getInventory().setItem(1, item[1]);
            player.getInventory().setItem(5, item[2]);
            player.getInventory().setItem(8, item[3]);
            player.getInventory().setHelmet(new Item(Material.LEATHER_HELMET, 1, 0).display("&bStaff Member Helmet").setColor(Color.AQUA).load());



            return;
        }
        modMode.remove(uuid);
        clearInv(uuid);
        loadInv(uuid);
    }

    public boolean isVanish(final UUID uuid) {
        return vanish.contains(uuid);
    }
    public void setVanish(final UUID uuid, final boolean value) {
        if(value) {
            vanish.add(uuid);
            Bukkit.getOnlinePlayers().stream().filter(filter -> !filter.hasPermission("core.command.modmode")).forEach(player-> player.hidePlayer(Bukkit.getPlayer(uuid)));
        } else {
            vanish.remove(uuid);
            Bukkit.getOnlinePlayers().stream().filter(filter -> !filter.hasPermission("core.command.modmode")).forEach(player-> player.showPlayer(Bukkit.getPlayer(uuid)));
        }
    }
    public boolean isGod(final UUID uuid) {
        return god.contains(uuid);
    }
    public void setGod(final UUID uuid, final boolean value) {
        if(value) god.add(uuid);
        else god.remove(uuid);

    }

    public boolean isFrozen(final UUID uuid) {
        return frozen.containsKey(uuid);
    }
    public void setFrozen(final UUID uuid, final boolean value) {
        final Player player = Bukkit.getPlayer(uuid);
        if(value) {
            frozen.put(uuid, new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage("\n§f§l█████████\n" +
                            "§f§l████§c§l█§f§l████\n" +
                            "§f§l███§c§l█§0§l█§c§l█§f§l███\n" +
                            "§f§l██§c§l█§6§l█§0§l█§6§l█§c§l█§f§l██\n" +
                            "§f§l██§c§l█§6§l█§0§l█§6§l█§c§l█§f§l██\n" +
                            "§f§l██§c§l█§6§l█§0§l█§6§l█§c§l█§f§l██\n" +
                            "§f§l█§c§l█§6§l█████§c§l█§f§l█\n" +
                            "§c§l█§6§l███§0§l█§6§l███§c§l█\n" +
                            "§c§l█████████\n" +
                            "§f§l█████████\n");
                }
            });
            frozen.get(uuid).runTaskTimerAsynchronously(Core.get(), 0, 15*20);
            hat.put(uuid, player.getInventory().getHelmet());

            player.setWalkSpeed(0.0F);
            //player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, 180));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000, 180));
            player.getInventory().setHelmet(new ItemStack(Material.PACKED_ICE));
            //player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
        } else {
            frozen.get(uuid).cancel();
            frozen.remove(uuid);
            player.getInventory().setHelmet(hat.get(uuid));
            hat.remove(uuid);
            player.setWalkSpeed(0.2f);
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
            //player.playSound(player.getLocation(), Sound.NOTE_PLING, 2.0F, 2.0F);
        }
    }

    public void broadcastToStaff(final String text) {
        Bukkit.getOnlinePlayers().stream().filter(filter -> filter.hasPermission("core.command.staffchat")).forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', text)));
    }



    public boolean hasSavedInv(final UUID uuid) {
        return inventory.containsKey(uuid);
    }
    private void saveInv(final UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        armor.put(uuid, player.getInventory().getArmorContents());
        inventory.put(uuid, player.getInventory().getContents());
        exp.put(uuid, player.getLevel());
        maxHealth.put(uuid, player.getMaxHealth());
        health.put(uuid, player.getHealth());
        gamemode.put(uuid, player.getGameMode());
    }
    private void clearInv(final UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.getInventory().setContents(new ItemStack[]{null, null, null, null});
        player.getInventory().setArmorContents(null);
        player.setLevel(0);
        player.updateInventory();
    }
    private void loadInv(final UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.getInventory().setContents(inventory.get(uuid));
        player.getInventory().setArmorContents(armor.get(uuid));
        player.setLevel(exp.get(uuid));
        player.setMaxHealth(maxHealth.get(uuid));
        player.setHealth(health.get(uuid));
        player.setGameMode(gamemode.get(uuid));
        player.updateInventory();

        inventory.remove(uuid);
        armor.remove(uuid);
        exp.remove(uuid);
        health.remove(uuid);
        maxHealth.remove(uuid);
        gamemode.remove(uuid);
    }

    public ItemStack[] modTools(final UUID uuid) {
        boolean vanished = isVanish(uuid);
        ItemStack yesVanish = new Item(Material.getMaterial(Core.get().getVersion().getYesVanish()), 1, 10).display("&eVanish: &aSi").load();
        ItemStack noVanish = new Item(Material.getMaterial(Core.get().getVersion().getNoVanish()), 1, 8).display("&eVanish: &cNo").load();
        return new ItemStack[]{
                new Item(Material.BOOK, 1, 0).display("&bVer Inv").load(),
                new Item(Material.PACKED_ICE, 1, 0).display("&9Frozen Player").load(),
                new Item(Material.COMPASS,1,0).display("&aRandom Teleport").load(),
                vanished ? yesVanish: noVanish};
    }
}
