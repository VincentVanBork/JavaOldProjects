package Task4;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;


public class Day implements Serializable {
    private ConcurrentHashMap<Integer, Boolean> schedule;

    public Day(ConcurrentHashMap<Integer, Boolean> schedule){
        this.schedule = schedule;
    }

    public Day(){
        schedule = new ConcurrentHashMap<>();
        for(int i = 10; i<=18; i++){
            schedule.put(i, true);
        }
    }

    public synchronized boolean isReservedAtHour(int hour){
        return !schedule.get(hour);
    }

    public synchronized void changeAval(Integer key, Boolean status) {
        this.schedule.replace(key, status);
    }

    public boolean aval(Integer key) {
        return this.schedule.get(key);
    }

    public ConcurrentHashMap<Integer, Boolean> getSchedule() {
        return this.schedule;
    }

}