package mainclub.network.core.configuration.context;

import lombok.Getter;
import mainclub.network.core.Core;
import mainclub.network.core.configuration.Configuration;
import mainclub.network.core.configuration.file.SettingsFile;

public class Strings {
    @Getter
    private String serverName;
    @Getter
    private String chatFormat;
    @Getter
    private String serverRestartTimeZone;
    @Getter
    private String whitelistText;

    public Strings() {load();}

    public void load() {
        final Configuration configuration = Core.get().getConfiguration();

        serverName = configuration.getSettingsFile().getString("SERVER_NAME");
        chatFormat = configuration.getSettingsFile().getString("CHAT_FORMAT");
        serverRestartTimeZone = configuration.getSettingsFile().getString("SERVER_RESTART.TIME-ZONE");
        whitelistText = configuration.getSettingsFile().getString("WHITELIST.KICK_MESSAGE");
    }
}