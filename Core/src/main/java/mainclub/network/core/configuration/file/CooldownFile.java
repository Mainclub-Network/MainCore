package mainclub.network.core.configuration.file;

import mainclub.network.core.Core;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CooldownFile extends YamlConfiguration {
    private final File config = new File(Core.get().getDataFolder(), "cooldowns.yml");

    public CooldownFile() {
        if (!config.exists()) {
            Core.get().saveResource("cooldowns.yml", false);
        }
        reload();
    }

    public void reload() {
        try {
            super.load(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}