package mainclub.network.core.manager;

import mainclub.network.core.Core;
import mainclub.network.core.database.Profile;
import mainclub.network.core.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class MenusManager {

    public Inventory getStats(final Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Stats");

        final Profile profile = Core.get().getBase().get(player.getUniqueId());

        final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName("§a"+player.getName()+ "§7 - §e#"+profile.getJoinNumber());
        meta.setOwningPlayer(player);

        meta.setLore(Arrays.asList("§7"+profile.getJoinDate(), "", "§f Kills: §a"+profile.getKills(), "§f Streak: §a"+profile.getStreak(), "§f MaxStreak: §a"+profile.getMaxStreak(), "", "§f Deaths: §a"+profile.getDeaths(), "", "§f Rank: §a"+Core.get().getVault().permission().getPrimaryGroup(player).replace("&","§"), "§f Playtime: §a"+new TimeUtil(profile.getTimePlayed()).format(), ""));
        head.setItemMeta(meta);

        inv.setItem(13, head);

        return inv;
    }
}
