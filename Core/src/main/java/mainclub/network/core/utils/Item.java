package mainclub.network.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Item {
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private LeatherArmorMeta colorMeta;


    public Item(final Material material, final int amount, final int data) {
        itemStack = new ItemStack(material, amount, (byte)data);
        itemMeta = itemStack.getItemMeta();
    }
    public Item display(final String display) {
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
        return this;
    }
    public Item setColor(final Color color) {
        colorMeta = (LeatherArmorMeta) itemMeta;
        colorMeta.setColor(color);
        itemStack.setItemMeta(colorMeta);
        return this;
    }
    public ItemStack load() {
        itemStack.setItemMeta(colorMeta != null ? colorMeta : itemMeta);
        return itemStack;
    }
}
