package mainclub.network.version;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.sk89q.worldedit.regions.Region;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class v1_21 implements VersionAdapter {
    private WorldEditPlugin plugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

    private final String yesVanish = "LIME_DYE";
    private final String noVanish = "GRAY_DYE";
    private final String skull = "PLAYER_HEAD";
    final HashMap<Enchantment, List<String>> enchants = new HashMap<>();

    public v1_21() {
        enchants.put(org.bukkit.enchantments.Enchantment.PROTECTION, Arrays.asList("proteccion", "protección","protection", "protection_environmental"));
        enchants.put(org.bukkit.enchantments.Enchantment.UNBREAKING, Arrays.asList("durabilidad","durability","inrrompibilidad", "unbreaking"));
        enchants.put(org.bukkit.enchantments.Enchantment.SHARPNESS, Arrays.asList("filo","sharpness", "damage_all"));
        enchants.put(org.bukkit.enchantments.Enchantment.FIRE_ASPECT, Arrays.asList("fireaspect", "aspectoardiente", "aspecto_ardiente","fire_aspect"));
        enchants.put(Enchantment.FIRE_PROTECTION, Arrays.asList("fireprotection", "fire_protection"));
        enchants.put(Enchantment.BLAST_PROTECTION, Arrays.asList("PROTECTION_EXPLOSIONS", "blastprotection", "blast_protection"));
        enchants.put(Enchantment.PROJECTILE_PROTECTION, Arrays.asList("PROTECTION_PROJECTILE", "projectileprotection", "projectile_protection"));
        enchants.put(Enchantment.RESPIRATION, Arrays.asList("oxygen", "respiration"));
        enchants.put(Enchantment.AQUA_AFFINITY, Arrays.asList("waterworker", "water_worker", "aquainfinity", "aqua_infinity"));
        enchants.put(Enchantment.FEATHER_FALLING, Arrays.asList("falldamage", "fall_damage", "featherfalling", "feather_falling"));
        enchants.put(Enchantment.DEPTH_STRIDER, Arrays.asList("depthstrider", "depth_strider"));
        enchants.put(Enchantment.SOUL_SPEED, Arrays.asList("soulspeed", "soul_speed"));
        enchants.put(Enchantment.SWIFT_SNEAK, Arrays.asList("swiftsneak", "swift_sneak"));
        enchants.put(org.bukkit.enchantments.Enchantment.FORTUNE, Arrays.asList("fortuna","fortune", "suerte", "loot_bonus_blocks"));
        enchants.put(org.bukkit.enchantments.Enchantment.EFFICIENCY, Arrays.asList("eficiencia","efficiency", "dig_speed"));
        enchants.put(org.bukkit.enchantments.Enchantment.POWER, Arrays.asList("poder","power", "arrow_damage"));
        enchants.put(org.bukkit.enchantments.Enchantment.FLAME, Arrays.asList("fuego","flame", "arrow_fire"));
        enchants.put(org.bukkit.enchantments.Enchantment.INFINITY, Arrays.asList("infinidad","infinity", "arrow_infinity"));
        enchants.put(Enchantment.PUNCH, Arrays.asList("golpe", "punch", "arrow_knockback"));
        enchants.put(Enchantment.LOOTING, Arrays.asList("looting", "saqueo", "loot_bonus_mobs"));
        enchants.put(Enchantment.SMITE, Arrays.asList("smite", "castigo", "damage_undead"));
        enchants.put(Enchantment.BANE_OF_ARTHROPODS, Arrays.asList("arthropods", "DAMAGE_ARTHROPODS", "BANE_OF_ARTHROPODS"));
        enchants.put(Enchantment.MENDING, Arrays.asList("mending"));
    }
    public HashMap<Enchantment, List<String>> getEnchantNames() {
        return enchants;
    }

    public void setTabulator(final Player player, final List<String> header, final List<String> footer) {
        //player.setPlayerListHeaderFooter(
        //        color("&#2B99FF&lP&#289FFF&lL&#26A6FF&lA&#23ACFF&lY&#20B3FF&l.&#1EB9FF&lM&#1BBFFF&lA&#18C6FF&lI&#16CCFF&lN&#13D2FF&lC&#10D9FF&lL&#0DDFFF&lU&#0BE6FF&lB&#08ECFF&l.&#05F2FF&lN&#03F9FF&lE&#00FFFF&lT\n&eplayers: &71\n&f"),
        //        color("&f\n&f⤹ &9ranks &f& &aperks&f ⤸\n&b&nwww.mainclub.net/store"));
        String makeHeader = "";
        String makeFooter = "";

        for(int i = 0; i < header.size(); i++) makeHeader += color(header.get(i))+(header.size()-1 == i ? "":"\n");
        for(int i = 0; i < footer.size(); i++) makeFooter += color(footer.get(i))+(footer.size()-1 == i ? "":"\n");

        player.setPlayerListHeaderFooter(makeHeader, makeFooter);
    }
    public String color(final String text) {
        final String[] texts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
        final StringBuilder createText = new StringBuilder();
        for (int i = 0; i < texts.length; ++i) {
            if (!text.contains("#")) return ChatColor.translateAlternateColorCodes('&', text);
            if (texts[i].equals("&")) ++i;

            if (texts[i].startsWith("#")) {
                final ChatColor color = ChatColor.of(texts[i].substring(0, 7));
                createText.append(color + texts[i].substring(7));
                ++i;
            }
            else {
                createText.append("§" + texts[i]);
            }
        }
        return createText.toString();
    }
    public ItemStack getItemInHand(final Player player) {
        return player.getInventory().getItemInHand();
    }
    public ItemStack getItemInOffHand(final Player player) {
        return  player.getInventory().getItemInOffHand();
    }

    public void setPlayerHand(final Player player, final ItemStack itemStack) {player.getInventory().setItemInMainHand(itemStack);}
    public int skullId(){return 0;}
    public void skullOwner(final SkullMeta meta, final String owner) {meta.setOwner(owner);}


    public String getYesVanish() {
        return yesVanish;
    }
    public String getNoVanish() {
        return noVanish;
    }
    public String getSkull() {
        return skull;
    }

    public ItemStack completeItem(final ItemStack itemStack, final String id, final String texture) {
        return itemStack;
    }

    private Region getWorldEditRegion(final Player player) {
        BukkitPlayer wePlayer = BukkitAdapter.adapt(player);

        // sesión local del jugador
        LocalSession session = plugin.getWorldEdit().getSessionManager().get(wePlayer);

        // región seleccionada
        Region region = null;
        try {
            region = session.getSelection(wePlayer.getWorld());
        } catch (Exception e) { player.sendMessage("§eSelect the region-area with WorldEdit Axe.");
        }

        return region;
    }
    public boolean hasWorldEditSelection(final Player player)  {
        return plugin.getSession(player).getRegionSelector(plugin.wrapPlayer(player).getWorld()) != null;
    }

    public Location getFirstWorldEditSelection(final Player player) {
        final Region region = getWorldEditRegion(player);
        return new Location(Bukkit.getWorld(region.getWorld().getName()), region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());
    }
    public Location getLastWorldEditSelection(final Player player) {
        final Region region = getWorldEditRegion(player);
        return new Location(Bukkit.getWorld(region.getWorld().getName()), region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
    }
}
