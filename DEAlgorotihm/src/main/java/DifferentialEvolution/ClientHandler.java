package DifferentialEvolution;
import java.io.*;
import java.net.Socket;



public class ClientHandler extends Thread {
    public Socket socket;

    public ObjectOutputStream out;
    public ObjectInputStream in;

    int def_function = 1;
    int def_n = 100;
    double def_F = 0.2f;
    double def_CR = 0.9f;
    int def_space = 4000;
    double def_STOP = 0.5;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    synchronized public void run() {

        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());



            while(true) {
                out.writeUTF("e for default calc, rosenbrock, population 100, 0.2 F , 0.9 CR, on space 4000 , STOP on 0,5 STD");
                out.flush();

                String request = in.readUTF();
                System.out.println(request);

                if (request.equals("e"))
                {
                    Differential DE = new Differential(def_function,def_n,def_F,def_CR,def_space,out,def_STOP);
                    DE.run();
                }

                if (request.equals("p")){
                    Values def;

                    def = (Values) in.readObject();
                    def_function = def.function;
                    def_n = def.n;
                    def_F = def.F;
                    def_CR = def.CR;
                    def_space = def.space;
                    def_STOP = def.STOP;

                    Differential DE = new Differential(def_function,def_n,def_F,def_CR,def_space,out,def_STOP);
                    DE.run();
                }

                if(request.equals("exit")) {
                    break;
                }


            }

        } catch(IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();

            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }


}

