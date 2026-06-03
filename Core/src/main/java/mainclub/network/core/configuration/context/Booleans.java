package mainclub.network.core.configuration.context;

import lombok.Getter;
import lombok.Setter;
import mainclub.network.core.Core;
import mainclub.network.core.configuration.Configuration;
import mainclub.network.core.configuration.file.SettingsFile;

public class Booleans {
    @Getter private boolean serverRestart;
    @Getter private boolean hearthTag;
    @Getter private boolean tabTag;
    @Getter private boolean serverAnnouncements;
    @Getter @Setter private boolean whitelist;

    public Booleans() {load();}

    public void load() {
        final Configuration configuration = Core.get().getConfiguration();

        serverRestart = configuration.getSettingsFile().getBoolean("SERVER_RESTART.ENABLE");
        whitelist = configuration.getSettingsFile().getBoolean("WHITELIST.ENABLE");
        tabTag = configuration.getSettingsFile().getBoolean("TAB_TAG");
        hearthTag = configuration.getSettingsFile().getBoolean("HEARTH_TAG");
    }

    public void save() {
        final Configuration configuration = Core.get().getConfiguration();

        configuration.getSettingsFile().set("SERVER_RESTART.ENABLE", serverRestart);
        configuration.getSettingsFile().set("WHITELIST.ENABLE", whitelist);

        configuration.getSettingsFile().save();

    }
}
