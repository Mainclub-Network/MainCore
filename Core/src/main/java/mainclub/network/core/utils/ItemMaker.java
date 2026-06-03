package mainclub.network.core.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemMaker {
    private ItemStack stack;
    private ItemMeta meta;

    public ItemMaker(final Material material) {
        this(material == null ? Material.BARRIER : material, 1);
    }

    public ItemMaker(final Material material, final int amount) {
        this(material == null ? Material.BARRIER : material, amount, (short)0);
    }

    public ItemMaker(final ItemStack stack) {
        if(stack == null) {
            this.stack = new ItemStack(Material.BARRIER);
        } else {
            //Preconditions.checkNotNull((Object) stack, "ItemStack cannot be null");
            this.stack = stack;
        }
    }

    public ItemMaker(final Material material, final int amount, final short data) {
        if(material == null) {
            this.stack = new ItemStack(Material.BARRIER, amount <= 0 ? 1 : amount, data);
        } else {
            //Preconditions.checkNotNull((Object) stack, "ItemStack cannot be null");
            this.stack = new ItemStack(material, amount <= 0 ? 1 : amount, data);
        }
    }

    public ItemMaker displayName(final String name) {
        if (meta == null) meta = stack.getItemMeta();
        if(stack.getType() == Material.BARRIER) meta.setDisplayName("§cError: Verify item name");

        meta.setDisplayName(name);
        return this;
    }

    public ItemMaker loreLine(final String line, final int i) {
        if (meta == null) {
            meta = stack.getItemMeta();
        }
        final boolean hasLore = meta.hasLore();
        final List<String> lore = hasLore ? meta.getLore() : new ArrayList<>();
        lore.add(hasLore ? lore.size() : 0, line);
        //lore(line);
        return this;
    }

    public ItemMaker lore(final List<String> lore) {
        if (meta == null) meta = stack.getItemMeta();
        if(stack.getType() == Material.BARRIER){
            lore.add("");
            lore.add("§cItem materialName not found.");
            lore.add("§cChange it in the respective file(yml).");
        }
        meta.setLore(lore);
        return this;
    }

    public ItemMaker enchant(final Enchantment enchantment, final int level) {
        return enchant(enchantment, level, true);
    }

    public ItemMaker enchant(final Enchantment enchantment, final int level, final boolean unsafe) {
        if (unsafe && level >= enchantment.getMaxLevel()) {
            stack.addUnsafeEnchantment(enchantment, level);
        }
        else {
            stack.addEnchantment(enchantment, level);
        }
        return this;
    }

    public ItemMaker amount(final int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemMaker data(final short data) {
        stack.setDurability(data);
        return this;
    }

    public ItemStack load() {
        if (meta != null) {
            stack.setItemMeta(meta);
        }
        return stack;
    }
}
