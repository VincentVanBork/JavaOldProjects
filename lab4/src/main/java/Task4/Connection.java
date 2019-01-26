package Task4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;


public class Connection extends Thread {
    public Socket socket;
    public ObjectOutputStream out;
    public ObjectInputStream in;
    private Data Ledger;
    private Reservation clientReservation;

    public Connection(Socket socket, Data Ledger) throws IOException {
        this.socket = socket;
        this.Ledger = Ledger;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String request = (String) in.readObject();
                System.out.println("Received client input: " + request);

                if (request.equals("siema")) {
                    System.out.println("was here");
                    out.writeObject("message");
                    out.writeObject("siemanko");
                    out.flush();
                }

                if (request.equals("exit")) {
                    break;
                }

                if (request.equals("reserve")) {
                    clientReservation = getClientsMessage();
                    try {
                        if (Ledger.day.isReservedAtHour(clientReservation.getHour())) {
                            out.writeObject("message");
                            out.writeObject("Sorry, this reservation is already reserved.");
                        } else {
                            Ledger.reservations.add(clientReservation);
                            Ledger.getDay().changeAval(clientReservation.getHour(), false);

                            out.writeObject("message");
                            out.flush();

                            out.writeObject(clientReservation.getName() + " your reservation was completed at "+ clientReservation.getHour()+ "o'clock");
                            out.flush();

                            for (int x=0;x<8;x++) {
                                if(!Ledger.day.getSchedule().get(x + 10)){
                                System.out.println(Ledger.day.getSchedule().get(x+10));
                                }
                            }

                            Ledger.sendToAll();
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        out.writeObject("END");
                        out.flush();
                    }
                }

                if (request.equals("cancel")) {
                    String clientsName = (String) in.readObject();
                    int hour = in.readInt();
                    boolean isSuccessfullyChanged = false;

                    for (Reservation res : Ledger.reservations) {
                        if (res.getName().equals(clientsName) && res.getHour() == hour) {
                            Ledger.day.changeAval(hour, true);
                            out.writeObject("message");
                            out.flush();

                            out.writeObject(clientReservation.getName() + " your reservation was cancelled at "+ clientReservation.getHour()+ "o'clock");
                            out.flush();
                            isSuccessfullyChanged = true;
                            Ledger.sendToAll();
                        }
                    }

                    if(!isSuccessfullyChanged){
                        out.writeObject("message");
                        out.writeObject("Sorry, we couldn't cancel this reservation.");
                    }
                }

                if (request.equals("show")) {
                    Day schedule = Data.day;
                    out.writeObject("show");
                    out.flush();
                    out.writeObject(new HashMap<>(schedule.getSchedule()));
                    out.flush();
                }

                if(request.equals("exit")){
                    out.writeObject("exit");
                    socket.close();
                    in.close();
                    out.close();
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Reservation getClientsMessage() throws IOException, ClassNotFoundException {
        return (Reservation) in.readObject();
    }

}

