package mainclub.network.core.utils;

public class TimeUtil {
    private  String format = "";

    public TimeUtil(final int time) {
        if(time < 60) format = time+"s"; //s
        else if(time < 3600) format = (time / 60 )+"m ";//+(time % 60)+"s"; // 1m
        else if(time < 86400) format = (time / 3600)+"h "+((time / 60) % 60)+"m"; //1h 1m
        else format = (time / 86400)+"d "+ ((time / 3600) % 24 == 0 ? "" : (time / 3600) % 24+"h"); //1d 1m/1h
    }

    public String format(){
            return format;
        }

}
