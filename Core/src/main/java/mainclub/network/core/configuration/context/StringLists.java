package mainclub.network.core.configuration.context;

import lombok.Getter;
import mainclub.network.core.Core;
import mainclub.network.core.configuration.Configuration;
import mainclub.network.core.configuration.file.LangFile;
import mainclub.network.core.configuration.file.SettingsFile;

import java.util.ArrayList;
import java.util.List;

public class StringLists {
    @Getter private List<String> announcements;
    @Getter private List<String> whitelist = new ArrayList<>();
    @Getter private List<String> firstJoin;
    @Getter private List<String> join;
    @Getter private List<String> quit;
    @Getter private List<String> tabulatorHeader;
    @Getter private List<String> tabulatorFooter;

    public StringLists() {load();}

    public void load() {
        final Configuration configuration = Core.get().getConfiguration();

        announcements = configuration.getSettingsFile().getStringList("ANNOUNCEMENTS");
        whitelist = configuration.getSettingsFile().getStringList("WHITELIST.PLAYERS");

        firstJoin = configuration.getLangFile().getStringList("FIRST-JOIN");
        join = configuration.getLangFile().getStringList("JOIN");
        quit = configuration.getLangFile().getStringList("QUIT");

        tabulatorHeader = configuration.getSettingsFile().getStringList("TABULATOR.HEADER");
        tabulatorFooter = configuration.getSettingsFile().getStringList("TABULATOR.FOOTER");
    }
}