package Server;



import Algorithms.Particle;
import Algorithms.Swarm;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {
    private Socket socket;

    public ObjectOutputStream out;
    public ObjectInputStream in;

    public Connection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(socket.getInputStream()));
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            Double INERTIA = Swarm.DEFAULT_INERTIA;
            Double COGNITIVE = Swarm.DEFAULT_COGNITIVE;
            Double SOCIAL = Swarm.DEFAULT_SOCIAL;
            Integer PARTICLES;
            Particle.type FUNCTION = Particle.type.XsqerYsqer;


            while(true) {
                try{
                String request = (String) in.readObject();


                if(request.equals("exit")) {
                    break;
                }



                if (request.equals("p")){
                    System.out.println("Received client input: " + request);
                    out.writeObject(" def Inertia:             " + Swarm.DEFAULT_INERTIA);
                    out.writeObject("def Cognitive Component: " + Swarm.DEFAULT_COGNITIVE);
                    out.writeObject("def Social Component:    " + Swarm.DEFAULT_SOCIAL);
                    out.writeObject("END");

                    System.out.println("def Inertia:             " + Swarm.DEFAULT_INERTIA);
                    System.out.println("def Cognitive Component: " + Swarm.DEFAULT_COGNITIVE);
                    System.out.println("def Social Component:    " + Swarm.DEFAULT_SOCIAL);



                    System.out.println("END");
                    request = "END";
                }

                if (request.equals("change_inertia")){
                    String new_inertia = (String) in.readObject();
                    INERTIA = Double.parseDouble(new_inertia);
                    System.out.println(INERTIA);
                    out.flush();
                }

                if (request.equals("change_cognitive")){
                    String new_cognitive = (String) in.readObject();
                    COGNITIVE = Double.parseDouble(new_cognitive);
                    System.out.println(COGNITIVE);
                    out.flush();
                }

                if (request.equals("change_social")){
                    String new_social = (String) in.readObject();
                    SOCIAL = Double.parseDouble(new_social);
                    System.out.println(SOCIAL);
                    out.flush();
                }

                if (request.equals("show_parameters")){
                    out.writeObject(INERTIA.toString());
                    out.writeObject(COGNITIVE.toString());
                    out.writeObject(SOCIAL.toString());
                    out.writeObject("END");
                    out.flush();
                    request = "waiting";
                }

                if (request.equals("s")){
                    Swarm swarm;
                    String type = (String) in.readObject();

                    Integer TYPE = Integer.parseInt(type);
                    if (TYPE==1) {
                        FUNCTION = Particle.type.RosenBrock;
                    }
                    if (TYPE==2) {
                        FUNCTION = Particle.type.Ackleys;
                    }
                    if (TYPE==3) {
                        FUNCTION = Particle.type.Booths;
                    }
                    if (TYPE==4) {
                        FUNCTION = Particle.type.XsqerYsqer;
                    }

                    out.writeObject(TYPE.toString());
                    out.flush();

                    String particles = (String) in.readObject();


                    PARTICLES = Integer.parseInt(particles);

                    out.writeObject(PARTICLES.toString());
                    out.flush();
                    String stop = (String) in.readObject();

                    out.writeObject(stop);

                    Double STOP_CONDITION = Double.parseDouble(stop);

                    swarm = new Swarm(FUNCTION, PARTICLES, INERTIA, COGNITIVE, SOCIAL,STOP_CONDITION,out);
                    swarm.run();

                }

            }catch (ClassNotFoundException e){e.getMessage();}
            }

        } catch(IOException e) {
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
