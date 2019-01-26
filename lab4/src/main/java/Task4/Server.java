package Task4;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public Data Ledger;

    public Server() {
        this.Ledger = new Data();
    }

    public void listening() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                Connection new_client = new Connection(serverSocket.accept(), Ledger);
                new_client.start();
                System.out.println("A new client is connected : " + serverSocket);
                Ledger.connections.add(new_client);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
