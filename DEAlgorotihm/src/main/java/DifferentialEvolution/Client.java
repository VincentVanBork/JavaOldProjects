package DifferentialEvolution;
import org.jzy3d.maths.Range;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;



public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public void run(){
        try (Socket socket = new Socket("localhost", 5000)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);
            String input;

            do {
                System.out.println("e default run ");
                System.out.println("p changed parameters run ");
                input = scanner.nextLine();
                out.writeUTF(input);
                out.flush();

                if (input.equals("e")){
                    listen(in);

                    Diagram diagram = new Diagram();

                    try{
                        double[][] points = (double[][]) in.readObject();
                        diagram.plot(Functions.getRosenBrock(), points, new Range(-3, 3), 100,1);
                    }
                    catch (ClassNotFoundException e){e.getMessage();}
                }
                if (input.equals("p")){

                    Values changed =  ChangeParams(scanner);
                    out.writeObject(changed);
                    out.flush();
                    listen(in);

                    Diagram diagram = new Diagram();

                    try{
                    double[][] points = (double[][]) in.readObject();


                        if (changed.function == 1){
                        diagram.plot(Functions.getRosenBrock(), points, new Range(-5, 5), 100,1);
                        }

                        else if (changed.function ==2){
                            diagram.plot(Functions.getackleysFunction(), points, new Range(-5, 5), 100,2);
                        }

                        else if (changed.function ==3){
                            diagram.plot(Functions.getboothsFunction(), points, new Range(-10, 10), 100,3);
                        }

                        else if (changed.function ==4){
                            diagram.plot(Functions.getXsqerYsqer(), points, new Range(-10, 10), 100,4);
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
                String p = in.readUTF();
                if (p.equals("END")) {
                    break;
                }else{System.out.println(p);}

            }catch (IOException e){
                System.out.println(e.getMessage());
            }


        }
    }


    public static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            System.out.println("Try again");
            return null;
        }
    }

    public static Double tryDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            System.out.println("Try again");
            return null;
        }
    }




    public static Values ChangeParams(Scanner scanner){
        int function = 1;
        int n = 100;
        int space = 4000;
        double F = 0.2;
        double CR = 0.9;
        double STOP = 0.6;
        System.out.println("Changing parameters press enter to move along");

        System.out.println("Functions default is Rosenbrock");
        System.out.println("1. RosenBrock");
        System.out.println("2. Ackley's Function");
        System.out.println("3. Booth's Function");
        System.out.println("4. x^2 + y^2");
        String new_function = scanner.nextLine();

        if (!new_function.equals("")) {
            while (tryParse(new_function) == null) {
                new_function = scanner.nextLine();
            }
            function = Integer.parseInt(new_function);
        }

        new_function= Integer.toString(function);

        System.out.println("number population "+ n );
        String new_n = scanner.nextLine();

        if (!new_n.equals("")){
            while (tryParse(new_n) == null) {
                new_n = scanner.nextLine();
            }
            n = Integer.parseInt(new_n);
        }

        new_n = Integer.toString(n);

        System.out.println("space / "+ space);
        String new_space = scanner.nextLine();

        if (!new_space.equals("")) {
            while (tryParse(new_space) == null) {
                new_space = scanner.nextLine();
            }
            space = Integer.parseInt(new_space);
        }

        new_space = Integer.toString(n);


        System.out.println("f is equal to "+ F +" from range [0,2] ");

        String new_f= scanner.nextLine();

        if(!new_f.equals("")) {
            while (tryDouble(new_f) == null) {
                new_f = scanner.nextLine();
            }
            F = Double.parseDouble(new_f);
        }
        new_f = Double.toString(F);

        System.out.println("cr is equal to "+CR +" from range [0,1] ");

        String new_cr= scanner.nextLine();

        if(!new_cr.equals("")) {
            while (tryDouble(new_cr) == null) {
                new_cr = scanner.nextLine();
            }
            CR = Double.parseDouble(new_cr);
        }
        new_cr = Double.toString(CR);


        System.out.println("stop criterium is equal to "+ STOP);
        String new_STOP = scanner.nextLine();

        if(!new_STOP.equals("")) {
            while (tryDouble(new_STOP) == null) {
                new_STOP = scanner.nextLine();
            }
            STOP = Double.parseDouble(new_STOP); }

        new_STOP = Double.toString(STOP);


        if (    tryParse(new_n) != null &&
                tryParse(new_function) != null &&
                tryParse(new_space) != null &&
                tryDouble(new_f) != null &&
                tryDouble(new_cr) != null &&
                tryDouble(new_STOP)!= null){
            return new Values(function,n,F,CR,space,STOP);
        }

        return new Values(function,n,F,CR,space,STOP);
    }

}


