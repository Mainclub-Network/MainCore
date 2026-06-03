package mainclub.network.core.configuration.file;

import mainclub.network.core.Core;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SettingsFile extends YamlConfiguration {
    private final File config = new File(Core.get().getDataFolder(), "settings.yml");

    public SettingsFile() {
        if (!config.exists()) {
            Core.get().saveResource("settings.yml", false);
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