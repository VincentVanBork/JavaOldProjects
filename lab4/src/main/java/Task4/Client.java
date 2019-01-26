package Task4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private static Scanner scanner;
    private static ClientListener clientListener;

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 5000)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            clientListener = new ClientListener(in);
            Thread thread = new Thread(clientListener);
            thread.start();

            scanner = new Scanner(System.in);
            String input;

            while(true) {
                out.writeObject("show");
                out.flush();

                System.out.println("r - for reserving ");
                System.out.println("c - for cancelling reservations");
                System.out.println("show - showing all reservations");
                System.out.println("exit - exit application");

                input = scanner.nextLine();


                if (input.equals("r")) {
                    Reservation reservation = Reserve();
                    out.writeObject("reserve");
                    out.writeObject(reservation);
                    out.flush();
                }

                if (input.equals("c")) {
                    out.writeObject("cancel");

                    System.out.println("Write your name: ");
                    input = scanner.nextLine();
                    out.writeObject(input);

                    System.out.println("At which hour?");
                    input = scanner.nextLine();
                    out.writeInt(tryParse(input));
                    out.flush();
                }

                if (input.equals("show")) {
                    out.writeObject("show");
                    out.flush();
                    System.out.println("Look at table!");

                }

                if(input.equals("exit")){
                    out.writeObject("exit");
                    thread.stop();
                    out.flush();
                    out.close();
                    in.close();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Try again");
            return null;
        }
    }


    public static Reservation Reserve() {
        System.out.println("Your name: ");
        String name = scanner.nextLine();

        System.out.println("At which hour? ");
        String new_hour = scanner.nextLine();
        while (tryParse(new_hour) == null) {
            new_hour = scanner.nextLine();
        }
        Integer hour = Integer.parseInt(new_hour);

        if (tryParse(new_hour) != null) {
            Reservation reservation = new Reservation(name, hour);
            return reservation;
        }

        return null;
    }

}
