package Task4;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientListener implements Runnable {

    private ObjectInputStream inputStream;

    public ClientListener(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {

        JFrame frame = new JFrame("Reservations");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 300));

        SwingUtilities.updateComponentTreeUI(frame);


        int hour = 10;
        String[] hours = {"10", "11", "12", "13", "14", "15", "16", "17"};

        while (true) {
            try {
                String message = (String) inputStream.readObject();

                if (message.equals("message")) {
                    String args = (String) inputStream.readObject();
                    Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"java -jar ReadFromServer.jar " +
                            args + "\"");
                }

                if (message.equals("show")) {

                    Map<Integer, Boolean> map = (HashMap<Integer, Boolean>) inputStream.readObject();
                    ConcurrentHashMap<Integer, Boolean> cMap = new ConcurrentHashMap<>(map);
                    Day schedule = new Day(cMap);

                    String[] aval = new String[8];

                    for (int m = 0; m < 8; m++) {
                            aval[m] = m + hour + " - ";
                            if(schedule.isReservedAtHour(m+hour)){
                                aval[m] += "UNAVAILABLE";
                            } else {
                                aval[m] += "AVAILABLE";
                            }
                    }


                    frame.setVisible(false);
                    frame.getContentPane().removeAll();

                    frame.add(new JList<>(aval));
                    frame.pack();
                    frame.add(new JList<>(hours));
                    frame.pack();
                    frame.repaint();

                    frame.setVisible(true);


                }

                if(message.equals("exit")){
                    inputStream.close();
                    break;
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
