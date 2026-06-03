package mainclub.network.core.manager;

import mainclub.network.core.Core;
import mainclub.network.core.configuration.Configuration;
import mainclub.network.core.configuration.file.CooldownFile;
import mainclub.network.core.configuration.file.KitsFile;
import mainclub.network.core.utils.Cooldown;
import mainclub.network.core.utils.item.Item;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KitManager {
    private final Core main = Core.get();
    private final Configuration configuration = main.getConfiguration();
    private final Cooldown cooldowns = main.getCooldown();

    public KitManager() {
        if(!configuration.getKitsFile().contains("KITS")) return;
        if(configuration.getKitsFile().getConfigurationSection("KITS") == null) return;
        if(configuration.getKitsFile().getConfigurationSection("KITS").getKeys(false).isEmpty()) return;

        createKitsCooldowns();
        createPlayersKitCooldown();
    }

    public void createKit(final String kit, final int cooldown, final Inventory inventory) {
        List<String> add = new ArrayList<>();

        for(ItemStack items : inventory.getContents()) {
            if(items == null || items.getType() == Material.AIR) continue;

            List<String> lore = new ArrayList<>();
            if(items.getItemMeta().hasLore()) lore = items.getItemMeta().getLore();

            List<String> enchants = new ArrayList<>();
            if(!items.getItemMeta().getEnchants().isEmpty()) {
                for (Map.Entry<Enchantment, Integer> enchant : items.getEnchantments().entrySet()) enchants.add(enchant.getKey().getName() + ":" + enchant.getValue());
            }
            //return add.toString().replace("[", "").replace("]", "").replace(",", "");


            if((items.getType().toString().equalsIgnoreCase("SKULL_ITEM") || items.getType().toString().equalsIgnoreCase("PLAYER_HEAD")) && items.getDurability() == 3) {
                Item skull = new Item(items);
                add.add(
                        items.getType()+";"+
                                "skull_data: "+skull.getSkullID()+":"+skull.getSkullTexture()+";"+
                                items.getDurability() +";"+
                                items.getAmount() +";"+
                                (items.getItemMeta().hasDisplayName() ? items.getItemMeta().getDisplayName().replace(" ", "_") : "null") + ";"+
                                "enchants: "+ enchants +";"+
                                "lore: "+ lore);
                continue;
            }

            add.add(items.getType()+";"+
                    items.getDurability() +";"+
                    items.getAmount() +";"+
                    (items.getItemMeta().hasDisplayName() ? items.getItemMeta().getDisplayName().replace(" ", "_") : "null") + ";"+
                    "enchants: "+enchants +";"+
                    "lore: "+ lore);
        }

        configuration.getKitsFile().set("KITS."+kit+".COOLDOWN", cooldown);
        configuration.getKitsFile().set("KITS."+kit+".INVENTORY", add);

        cooldowns.createCooldown(kit);
        configuration.getKitsFile().save();
    }

    public void deleteKit(final String kit) {
        configuration.getKitsFile().set("KITS."+equalsIgnoreCaseKit(kit), null);

        cooldowns.deleteCooldown(kit);
        configuration.getKitsFile().save();
    }
    public boolean isKit(final String kit) {
        if(!configuration.getKitsFile().contains("KITS")) return false;
        return configuration.getKitsFile().contains("KITS."+equalsIgnoreCaseKit(kit));
    }
    public String equalsIgnoreCaseKit(final String kit) {
        if(!configuration.getKitsFile().contains("KITS") || configuration.getKitsFile().getConfigurationSection("KITS") == null || configuration.getKitsFile().getConfigurationSection("KITS").getKeys(false).isEmpty()) return null;
        for(String kits : configuration.getKitsFile().getConfigurationSection("KITS.").getKeys(false)) {
            if(kits.equalsIgnoreCase(kit)) {
                return kits;
            }
        }
        return null;
    }
    public String getKitsList(final Player player) {

        String kits = "";
        if(configuration.getKitsFile().getConfigurationSection("KITS") != null) {
            for(String kit : configuration.getKitsFile().getConfigurationSection("KITS.").getKeys(false)) {
                final String kitToColor = player.hasPermission("core.kit."+kit) ? "§a"+kit+"§f" : "§c"+kit+"§f";
                kits += (kits.equals("") ? kitToColor : ", "+kitToColor);
            }
        }
        return kits;
    }
    public void addCooldownKit(final Player player, final String kit) {
        cooldowns.addCooldown(kit, player.getName(), configuration.getKitsFile().getInt("KITS."+kit+".COOLDOWN"));
    }
    public boolean isCooldownKit(final Player player, final String kit) {
        return cooldowns.isOnCooldown(kit, player.getName());
    }
    public String getPlayerCooldownKit(Player player, String kit) {
        return String.valueOf(new StringBuilder().append(DurationFormatUtils.formatDurationWords(cooldowns.getCooldownForPlayerLong(kit, player.getName()), true, true)))
                .replace(" seconds", "s").replace(" second", "s").replace(" minutes", "m").replace(" minute", "m").replace(" hours", "h").replace(" hour", "h").replace(" days", "d").replace(" day", "d");
    }

    public void sendPlayerKit(Player player, String kit) {
        for(String items : configuration.getKitsFile().getStringList("KITS."+kit+".INVENTORY")) {
            String[] value = items.split(";");

            Item itemMaking = new Item(Material.getMaterial(value[0]));
            List<String> enchants;
            List<String> lore;


            if((itemMaking.getType() == Material.getMaterial("SKULL_ITEM") || itemMaking.getType() == Material.getMaterial("PLAYER_HEAD")) && items.contains("skull_data")) {
                itemMaking.setDurability(Integer.valueOf(value[2]));
                itemMaking.setAmount(Integer.valueOf(value[3]));
                itemMaking.setDisplay(value[4].equals("null") ? null : value[4].replace("_", " "));
                final String id = value[1].split(":")[1].substring(1);
                final String texture = value[1].split(":")[2];
                itemMaking.setHeadTexture(id, texture);


                List<String> enchantList = Arrays.asList(value[5].replaceFirst("enchants:\\s*\\[", "").replaceFirst("]$", "").split(",\\s*"));
                enchants = enchantList.stream().map(s -> s.replaceAll("^\"|\"$", "")).toList();

                List<String> loreList = Arrays.asList(value[6].replaceFirst("lore:\\s*\\[", "").replaceFirst("]$", "").split(",\\s*"));
                loreList = loreList.stream().map(s -> s.replaceAll("^\"|\"$", "")).toList();
                lore = loreList;


            } else {

                itemMaking.setDurability(Integer.valueOf(value[1]));
                itemMaking.setAmount(Integer.valueOf(value[2]));
                itemMaking.setDisplay(value[3].equals("null") ? null : value[3].replace("_", " "));


                List<String> enchantList = Arrays.asList(value[4].replaceFirst("enchants:\\s*\\[", "").replaceFirst("]$", "").split(",\\s*"));
                enchants = enchantList.stream().map(s -> s.replaceAll("^\"|\"$", "")).toList();

                List<String> loreList = Arrays.asList(value[5].replaceFirst("lore:\\s*\\[", "").replaceFirst("]$", "").split(",\\s*"));
                loreList = loreList.stream().map(s -> s.replaceAll("^\"|\"$", "")).toList();
                lore = loreList;
            }


            if(!enchants.isEmpty() && !enchants.get(0).isEmpty()) enchants.forEach(key -> itemMaking.setEnchant(Enchantment.getByName(key.split(":")[0]), Integer.valueOf(key.split(":")[1])));
            if(!lore.isEmpty() && !lore.get(0).isEmpty()) itemMaking.setLore(lore);

            if (player.getInventory().firstEmpty() == -1) player.getWorld().dropItem(player.getLocation(), itemMaking.create());
            else player.getInventory().addItem(itemMaking.create());
        }
    }

    public void viewKit(final Player player, final String kit) {
        final Inventory inventory = Bukkit.createInventory(null, 45, kit.toUpperCase()+" KIT");

        for(String items : configuration.getKitsFile().getStringList("KITS."+kit+".INVENTORY")) {
            String[] value = items.split(";");

            Item itemMaking = new Item(Material.getMaterial(value[0]));
            List<String> enchants;
            List<String> lore;


            if((itemMaking.getType() == Material.getMaterial("SKULL_ITEM") || itemMaking.getType() == Material.getMaterial("PLAYER_HEAD")) && items.contains("skull_data")) {
                itemMaking.setDurability(Integer.valueOf(value[2]));
                itemMaking.setAmount(Integer.valueOf(value[3]));
                itemMaking.setDisplay(value[4].equals("null") ? null : value[4].replace("_", " "));
                final String id = value[1].split(":")[1].substring(1);
                final String texture = value[1].split(":")[2];
                itemMaking.setHeadTexture(id, texture);


                List<String> enchantList = Arrays.asList(value[5].replaceFirst("enchants:\\s*\\[", "").replaceFirst("]$", "").split(",\\s*"));
                enchants = enchantList.stream().map(s -> s.replaceAll("^\"|\"$", "")).toList();

                List<String> loreList = Arrays.asList(value[6].replaceFirst("lore:\\s*\\[", "").replaceFirst("]$", "").split(",\\s*"));
                loreList = loreList.stream().map(s -> s.replaceAll("^\"|\"$", "")).toList();
                lore = loreList;


            } else {

                itemMaking.setDurability(Integer.valueOf(value[1]));
                itemMaking.setAmount(Integer.valueOf(value[2]));
                itemMaking.setDisplay(value[3].equals("null") ? null : value[3].replace("_", " "));


                List<String> enchantList = Arrays.asList(value[4].replaceFirst("enchants:\\s*\\[", "").replaceFirst("]$", "").split(",\\s*"));
                enchants = enchantList.stream().map(s -> s.replaceAll("^\"|\"$", "")).toList();

                List<String> loreList = Arrays.asList(value[5].replaceFirst("lore:\\s*\\[", "").replaceFirst("]$", "").split(",\\s*"));
                loreList = loreList.stream().map(s -> s.replaceAll("^\"|\"$", "")).toList();
                lore = loreList;
            }


            if(!enchants.isEmpty() && !enchants.get(0).isEmpty()) enchants.forEach(key -> itemMaking.setEnchant(Enchantment.getByName(key.split(":")[0]), Integer.valueOf(key.split(":")[1])));
            if(!lore.isEmpty() && !lore.get(0).isEmpty()) itemMaking.setLore(lore);

            inventory.addItem(itemMaking.create());
        }
        inventory.setItem(44, new Item(Material.ARROW).setDisplay("&7Atras").create());
        player.openInventory(inventory);
    }

    private void createKitsCooldowns() {
        for(String kits : configuration.getKitsFile().getConfigurationSection("KITS.").getKeys(false)) cooldowns.createCooldown(kits);
    }
    public void createPlayersKitCooldown() {
        if(!configuration.getCooldownFile().contains("COOLDOWNS")  || configuration.getCooldownFile().getConfigurationSection("COOLDOWNS") == null || configuration.getCooldownFile().getConfigurationSection("COOLDOWNS").getKeys(false).isEmpty()) return;

        configuration.getCooldownFile().getConfigurationSection("COOLDOWNS.").getKeys(false).forEach(player -> {
            configuration.getCooldownFile().getConfigurationSection("COOLDOWNS."+player).getKeys(false).forEach(kit -> {
                if(cooldowns.isCooldown(kit)) {
                    cooldowns.addCooldown(kit, player, configuration.getCooldownFile().getInt("COOLDOWNS."+player+"."+kit));
                   // Bukkit.getConsoleSender().sendMessage(kit+"-"+player);
                }
            });
        });
        /*
        for(String player : cooldownFile.getConfigurationSection("COOLDOWNS").getKeys(false)) {
            for(String kit : cooldownFile.getConfigurationSection("COOLDOWNS."+player).getKeys(false)) {
                if(cooldowns.isCooldown(kit)) {
                    cooldowns.addCooldown(kit, Bukkit.getPlayer(player), cooldownFile.getInt("COOLDOWNS."+player+"."+kit));
                } else {
                    cooldownFile.set("COOLDOWNS."+player+"."+kit, null);
                }
            }
        }*/
    }
    public void savePlayerKitCooldown() {
        configuration.getCooldownFile().set("COOLDOWNS", null);

        cooldowns.hashMapCooldown().forEach((cooldown,mapValues)-> {
            if(isKit(cooldown)) {
                mapValues.entrySet().forEach(value -> {
                    configuration.getCooldownFile().set("COOLDOWNS." + value.getKey() + "." + cooldown, cooldowns.getCooldownForPlayerInt(cooldown, value.getKey()));
                });
            }
        });
       /* for(UUID players : cooldowns.hashMapCooldown().get(kits).keySet()) {
        for(String kits : kitsFile.getConfigurationSection("KITS.").getKeys(false)) {
                if(cooldowns.isOnCooldown(kits, Bukkit.getPlayer(players))) {
                    cooldownFile.set("COOLDOWNS."+players+"."+kits, cooldowns.getCooldownForPlayerInt(kits, Bukkit.getPlayer(players)));
                }
            }
        }

        for(String player : cooldownFile.getConfigurationSection("COOLDOWNS.").getKeys(false)) {
            for(String kits : kitsFile.getConfigurationSection("KITS.").getKeys(false)) {
                if(!cooldowns.isOnCooldown(kits, Bukkit.getPlayer(player))) {
                    cooldownFile.set("COOLDOWNS."+player+"."+kits, null);
                }
            }
        }*/

        configuration.getCooldownFile().save();
    }


    private void addItemEnchants(String items, ItemStack item) {
        if(items.contains("enchants:")) {
            String[] split = items.split(" ");
            for (int enchants = 1; enchants < split.length; ++enchants) {
                String[] a = split[enchants].split(":");

                if(Integer.valueOf(a[1]) > Enchantment.getByName(a[0]).getMaxLevel()) {
                    item.addUnsafeEnchantment(Enchantment.getByName(a[0]), Integer.valueOf(a[1]));
                } else {
                    item.addEnchantment(Enchantment.getByName(a[0]), Integer.valueOf(a[1]));
                }
            }
        }
    }


}
