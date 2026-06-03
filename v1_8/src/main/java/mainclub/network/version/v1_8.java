package mainclub.network.version;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class v1_8 implements VersionAdapter {
    final HashMap<Enchantment, List<String>> enchants = new HashMap<>();

    public v1_8() {
        enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, Arrays.asList("proteccion", "protección","protection", "protection_environmental"));
        enchants.put(Enchantment.DURABILITY, Arrays.asList("durabilidad","durability","inrrompibilidad", "unbreaking"));
        enchants.put(Enchantment.DAMAGE_ALL, Arrays.asList("filo","sharpness", "damage_all"));
        enchants.put(Enchantment.FIRE_ASPECT, Arrays.asList("fireaspect", "aspectoardiente", "aspecto_ardiente","fire_aspect"));
        enchants.put(Enchantment.PROTECTION_FIRE, Arrays.asList("fireprotection", "fire_protection"));
        enchants.put(Enchantment.PROTECTION_EXPLOSIONS, Arrays.asList("blastprotection", "blast_protection"));
        enchants.put(Enchantment.PROTECTION_PROJECTILE, Arrays.asList("projectileprotection", "projectile_protection"));
        enchants.put(Enchantment.OXYGEN, Arrays.asList("oxygen", "respiration"));
        enchants.put(Enchantment.WATER_WORKER, Arrays.asList("waterworker", "water_worker", "aquainfinity", "aqua_infinity"));
        enchants.put(Enchantment.PROTECTION_FALL, Arrays.asList("falldamage", "fall_damage", "featherfalling", "feather_falling"));
        enchants.put(Enchantment.DEPTH_STRIDER, Arrays.asList("depthstrider", "depth_strider"));
        enchants.put(Enchantment.LOOT_BONUS_BLOCKS, Arrays.asList("fortuna","fortune", "suerte", "loot_bonus_blocks"));
        enchants.put(Enchantment.DIG_SPEED, Arrays.asList("eficiencia","efficiency", "dig_speed"));
        enchants.put(Enchantment.ARROW_DAMAGE, Arrays.asList("poder","power", "arrow_damage"));
        enchants.put(Enchantment.ARROW_FIRE, Arrays.asList("fuego","flame", "arrow_fire"));
        enchants.put(Enchantment.ARROW_INFINITE, Arrays.asList("infinidad","infinity", "arrow_infinity"));
        enchants.put(Enchantment.ARROW_KNOCKBACK, Arrays.asList("golpe", "punch", "arrow_knockback"));
        enchants.put(Enchantment.LOOT_BONUS_MOBS, Arrays.asList("looting", "saqueo"));
        enchants.put(Enchantment.DAMAGE_UNDEAD, Arrays.asList("smite", "castigo"));
        enchants.put(Enchantment.DAMAGE_ARTHROPODS, Arrays.asList("arthropods", "DAMAGE_ARTHROPODS", "BANE_OF_ARTHROPODS"));
    }

    public HashMap<Enchantment, List<String>> getEnchantNames() {
        return enchants;
    }

    //@Getter @Setter static double h = 0.48, v = 0.34,f = 1.9, yLimit = 0.40;
    private final String yesVanish = "INK_SACK";
    private final String noVanish = "INK_SACK";
    private final String skull = "SKULL_ITEM";

    public ItemStack getItemInHand(final Player player) {return player.getItemInHand();}
    public ItemStack getItemInOffHand(final Player player) {return new ItemStack(Material.AIR);}
    public void setPlayerHand(final Player player, final ItemStack itemStack) {player.setItemInHand(itemStack);}
    @Override
    public String color(final String text) {return ChatColor.translateAlternateColorCodes('&', text);}
    public int skullId(){return 3;}
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

    public void setTabulator(final Player player, final List<String> header, final List<String> footer) {
        //final List<String> head = Arrays.asList("&3&lPLAY.MAINCLUB.NET", "&fplayers: &71", "");
        //final List<String> foot = Arrays.asList("", "&f⤹ &9ranks &f& &aperks&f ⤸", "&b&nwww.mainclub.net/store");

        String makeHeader = "";
        String makeFooter = "";

        for(int i = 0; i < header.size(); i++) makeHeader += ChatColor.translateAlternateColorCodes('&', header.get(i))+(header.size()-1 == i ? "":"\n");
        for(int i = 0; i < footer.size(); i++) makeFooter += ChatColor.translateAlternateColorCodes('&', footer.get(i))+(footer.size()-1 == i ? "":"\n");


        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
            Method aMethod = chatSerializerClass.getMethod("a", String.class);

            String headerText = makeHeader.replace("\\", "\\\\").replace("\"", "\\\"");
            String footerText = makeFooter.replace("\\", "\\\\").replace("\"", "\\\"");

            String headerJson = "{\"text\":\"" + headerText + "\"}";
            String footerJson = "{\"text\":\"" + footerText + "\"}";

            Object head = aMethod.invoke(null, headerJson);
            Object foot = aMethod.invoke(null, footerJson);


            Class<?> packetClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutPlayerListHeaderFooter");
            Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = craftPlayer.getClass().getField("playerConnection").get(craftPlayer);
            Object packet = packetClass.newInstance();

            Field headerField = packetClass.getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, head);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packetClass.getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, foot);
            footerField.setAccessible(!footerField.isAccessible());


            connection.getClass()
                    .getMethod("sendPacket", Class.forName("net.minecraft.server."+version+".Packet"))
                    .invoke(connection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ItemStack completeItem(final ItemStack itemStack, final String id, final String texture) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbt = nmsStack.getTag();

        if (nbt == null) {
            nbt = new NBTTagCompound();
        }


        nmsStack.setTag(nbt);

        if (id != null) {
            net.minecraft.server.v1_8_R3.NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new net.minecraft.server.v1_8_R3.NBTTagCompound();
            net.minecraft.server.v1_8_R3.NBTTagCompound skullOwnerCompound = new net.minecraft.server.v1_8_R3.NBTTagCompound();
            net.minecraft.server.v1_8_R3.NBTTagCompound propiedades = new net.minecraft.server.v1_8_R3.NBTTagCompound();
            net.minecraft.server.v1_8_R3.NBTTagList texturas = new net.minecraft.server.v1_8_R3.NBTTagList();
            net.minecraft.server.v1_8_R3.NBTTagCompound texturasObjeto = new net.minecraft.server.v1_8_R3.NBTTagCompound();
            texturasObjeto.setString("Value", texture);
            texturas.add(texturasObjeto);
            propiedades.set("textures", texturas);
            skullOwnerCompound.set("Properties", propiedades);
            skullOwnerCompound.setString("Id", id);
            tag.set("SkullOwner", skullOwnerCompound);
            nmsStack.setTag(tag);
        }
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static Region getWorldEditRegion(final WorldEditPlugin plugin, final Player player) throws IncompleteRegionException {
        final Selection selection = plugin.getSelection(player);

        return selection == null ? null : selection.getRegionSelector().getRegion();
    }

    public static boolean hasWorldEditSelection(final WorldEditPlugin plugin, final Player player) {
        final Selection selection = plugin.getSelection(player);
        return selection.getRegionSelector().getIncompleteRegion() != null;
    }


        /*
         public Region getWorldEditRegion(final WorldEditPlugin plugin, final Player player) throws IncompleteRegionException {
             return Main.getWorldEditRegion(plugin, player);
          }

        public boolean hasWorldEditSelection(final WorldEditPlugin plugin, final Player player) {
            return Main.hasWorldEditSelection(plugin, player);
            }
        */



    /*public static void setKnockback(Player attacker, final Player victim) {
        Vector direction = victim.getLocation().toVector()
                .subtract(attacker.getLocation().toVector());

        direction.setY(0);

        if (direction.lengthSquared() == 0) return;

        direction.normalize();

        // ⚙️ 3. Knockback base
        double horizontal = h;
        double vertical = victim.isOnGround() ? v : 30;
        double friction = f;

        Vector velocity = victim.getVelocity().clone();

        velocity.setX(velocity.getX() / friction);
        velocity.setY(velocity.getY() / friction);
        velocity.setZ(velocity.getZ() / friction);

        velocity.setX(velocity.getX() - direction.getX() * horizontal);
        velocity.setZ(velocity.getZ() - direction.getZ() * horizontal);
        velocity.setY(velocity.getY() + vertical);

        //y limit
        if (velocity.getY() > yLimit) {
            velocity.setY(yLimit);
        }

        // CTRL first hit punch
        if (attacker.isSprinting()) {
            velocity.setX(velocity.getX() * 1.1);
            velocity.setZ(velocity.getZ() * 1.1);
            attacker.setSprinting(false);
        }

        // randomize - no igual
        //double random = 0.97 + (Math.random() * 0.6);
        //velocity.multiply(random);
        //velocity.setX(velocity.getX() * random);
        //velocity.setZ(velocity.getZ() * random);


        // Aplicar + packet
        //victim.setVelocity(velocity);

        CraftPlayer cp = (CraftPlayer) victim;
        EntityPlayer ep = cp.getHandle();

        PacketPlayOutEntityVelocity packet = new PacketPlayOutEntityVelocity(
                victim.getEntityId(),
                velocity.getX(),
                velocity.getY(),
                velocity.getZ()
        );

        ep.playerConnection.sendPacket(packet);
    }
    */
}
