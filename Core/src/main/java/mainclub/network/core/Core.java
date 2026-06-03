package mainclub.network.core;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import mainclub.network.core.command.*;
import mainclub.network.core.command.admin.*;
import mainclub.network.core.command.admin.claim.ClaimCommand;
import mainclub.network.core.command.admin.gamemode.argument.AdventureArgument;
import mainclub.network.core.command.admin.gamemode.argument.CreativeArgument;
import mainclub.network.core.command.admin.gamemode.GameModeCommand;
import mainclub.network.core.command.admin.gamemode.argument.SpectatorArgument;
import mainclub.network.core.command.admin.gamemode.argument.SurvivalArgument;
import mainclub.network.core.command.donation.*;
import mainclub.network.core.command.DelHomeCommand;
import mainclub.network.core.command.HomeCommand;
import mainclub.network.core.command.SetHomeCommand;
import mainclub.network.core.command.admin.FreezeCommand;
import mainclub.network.core.command.admin.ModCommand;
import mainclub.network.core.command.admin.VanishCommand;
import mainclub.network.core.command.teleport.AcceptCommand;
import mainclub.network.core.command.teleport.HereCommand;
import mainclub.network.core.command.teleport.IgnoreCommand;
import mainclub.network.core.command.teleport.TeleportCommand;
import mainclub.network.core.configuration.Configuration;
import mainclub.network.core.hook.PlaceholderAPI;
import mainclub.network.core.hook.VaultAPI;
import mainclub.network.core.database.ProfileManager;
import mainclub.network.core.leaderboards.LeaderboardMaker;
import mainclub.network.core.listener.*;
import mainclub.network.core.manager.*;
import mainclub.network.core.utils.Cooldown;
import mainclub.network.core.utils.Moderation;
import mainclub.network.core.utils.RunnableTask;
import mainclub.network.core.command.teleport.ToggleCommand;
import mainclub.network.version.VersionAdapter;
import mainclub.network.version.v1_21;
import mainclub.network.version.v1_8;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {
    public static Core core;
    @Getter private WorldEditPlugin worldEdit;

    @Getter private Configuration configuration;

    //MANAGERS
    @Getter private Cooldown cooldown;
    @Getter private ProfileManager base;
    @Getter private CoreManager manager;
    @Getter private LeaderboardMaker leaderboards;
    @Getter private ClaimManager claims;
    @Getter private SpawnManager spawn;
    @Getter private WarpManager warps;
    @Getter private KitManager kits;
    @Getter private HomeManager homes;
    @Getter private Moderation moderation;

    @Getter private VersionAdapter version;
    @Getter private VaultAPI vault;


    @Override
    public void onEnable() {
        core = this;
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new PlaceholderAPI().register();
        if(getServer().getPluginManager().getPlugin("WorldEdit") != null) worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        else Bukkit.getConsoleSender().sendMessage("[MainCore] WorldEdit dependency not found !");


        configuration = new Configuration();
        configuration.load();

        setupVersion();

        cooldown = new Cooldown();
        base = new ProfileManager();
        manager = new CoreManager();
        leaderboards = new LeaderboardMaker();
        claims = new ClaimManager();
        spawn = new SpawnManager();
        warps = new WarpManager();
        kits = new KitManager();
        homes = new HomeManager();
        moderation = new Moderation();

        vault = new VaultAPI();

        getCommand("maincore").setExecutor(new MainCoreCommand());

        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand());
        getCommand("home").setExecutor(new HomeCommand());

        getCommand("whitelist").setExecutor(new WhitelistCommand());
        getCommand("claim").setExecutor(new ClaimCommand());
        getCommand("world").setExecutor(new WorldCommand());
        getCommand("modmode").setExecutor(new ModCommand());
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("god").setExecutor(new GodCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("mutechat").setExecutor(new MuteChatCommand());
        getCommand("clearchat").setExecutor(new ClearChatCommand());
        getCommand("enchant").setExecutor(new EnchantCommand());
        getCommand("itemname").setExecutor(new ReNameCommand());
        getCommand("nick").setExecutor(new NickCommand());
        getCommand("realnick").setExecutor(new RealNickCommand());
        getCommand("itemlore").setExecutor(new ItemLoreCommand());
        getCommand("repair").setExecutor(new RepairCommand());

        getCommand("gamemode").setExecutor(new GameModeCommand());
        getCommand("gmc").setExecutor(new CreativeArgument());
        getCommand("gms").setExecutor(new SurvivalArgument());
        getCommand("gma").setExecutor(new AdventureArgument());
        getCommand("gmspec").setExecutor(new SpectatorArgument());

        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("craft").setExecutor(new CraftCommand());
        getCommand("enderchest").setExecutor(new EnderChestCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("back").setExecutor(new BackCommand());
        getCommand("kit").setExecutor(new KitCommand());

        /*getCommand("msg").setExecutor(new MessageCommand());
        getCommand("msgtoggle").setExecutor(new ToggleCommand());
        getCommand("reply").setExecutor(new ReplyCommand());
        getCommand("msgignore").setExecutor(new IgnoreCommand());*/

        getCommand("teleport").setExecutor(new TeleportCommand());
        getCommand("tphere").setExecutor(new HereCommand());
        getCommand("tpaccept").setExecutor(new AcceptCommand());
        getCommand("tptoggle").setExecutor(new ToggleCommand());
        getCommand("tpignore").setExecutor(new IgnoreCommand());


        getServer().getPluginManager().registerEvents(new CoreListener(), this);
        getServer().getPluginManager().registerEvents(new ClaimListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new ModerationListener(), this);
        getServer().getPluginManager().registerEvents(new ViewKitListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);

        new RunnableTask();

    }

    @Override
    public void onDisable() {
        configuration.save();
        claims.save();
        base.save();
        Bukkit.getOnlinePlayers().stream().filter(player-> player != null && moderation.isModMode(player.getUniqueId())).forEach(player -> {
            moderation.setModMode(player.getUniqueId(), false);
        });
        /*settingsFile.set("WHITELIST.LIST", configuration.getStringLists().getWhitelist());
        settingsFile.save();
        if (settingsFile.getBoolean("ON-STOP.DELETE-LOGS")) new Folder().delete("logs");
        if (settingsFile.getBoolean("ON-STOP.DELETE-USERCACHE")) {
            new Folder().delete("world");
            new Folder().delete("usercache.json");
        }*/
        core = null;
    }

    public void setupVersion() {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            if(version.contains("v1_8") || version.contains("v1_9") || version.contains("v1_10")|| version.contains("v1_11") || version.contains("v1_12")) {
                this.version = new v1_8();
                Bukkit.getConsoleSender().sendMessage("[ClubCore] Version loaded: v1.8");
            } else {
                this.version = new v1_21();
                Bukkit.getConsoleSender().sendMessage("[ClubCore] Version loaded: v1.21");
            }
        }catch (Exception e) {this.version = new v1_21();}

    }

    /*public void setupExpansions() {
        Bukkit.getConsoleSender().sendMessage("[ClubCore] Searching expansions...");
        File[] files = expansions.listFiles(file -> file.isFile() && file.getName().endsWith(".jar"));

        if (files == null || files.length == 0) {
            Bukkit.getConsoleSender().sendMessage("[ClubCore] No expansions found.");
            return;
        }

        PluginManager pluginManager = getServer().getPluginManager();
        for (File file : files) {
            try {
                Plugin plugin = pluginManager.loadPlugin(file);
                pluginManager.enablePlugin(plugin);
                Bukkit.getConsoleSender().sendMessage("[ClubCore] Loaded expansion: " + file.getName());
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[ClubCore] Catch expansion: " + file.getName());
                e.printStackTrace();
            }
        }
    }*/

    public static Core get(){return core;}
}
