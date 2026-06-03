package mainclub.network.core.manager;

import mainclub.network.core.Core;
import mainclub.network.core.configuration.Configuration;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Date;

public class CoreManager {
    private final Core main = Core.get();
    private final Configuration configuration = main.getConfiguration();

    private List<UUID> messagesToggled = new ArrayList<>();
    private HashMap<UUID, List<UUID>> messagesIgnore = new HashMap<>();
    private HashMap<UUID, UUID> messageReply = new HashMap<>();

    private List<UUID> tpaToggled = new ArrayList<>();
    private HashMap<UUID, List<UUID>> tpaIgnore = new HashMap<>();
    private final HashMap<UUID, HashMap<UUID, Boolean>> tpaRequest = new HashMap<>();

    public CoreManager() {


        runRestart();
    }


    public void runRestart() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(()-> {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
            calendar.setTime(date);

            if(calendar.getTime().getHours() == configuration.getIntegers().getServerRestartHour()) Bukkit.shutdown();
        }, 1, 1, TimeUnit.HOURS);
    }


    public void requestTPA(final UUID target, final UUID player, boolean here) {
        final HashMap values = new HashMap();
        values.put(player, here);

        tpaRequest.put(target, values);
        Bukkit.getScheduler().runTaskLater(Core.get(), () -> {
            if(tpaRequest.containsKey(target)) tpaRequest.remove(player);
        }, 20 * 60);
    }
    public boolean hasTPARequest(final UUID uuid) {
        return tpaRequest.containsKey(uuid);
    }

    public List<UUID> messagesToggled() {
        return messagesToggled;
    }

    public HashMap<UUID, List<UUID>> messagesIgnore() {
        return messagesIgnore;
    }

    public HashMap<UUID, UUID> messageReply() {
        return messageReply;
    }

    public List<UUID> tpaToggled() {
        return tpaToggled;
    }

    public HashMap<UUID, List<UUID>> tpaIgnore() {
        return tpaIgnore;
    }
    public HashMap<UUID, HashMap<UUID, Boolean>> tpaRequest() {
        return tpaRequest;
    }
}
