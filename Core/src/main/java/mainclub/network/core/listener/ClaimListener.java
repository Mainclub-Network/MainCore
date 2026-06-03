package mainclub.network.core.listener;

import mainclub.network.core.Core;
import mainclub.network.core.manager.ClaimManager;
import mainclub.network.core.utils.Claim;
import mainclub.network.core.utils.event.list.CooldownExpiredEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

public class ClaimListener implements Listener {
    private final ClaimManager claims = Core.get().getClaims();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void join(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Block joinBlock = player.getLocation().getBlock();
        if (claims.has(joinBlock)) claims.getMap().put(player.getUniqueId(), claims.get(joinBlock));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void move(final PlayerMoveEvent event) {
        if (event.getFrom().getBlock().getX() != event.getTo().getBlock().getX() || event.getFrom().getBlock().getY() != event.getTo().getBlock().getY() || event.getFrom().getBlock().getZ() != event.getTo().getBlock().getZ()) {

            final Player player = event.getPlayer();
            final UUID playerId = event.getPlayer().getUniqueId();
            final Block moveToBlock = event.getTo().getBlock();

            final Claim currentClaim = claims.getMap().get(playerId);
            final Claim nextClaim = claims.get(moveToBlock);


            if(currentClaim != null && nextClaim != null) { //CHANGED CLAIM
                if(currentClaim.getName().equals(nextClaim.getName())) return;
                Core.get().getCooldown().addCooldown("fall", player.getName(), 8);
                claims.getMap().put(playerId, nextClaim);
            } else if(currentClaim == null && nextClaim != null) { //ENTRY A CLAIM
                claims.getMap().put(playerId, nextClaim);
            } else if(currentClaim != null && nextClaim == null) { //LEAVE A CLAIM
                Core.get().getCooldown().addCooldown("fall", player.getName(), 8);
                claims.getMap().remove(playerId);
            }

            /*if (currentClaim != null) { //MOVING ON CURRENT CLAIM
                Claim newClaim = null;
                for (Claim claim : claims.getClaims()) {
                    if (currentClaim.getName() != claim.getName() && claim.getCuboid().contains(moveToBlock)) {
                        newClaim = claim;
                        break;
                    }
                }
                if (newClaim != null) {
                    claims.getMap().put(playerId, newClaim);
                    ClubCore.get().getCooldown().addCooldown("fall", player.getName(), 6);
                    for (String command : newClaim.getJoinCommands()) {
                        player.performCommand(command);
                    }
                }
                return;
            }


            Claim newClaim = null;
            for (Claim claim : claims.getClaims()) {
                if (claim.getCuboid().contains(moveToBlock)) {
                    newClaim = claim;
                    if (claim.getJoinPermission() != null && !player.hasPermission(claim.getJoinPermission())) {
                        event.setCancelled(true);
                        Vector vector = player.getLocation().getDirection();
                        vector.multiply(-1);

                        player.setVelocity(vector.multiply(0.8));
                        return;
                    }
                    for (String command : claim.getJoinCommands()) {
                        player.performCommand(command);
                    }
                    break;
                }
            }

            if (newClaim != null) {
                claims.getMap().put(playerId, newClaim);
            } else {
                if (currentClaim != null) {
                    for (String command : currentClaim.getQuitCommands()) {
                        player.performCommand(command);
                    }
                    if (currentClaim.isQuitTeleport()) {
                        event.getPlayer().teleport(currentClaim.getSpawn());
                        return;
                    }
                    claims.getMap().remove(playerId);
                    ClubCore.get().getCooldown().addCooldown("fall", player.getName(), 10);
                }

            }*/

        }
    }

    @EventHandler
    public void hit(final EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player && claims.has(event.getEntity().getLocation().getBlock()) && claims.get(event.getEntity().getLocation().getBlock()).getName().equalsIgnoreCase("spawn")) {
            event.setCancelled(true);
            return;
        }

        if (event.getEntity() instanceof Player && (event.getDamager() instanceof Player || event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player)) {
            final Player attacker = event.getDamager() instanceof Player ? ((Player) event.getDamager()).getPlayer() : (Player) ((Projectile) event.getDamager()).getShooter();
            final Player damaged = (Player) event.getEntity();

            if (claims.has(attacker.getLocation().getBlock()) || claims.has(damaged.getLocation().getBlock())) {
                if(!claims.get(attacker.getLocation().getBlock()).isPvp() || !claims.get(damaged.getLocation().getBlock()).isPvp()) event.setCancelled(true);
            }
        }
        
    }

