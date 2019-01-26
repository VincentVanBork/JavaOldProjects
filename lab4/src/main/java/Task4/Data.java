package Task4;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Data implements Serializable {
    public static Day day;
    public static List<Connection> connections;
    public List<Reservation> reservations;

    public Data() {
        this.day = new Day();
        this.connections = new CopyOnWriteArrayList<>();
        this.reservations = new CopyOnWriteArrayList<>();
    }

    public synchronized static void sendToAll(){
        for (Connection conn : connections){
            try {
                conn.out.writeObject("show");
                conn.out.flush();
                conn.out.writeObject(new HashMap<>(day.getSchedule()));
                conn.out.flush();

                for (int x=0;x<8;x++) {
                    if(!day.getSchedule().get(x + 10)){
                        System.out.println(day.getSchedule().get(x+10));}
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public synchronized Reservation findReservation(String Name, Integer Hour) {
//        if (reservations == null) {
//            return null;
//        }
//        for (Reservation x : reservations) {
//            if (x.getName().equals(Name) && x.getHour().equals(Hour)) {
//                return x;
//            }
//        }
//        return null;
//    }


//    public synchronized Boolean checkReservation(Integer Hour) {
//        if (reservations == null) {
//            return false;
//        }
//        for (Reservation x : reservations) {
//            if (x.getHour().equals(Hour)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public synchronized static Day getDay() {
        return day;
    }

//    public List<Reservation> getReservations() {
//        return reservations;
//    }
//
//    public static List<Connection> getConnections() {
//        return connections;
//    }
}
