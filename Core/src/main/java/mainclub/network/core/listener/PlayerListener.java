package mainclub.network.core.listener;

import mainclub.network.core.Core;
import mainclub.network.core.database.Profile;
import mainclub.network.core.database.ProfileManager;
import mainclub.network.core.utils.Cooldown;
import mainclub.network.core.utils.Moderation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
    private Core clubCore = Core.get();
    private ProfileManager base = clubCore.getBase();
    private Moderation moderation = Core.get().getModeration();
    private Cooldown cooldown = clubCore.getCooldown();

    @EventHandler
    public void whitelist(final AsyncPlayerPreLoginEvent event) {
        if(clubCore.getConfiguration().getBooleans().isWhitelist() && !clubCore.getConfiguration().getStringLists().getWhitelist().contains(event.getName())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', clubCore.getConfiguration().getStrings().getWhitelistText()));
        }
    }


    @EventHandler (priority = EventPriority.NORMAL)
    public void join(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        //player.setNoDamageTicks(18);

    //    if(event.getPlayer().hasPermission("clubcore.command.vanish")) moderation.setVanish(event.getPlayer().getUniqueId(), true);
    //    else Bukkit.getOnlinePlayers().stream().filter(filter->moderation.isVanish(filter.getUniqueId())).forEach(player::hidePlayer);

        clubCore.getVersion().setTabulator(player, clubCore.getConfiguration().getStringLists().getTabulatorHeader(), clubCore.getConfiguration().getStringLists().getTabulatorFooter());

        Profile profile;
        String groupPrefix = clubCore.getVault().chat().getGroupPrefix(player.getWorld(), clubCore.getVault().chat().getPrimaryGroup(player));
        String groupSuffix = clubCore.getVault().chat().getGroupSuffix(player.getWorld(), clubCore.getVault().chat().getPrimaryGroup(player));
        boolean groupPrefixPlus = groupPrefix.contains("&l");

        if(!base.has(player.getUniqueId().toString())) {
            player.getInventory().clear();
            event.setJoinMessage(null);
            if(clubCore.getSpawn().hasSpawn()) player.teleport(clubCore.getSpawn().location());
            if(clubCore.getKits().isKit("Starter")) clubCore.getKits().sendPlayerKit(player, "Starter");

            base.create(player);
            profile = base.get(player.getUniqueId());
            clubCore.getConfiguration().getStringLists().getFirstJoin().forEach(string-> Bukkit.broadcastMessage(clubCore.getVersion().color(string
                    .replace("<player>", player.getName())
                    .replace("<vault_group_prefix>", groupPrefix)
                    .replace("<vault_group_suffix>", groupSuffix)
                    .replace("<vault_group_color>", groupPrefix.substring(groupPrefix.length() -2))
                    .replace("<vault_group_color+>", groupPrefix.substring(groupPrefix.length() -2) + (groupPrefixPlus ? "§l" : ""))
                    .replace("<number>", profile.getJoinNumber()+""))));
        } else {
            event.setJoinMessage(null);
            profile = base.get(player.getUniqueId());

            if(!player.hasPermission("core.command.vanish")) {
                if(!clubCore.getConfiguration().getStringLists().getJoin().equals("[]")) {

                    Bukkit.getOnlinePlayers().stream().filter(p -> !p.getName().equals(player.getName())).forEach(p -> {
                        clubCore.getConfiguration().getStringLists().getJoin().forEach(string ->
                                p.sendMessage(clubCore.getVersion().color(string
                                .replace("<player>", player.getName())
                                .replace("<vault_group_prefix>", groupPrefix)
                                .replace("<vault_group_suffix>", groupSuffix)
                                .replace("<vault_group_color>", groupPrefix.substring(groupPrefix.length() -2))
                                .replace("<vault_group_color+>", groupPrefix.substring(groupPrefix.length() -2) + (groupPrefixPlus ? "§l" : ""))
                                .replace("<coord_x>", String.valueOf(player.getLocation().getBlockX()).replace(".0", ""))
                                .replace("<coord_z>", String.valueOf(player.getLocation().getBlockZ()).replace(".0", "")))));

                    });
                }
                /*base.get(event.getPlayer().getUniqueId().toString()).getRank(true)+player.getName()+ " se unio."*/
                profile.setJoinTimePlaying(0);
            }
        }

        if(player.getInventory().contains(moderation.modTools(player.getUniqueId())[0])) {
            player.getInventory().clear();
            moderation.setModMode(player.getUniqueId(), false);
        }

        clubCore.getHomes().load(player.getUniqueId());
    }
    @EventHandler (priority = EventPriority.HIGH)
    public void quit(final PlayerQuitEvent event) {
        event.setQuitMessage(null);
        final Player player = event.getPlayer();
        final Profile profile = base.get(player.getUniqueId());
        profile.setJoinTimePlaying(0);

        if(!player.hasPermission("core.command.vanish")) {
            if (!clubCore.getConfiguration().getStringLists().getQuit().equals("[]")) {
                String groupPrefix = clubCore.getVault().chat().getGroupPrefix(player.getWorld(), clubCore.getVault().chat().getPrimaryGroup(player));
                String groupSuffix = clubCore.getVault().chat().getGroupSuffix(player.getWorld(), clubCore.getVault().chat().getPrimaryGroup(player));
                boolean groupPrefixPlus = groupPrefix.contains("&l");

                Bukkit.getOnlinePlayers().forEach(p -> {
                    clubCore.getConfiguration().getStringLists().getJoin().forEach(string -> player.sendMessage(clubCore.getVersion().color(string
                            .replace("<player>", player.getName())
                            .replace("<vault_group_prefix>", groupPrefix)
                            .replace("<vault_group_suffix>", groupSuffix)
                            .replace("<vault_group_color>", groupPrefix.substring(groupPrefix.length() - 2))
                            .replace("<vault_group_color+>", groupPrefix.substring(groupPrefix.length() - 2) + (groupPrefixPlus ? "§l" : ""))
                            .replace("<coord_x>", String.valueOf(player.getLocation().getBlockX()).replace(".0", ""))
                            .replace("<coord_z>", String.valueOf(player.getLocation().getBlockZ()).replace(".0", ""))
                            .replace("<number>", profile.getJoinNumber() + ""))));

                });
            }
        }

        clubCore.getHomes().unload(player.getUniqueId());
        if(moderation.isFrozen(player.getUniqueId())) {
            moderation.setFrozen(player.getUniqueId(), false);
            moderation.broadcastToStaff("§4§l[SS] §3"+player.getName()+" §bse desconecto.");
            return;
        }
        if(moderation.isModMode(player.getUniqueId())){
            moderation.setModMode(player.getUniqueId(), false);
        }
        if(moderation.isVanish(player.getUniqueId())) moderation.setVanish(player.getUniqueId(), false);
    }
    @EventHandler (priority = EventPriority.HIGH)
    public void kick(final PlayerKickEvent  event) {
        final Player player = event.getPlayer();
        event.setLeaveMessage(null);
        final Profile profile = base.get(player.getUniqueId());
        profile.setJoinTimePlaying(0);

        clubCore.getHomes().unload(player.getUniqueId());
        if(moderation.isFrozen(player.getUniqueId())) {
            moderation.setFrozen(player.getUniqueId(), false);
            moderation.broadcastToStaff("§4§l[SS] §3"+player.getName()+" §bse desconecto.");
            return;
        }
        if(moderation.isModMode(player.getUniqueId())){
            moderation.setModMode(player.getUniqueId(), false);
        }
        if(moderation.isVanish(player.getUniqueId())) moderation.setVanish(player.getUniqueId(), false);
    }


    @EventHandler (priority = EventPriority.HIGH)
    public void move(final PlayerMoveEvent event) {
        if (event.getFrom().getBlock().getX() != event.getTo().getBlock().getX() || event.getFrom().getBlock().getY() != event.getTo().getBlock().getY() || event.getFrom().getBlock().getZ() != event.getTo().getBlock().getZ()) {
            final Player player = event.getPlayer();

            if (cooldown.isOnCooldown("warps", player.getName())) {
                cooldown.removeCooldown("warps", player.getName());
                player.sendMessage("§cTeletransporte cancelado, te moviste.");
                return;
            }
            if (cooldown.isOnCooldown("home", player.getName())) {
                cooldown.removeCooldown("home", player.getName());
                player.sendMessage("§cTeletransporte cancelado, te moviste.");
                return;
            }
            if (cooldown.isOnCooldown("spawn", player.getName())) {
                cooldown.removeCooldown("spawn", player.getName());
                player.sendMessage("§cTeletransporte cancelado, te moviste.");
                return;
            }
            if (cooldown.isOnCooldown("back", player.getName())) {
                cooldown.removeCooldown("back", player.getName());
                player.sendMessage("§cTeletransporte cancelado, te moviste.");
                return;
            }

            if (cooldown.hasCooldown(player.getName()) && clubCore.getHomes().hasHome(player.getUniqueId())) {
                clubCore.getHomes().getHomes(player.getUniqueId()).forEach(homeName ->  {
                    if(cooldown.isOnCooldown(player.getUniqueId().toString()+":home:"+homeName.split(";")[0], player.getName())) {
                        cooldown.removeCooldown(player.getUniqueId().toString()+":home:"+homeName.split(";")[0], player.getName());
                        player.sendMessage("§cTeletransporte cancelado, te moviste.");
                        return;
                    }
                });
            }
            if (moderation.isFrozen(player.getUniqueId())) event.setTo(event.getFrom());
        }
    }
    @EventHandler
    public void teleport(final PlayerTeleportEvent event) {
        if(!event.getPlayer().isOnline()) return;
        if(clubCore.getBase().has(event.getPlayer().getUniqueId().toString())) clubCore.getBase().get(event.getPlayer().getUniqueId().toString()).setBack(event.getPlayer().getLocation());
    }


    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void killPlayer(final EntityDamageByEntityEvent event) {//to checkear
        if (event.getEntity() instanceof Player && (event.getDamager() instanceof Player || event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player)) {
            final Player attacker = event.getDamager() instanceof Player ? ((Player) event.getDamager()).getPlayer() : (Player) ((Projectile) event.getDamager()).getShooter();
            final Player damaged = (Player) event.getEntity();

            final ItemStack offHand = new ItemStack(clubCore.getVersion().getItemInOffHand(damaged));
            if ((offHand.getType() == Material.AIR || offHand.getType() != Material.TOTEM_OF_UNDYING) && event.getFinalDamage() >= damaged.getHealth()) {
                final Profile profile = base.get(attacker.getUniqueId());
                profile.setKills(profile.getKills() + 1);
                profile.setStreak(profile.getStreak() + 1);
            }

            if (event.getDamager() instanceof Arrow && attacker != damaged) attacker.sendMessage(damaged.getName()+" ahora tiene "+damaged.getHealth());
        }
    }
    @EventHandler (priority = EventPriority.HIGHEST)
    public void death(final EntityDamageEvent event) {
        if ((event.getEntityType() != EntityType.PLAYER)) return;
        final Player damaged = ((Player) event.getEntity());

        if (event.getFinalDamage() >= damaged.getHealth()) {
            final Profile profile = base.get(damaged.getUniqueId());
            profile.setBack(damaged.getLocation());
            profile.setDeaths(profile.getDeaths()+1);
            profile.setMaxStreak(Math.max(profile.getStreak(), profile.getMaxStreak()));
            profile.setStreak(0);
        }

    }


    //@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void respawn(final PlayerRespawnEvent event) {
        if(clubCore.getSpawn().hasSpawn()) event.setRespawnLocation(clubCore.getSpawn().location());
    }
    //@EventHandler
    public void onPlayerVelocity(final PlayerVelocityEvent event) {
        final Player player = event.getPlayer();
        final EntityDamageEvent lastDamage = player.getLastDamageCause();
        if (lastDamage == null || !(lastDamage instanceof EntityDamageByEntityEvent)) {
            return;
        }
        if (((EntityDamageByEntityEvent)lastDamage).getDamager() instanceof Player) {
            event.setCancelled(true);
        }
    }
    //@EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        //clubCore.getVersion().setKnockback(attacker, victim);
    }

}
