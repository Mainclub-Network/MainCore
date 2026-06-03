package mainclub.network.core.utils.item;

import mainclub.network.core.Core;
import mainclub.network.version.VersionAdapter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {
    private ItemStack itemStack;
    private String skullID;
    private String skullTexture;
    private ItemMeta itemMeta;
    private VersionAdapter version = Core.get().getVersion();

    private Map<Enchantment, Integer> enchantments = new HashMap<>();

    public Item(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public Item(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }
    public Material getType(){
        return itemStack.getType();
    }
    public String getSkullID() {
        return skullID;
    }
    public String getSkullTexture() {
        return skullTexture;
    }

    public Item setOwner(String name) {
        SkullMeta meta = (SkullMeta) itemMeta;
        meta.setOwner(name);
        return this;
    }

    public Item setHeadTexture(String id, String texture) {
        skullID = id;
        skullTexture = texture;
        return this;
    }


    public Item setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public Item setDisplay(String name) {
        if(name != null) itemMeta.setDisplayName(version.color(name));
        return this;
    }

    public Item setDurability(int durability) {
        itemStack.setDurability((short) durability);
        return this;
    }

    public Item addLore(String lore) {
        Object object = itemMeta.getLore();
        if (object == null) object = new ArrayList<>();

        ((List) object).add(version.color(lore));
        itemMeta.setLore((List<String>) object);
        return this;
    }

    public Item addLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    public Item addLore(String... lore) {
        List<String> strings = new ArrayList<>();
        for (String string : lore) {
            strings.add(version.color(string));
        }
        itemMeta.setLore(strings);
        return this;
    }

    public Item setEnchant(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public Item setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }
    public Item setLore(List<String> lore) {
        if (itemMeta == null) itemMeta = itemStack.getItemMeta();
        if(itemStack.getType() == Material.BARRIER){
            if(lore == null) lore = new ArrayList<>();
            lore.add("");
            lore.add("§cItem(material-name) not found.");
            lore.add("§cChange it in the respective file(yml).");
        }
        itemMeta.setLore(lore);
        return this;
    }
    public Item setColor(Color color) {
        if (itemStack.getType() != null && itemStack.getType().name().contains("LEATHER")) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) itemMeta;
            armorMeta.setColor(color);
        }
        return this;
    }

    public ItemStack create() {
        if (itemMeta != null) {
            itemStack.setItemMeta(itemMeta);
        }

        enchantments.forEach((enchantment, integer) -> itemStack.addUnsafeEnchantment(enchantment, integer));
        return version.completeItem(itemStack, skullID, skullTexture);
    }
}