    @EventHandler
    public void onFallDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();

            if(claims.has(player.getLocation().getBlock()) && claims.get(player.getLocation().getBlock()).getName().equalsIgnoreCase("spawn") && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
                return;
            }
            if (Core.get().getCooldown().isOnCooldown("fall", event.getEntity().getName()) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                Core.get().getCooldown().removeCooldown("fall", event.getEntity().getName());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void command(final PlayerCommandPreprocessEvent event) {
        if (claims.has(event.getPlayer().getLocation().getBlock())) {
            Claim claim = claims.get(event.getPlayer().getLocation().getBlock());

            if (claim.getCancelCommands() != null) {
                for (String command : claim.getCancelCommands()) {
                    final String[] split = event.getMessage().split(" ");
                    if (event.getMessage().startsWith("/") && event.getMessage().toLowerCase().contains(command.toLowerCase())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage("§cComando denegado en está area.");
                        return;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void teleport(final PlayerTeleportEvent event) {
        final Block teleportBlock = event.getTo().getBlock();
        if (claims.has(teleportBlock)) {
            if (claims.get(teleportBlock).getJoinPermission() != null && !event.getPlayer().hasPermission(claims.get(teleportBlock).getJoinPermission())) {
                event.setCancelled(true);
                return;
            }
            if (claims.getMap().containsKey(event.getPlayer().getUniqueId()) && claims.get(teleportBlock) == claims.getMap().get(event.getPlayer().getUniqueId()))
                return;
            claims.getMap().put(event.getPlayer().getUniqueId(), claims.get(teleportBlock));
        } else claims.getMap().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void mobSpawning(final CreatureSpawnEvent event) {
        final Block spawningBlock = event.getLocation().getBlock();

        if (claims.has(spawningBlock)) { //NO DECTECT SI HAY 2 CLAIMS
            if (claims.get(spawningBlock).isSegureClaim()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void placeBlock(final BlockPlaceEvent event) {
        final Block spawningBlock = event.getBlock();
        if (claims.has(spawningBlock) && claims.get(spawningBlock).isSegureClaim() && !event.getPlayer().isOp()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void breakBlock(final BlockBreakEvent event) {
        boolean breakBlock = false;
        //if(claims.has(block) && claims.get(block).isSegureClaim() && !event.getPlayer().isOp()) event.setCancelled(true);

        for (Claim claim : claims.getClaims()) {
            if (claim.isSegureClaim() && claim.getCuboid().contains(event.getBlock())) {
                breakBlock = true;
                break;
            }
        }
        if(breakBlock && !event.getPlayer().isOp()) event.setCancelled(true);

    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void burnBlock(final BlockBurnEvent event) {
        final Block block = event.getBlock();
        if(claims.has(block) && (claims.get(block).isSegureClaim() || claims.get(block).getName().contains("warzone"))) event.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void explodeBlock(final BlockExplodeEvent event) {
        final Block block = event.getBlock();
        if(claims.has(block) && (claims.get(block).isSegureClaim() || claims.get(block).getName().contains("warzone"))) event.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void entityExplodeBlock(final EntityExplodeEvent event) {
        final Block block = event.getLocation().getBlock();
        if(claims.has(block) && (claims.get(block).isSegureClaim() || claims.get(block).getName().contains("warzone"))) event.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void fadeBlock(final BlockFadeEvent event) {
        final Block block = event.getBlock();
        if(claims.has(block) && (claims.get(block).isSegureClaim() || claims.get(block).getName().contains("warzone"))) event.setCancelled(true);
    }


    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void igniteBlock(final BlockIgniteEvent event) {
        final Block block = event.getBlock();
        if(claims.has(block) && (claims.get(block).isSegureClaim() || claims.get(block).getName().contains("warzone"))) event.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void decayBlock(final LeavesDecayEvent event) {
        final Block block = event.getBlock();
        if(claims.has(block) && (claims.get(block).isSegureClaim() || claims.get(block).getName().contains("warzone"))) event.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void interactBlock(final PlayerInteractEvent event) {
        if(event.hasBlock() && claims.has(event.getClickedBlock()) && claims.get(event.getClickedBlock()).isSegureClaim() && !event.getPlayer().isOp()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void cooldown(final CooldownExpiredEvent event) {
        if(event.getName().equals("spawn")) claims.getMap().put(event.getPlayer().get().getUniqueId(), claims.get("spawn"));
    }
}
