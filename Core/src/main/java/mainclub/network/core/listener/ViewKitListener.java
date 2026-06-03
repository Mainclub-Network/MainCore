package mainclub.network.core.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ViewKitListener implements Listener {

    @EventHandler
    public void click(final InventoryClickEvent event) {
        if(event.getView().getTitle().equals("Stats")) {
            event.setCancelled(true);
            return;
        }
        if (event.getView().getTitle().endsWith("KIT")) {
            event.setCancelled(true);
            if(event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) return;
            if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Atras")) ((Player)event.getWhoClicked()).chat("/kit");
        }
    }
}
