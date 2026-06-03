package mainclub.network.core.hook;

import mainclub.network.core.Core;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAPI {
    private final Core main = Core.get();
    private Economy econ = null;
    private Chat chat = null;
    private Permission permission = null;

    public VaultAPI() {
        setupEconomy();
        setupChat();
        setupPermission();
    }
    public boolean setupEconomy() {
        if (main.getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getConsoleSender().sendMessage("[MainCore] Vault dependency not found !");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = main.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Bukkit.getConsoleSender().sendMessage("[MainCore] Economy from Vault not found !");
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = main.getServer().getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null) {
            this.chat = chatProvider.getProvider();
        } else Bukkit.getConsoleSender().sendMessage("[MainCore] Chat from Vault not found !");

        return this.chat != null;
    }
    public boolean setupPermission() {
        final RegisteredServiceProvider<Permission> permissionProvider = main.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            this.permission = permissionProvider.getProvider();
        }// else Bukkit.getConsoleSender().sendMessage("[ClubCore] Permissions from Vault not found !");

        return this.permission != null;
    }
    public Economy economy() {
        return econ;
    }
    public Chat chat() {
        return chat;
    }
    public Permission permission() {return permission;}
}
