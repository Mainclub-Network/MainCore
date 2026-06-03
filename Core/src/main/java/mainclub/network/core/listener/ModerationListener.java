package mainclub.network.core.listener;

import mainclub.network.core.Core;
import mainclub.network.core.utils.Moderation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.concurrent.ThreadLocalRandom;

public class ModerationListener implements Listener {
    private Core clubCore = Core.get();
    private Moderation moderation = Core.get().getModeration();

    @EventHandler(priority = EventPriority.HIGH)
    public void interact(final PlayerInteractEvent event) {
        if(moderation.isFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            return;
        }
        if(moderation.isModMode(event.getPlayer().getUniqueId())) {
            if((event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) || event.getItem() == null || event.getItem().getType() == Material.AIR) return;

            if(event.getItem().getType() == Material.getMaterial(Core.get().getVersion().getYesVanish()) || event.getItem().getType() == Material.getMaterial(Core.get().getVersion().getNoVanish())) {
                if(!moderation.isVanish(event.getPlayer().getUniqueId())) {
                    moderation.setVanish(event.getPlayer().getUniqueId(), true);
                    event.getPlayer().setItemInHand(moderation.modTools(event.getPlayer().getUniqueId())[3]);
                    return;
                }

                moderation.setVanish(event.getPlayer().getUniqueId(), false);
                event.getPlayer().setItemInHand(moderation.modTools(event.getPlayer().getUniqueId())[3]);
            } else if(event.getItem().getType() == Material.COMPASS) {
                final Player randomPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[ThreadLocalRandom.current().nextInt(0, Bukkit.getOnlinePlayers().size())];
                event.getPlayer().teleport(randomPlayer.getLocation().add(0, 1.0, 0));
            }
        }
    }

    @EventHandler
    public void interactEntity(final PlayerInteractEntityEvent event) {
        if(moderation.isModMode(event.getPlayer().getUniqueId()) && event.getRightClicked() instanceof Player) {
            if(event.getPlayer().getItemInHand().getType() == Material.BOOK) {
                event.getPlayer().openInventory(((Player)event.getRightClicked()).getInventory());
            } else if(event.getPlayer().getItemInHand().getType() == Material.PACKED_ICE) {
                event.getPlayer().chat("/freeze "+event.getRightClicked().getName());
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void damage(final EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(moderation.isFrozen(event.getEntity().getUniqueId()) || moderation.isModMode(event.getEntity().getUniqueId()) || moderation.isGod(event.getEntity().getUniqueId())) event.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.HIGH)
    public void damageByEntity(final EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Projectile ? ((Projectile) event.getDamager()).getShooter() instanceof Player : event.getDamager() instanceof Player)) return;
        final Player damager = event.getDamager() instanceof Projectile ? ((Player) ((Projectile) event.getDamager()).getShooter()) : (Player)event.getDamager();
        if(moderation.isFrozen(damager.getUniqueId()) || moderation.isModMode(damager.getUniqueId())) event.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.HIGH)
    public void blockBreak(final BlockBreakEvent event) {
        if(moderation.isModMode(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.HIGH)
    public void blockPlace(final BlockPlaceEvent event) {
        if(moderation.isModMode(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }
    @EventHandler
    public void clickInventory(final InventoryClickEvent event) {
        if(moderation.isFrozen(event.getWhoClicked().getUniqueId()) || moderation.isModMode(event.getWhoClicked().getUniqueId())) event.setCancelled(true);
    }
    @EventHandler
    public void itemConsume(final PlayerItemConsumeEvent event) {
        if(moderation.isFrozen(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }
    @EventHandler
    public void itemDrop(final PlayerDropItemEvent event) {
        if(moderation.isFrozen(event.getPlayer().getUniqueId()) || moderation.isModMode(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }
    @EventHandler
    public void itemPickup(final PlayerPickupItemEvent event) {
        if(moderation.isFrozen(event.getPlayer().getUniqueId()) || moderation.isModMode(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityDamage(final EntityDamageEvent event) {
        if(moderation.isFrozen(event.getEntity().getUniqueId()) || moderation.isModMode(event.getEntity().getUniqueId())) event.setCancelled(true);
    }
}
