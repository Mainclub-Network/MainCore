package mainclub.network.core.configuration;

import lombok.Getter;
import mainclub.network.core.Core;
import mainclub.network.core.configuration.context.Booleans;
import mainclub.network.core.configuration.context.Integers;
import mainclub.network.core.configuration.context.StringLists;
import mainclub.network.core.configuration.context.Strings;
import mainclub.network.core.configuration.file.*;

public class Configuration {
    private final Core clubCore = Core.get();
    @Getter private SettingsFile settingsFile;
    @Getter private LangFile langFile;
    @Getter private LocationsFile locationsFile;
    @Getter private CooldownFile cooldownFile;
    @Getter private KitsFile kitsFile;
    @Getter private ProfilesFile profileFile;
    @Getter private HomesFile homesFile;

    @Getter private Integers integers;
    @Getter private Strings strings;
    @Getter private StringLists stringLists;
    @Getter private Booleans booleans;

    public Configuration() {
        settingsFile = new SettingsFile();
        langFile = new LangFile();
        locationsFile = new LocationsFile();
        cooldownFile = new CooldownFile();
        kitsFile = new KitsFile();
        profileFile = new ProfilesFile();
        homesFile = new HomesFile();

        //load();
    }


    public void load() {
        integers = new Integers();
        strings = new Strings();
        stringLists = new StringLists();
        booleans = new Booleans();
    }

    public void save() {
        booleans.save();
        locationsFile.save();
    }

    public void reload() {
        settingsFile.reload();
        langFile.reload();
        locationsFile.reload();
        kitsFile.reload();
        //clubCore.getProfileFile().reload();
        integers.load();
        strings.load();
        stringLists.load();
    }

}