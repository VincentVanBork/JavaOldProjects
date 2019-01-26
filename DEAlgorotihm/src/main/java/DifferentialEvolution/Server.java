package DifferentialEvolution;

import java.io.IOException;
import java.net.ServerSocket;

public class Server{
    public Server(){

    }

    public static void main(String[] args) {
    }

    public void listening() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                ClientHandler new_client = new ClientHandler(serverSocket.accept());
                new_client.start();
                System.out.println("A new client is connected : " + serverSocket);


            }

        } catch (IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    }




}

