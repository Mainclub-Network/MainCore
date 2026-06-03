package mainclub.network.version;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;

public interface VersionAdapter {
    default void setTabulator(final Player player, final List<String> header, final List<String> footer) {}
    default ItemStack getItemInHand(final Player player) {
        return null;
    }
    default ItemStack getItemInOffHand(final Player player) {return null;}
    default void setPlayerHand(final Player player, final ItemStack itemStack) {}
    default String color(final String text){ return text;}
    default int skullId(){return 0;}
    default void skullOwner(final SkullMeta meta, final String owner){}
    default String getYesVanish() {return null;}
    default String getNoVanish() {return null;}
    default String getSkull() {return null;}

    default boolean hasWorldEditSelection(final Player player) {return false;}
    default Location getFirstWorldEditSelection(final Player player) {return null;}
    default Location getLastWorldEditSelection(final Player player) {return null;}

    default int ScoreboardLinesSize() {return 16;}
    default ItemStack completeItem(final ItemStack itemStack, final String id, final String texture) {return null;}

    default HashMap<Enchantment, List<String>> getEnchantNames() {return null;}
}
