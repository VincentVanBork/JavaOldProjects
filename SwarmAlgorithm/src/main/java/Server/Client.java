package Server;
import Algorithms.Diagram;
import Algorithms.Function;
import Algorithms.Particle;
import org.jzy3d.maths.Range;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(socket.getInputStream()));
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Scanner scanner = new Scanner(System.in);
            String input;
            String function = "1";

            do {
                System.out.println("p for change in parameters  ");
                System.out.println("s for starting calculations  ");
                input = scanner.nextLine();
                try {
                    out.writeObject(input);

                    out.flush();
                }catch (IOException e){
                    e.getMessage();
                }

                if (input.equals("p")) {
                    listen(in);
                    try {
                        out.flush();

                        System.out.println("enter new inertia");
                        out.writeObject("change_inertia");
                        out.flush();
                        String new_inertia = scanner.nextLine();
                        out.writeObject(new_inertia);

                        System.out.println("enter new cognitive");
                        out.writeObject("change_cognitive");
                        String new_cognitive = scanner.nextLine();
                        out.writeObject(new_cognitive);
                        out.flush();

                        System.out.println("enter new social");
                        out.writeObject("change_social");
                        String new_social = scanner.nextLine();
                        out.writeObject(new_social);
                        out.flush();

                        out.writeObject("show_parameters");
                        listen(in);
                    }catch (IOException e){e.getMessage();}

                }

                if (input.equals("s")) {

                    try {
                        System.out.println("Select a function defalut x^2 + y^2");
                        System.out.println("1. Rosenbrock");
                        System.out.println("2. Ackley's Function");
                        System.out.println("3. Booth's Function");
                        System.out.println("4. x^2 + y^2");
                        function = scanner.nextLine();

                        out.writeObject(function);

                        System.out.println(in.readObject());

                        System.out.println("Select number of particles default 10");
                        String particles = scanner.nextLine();

                        out.writeObject(particles);
                        System.out.println(in.readObject());
                        out.flush();

                        System.out.println("Select stop condition Default 0.000001");
                        String condition = scanner.nextLine();

                        out.writeObject(condition);
                        System.out.println( in.readObject());
                        out.flush();

                    }catch (IOException | ClassNotFoundException e){e.getMessage();}

                    listen(in);

                    Diagram diagram = new Diagram();

                    try{

                        Particle[] particles = (Particle[]) in.readObject();

                        if (function.equals("1")){
                            diagram.plot(Function.getRosenBrock(), particles, new Range(-2, 2), 100,1);
                        }

                        else if (function.equals("2")){
                            diagram.plot(Function.getackleysFunction(), particles, new Range(-5, 5), 100,2);
                        }

                        else if (function.equals("3")){
                            diagram.plot(Function.getboothsFunction(), particles, new Range(-10, 10), 100,3);
                        }

                        else if (function.equals("4")){
                            diagram.plot(Function.getXsqerYsqer(), particles, new Range(-10, 10), 100,4);
                        }


                    }
                    catch (ClassNotFoundException e){e.getMessage();}

                }



            } while (!input.equals("exit"));

        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());

        }
    }

    public static void listen(ObjectInputStream in) {
        while (true) {
            try {
                Object o = in.readObject();
                if (o instanceof Double) {
                    Double d = (Double) o;
                    System.out.println(d);
                }

                if (o instanceof Integer) {
                    Integer I = (Integer) o;
                    System.out.println(I);
                }

                if (o instanceof String) {
                    String s = (String) o;
                    if (s.equals("END")){
                        break;}
                    else{
                        System.out.println(s);}

                }



            }catch (IOException | ClassNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
