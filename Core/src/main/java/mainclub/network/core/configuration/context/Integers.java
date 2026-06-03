package mainclub.network.core.configuration.context;

import lombok.Getter;
import mainclub.network.core.Core;
import mainclub.network.core.configuration.Configuration;
import mainclub.network.core.configuration.file.SettingsFile;

public class Integers {
    @Getter private int serverRestartHour;

    public Integers() {load();}

    public void load() {
        final Configuration configuration = Core.get().getConfiguration();

        serverRestartHour = configuration.getSettingsFile().getInt("SERVER_RESTART.TIME");
    }
}
