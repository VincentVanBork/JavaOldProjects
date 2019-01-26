package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Disabled {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String input;
            while (true) {
                Thread.sleep(1000);
                input = "s";
                out.println(input);
                out.flush();
                String function = "1";
                out.println(function);
                String particles = "20";
                out.println(particles);
                System.out.println(in.readLine());
                out.flush();
                listen(in);
                input = "wait";
                out.println(input);
                out.flush();
                System.out.println("Waiting");

            }

        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void listen(BufferedReader in) {
        while (true) {
            try {String p = in.readLine();

                System.out.println(p);
                if (p.equals("END")) {
                    break;
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
            }


        }
    }
}

