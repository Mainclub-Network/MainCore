package mainclub.network.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class Inventory {
    private org.bukkit.inventory.Inventory inventory;
    private String title;
    private int size;

    public Inventory(final String title, final int size) {
        this.title = title;
        this.size = size;

        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public Inventory setItem(final int size, final ItemStack item) {
        inventory.setItem(size, item);
        return this;
    }

    public org.bukkit.inventory.Inventory load() {
        return inventory;
    }

}
