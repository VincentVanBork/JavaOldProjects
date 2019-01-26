package Task4;


import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListSet;

public class Reservation extends ConcurrentSkipListSet implements Serializable {
    private String customerName;
    private int hour;

    public Reservation(String customerName, int hour) {
        this.customerName = customerName;
        this.hour = hour;
    }


    public String getName() {
        return customerName;
    }

    public Integer getHour() {
        return this.hour;
    }


}
