package Server;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@XmlRootElement(name = "Server")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Server {

    private static final String TILES_FILE = "Server.xml";

    // refreshing game state and sending data to clients every x ms
    private static final long REFRESH_GAP = 30;

    private static int SERVER_PORT_TCP;

    private static long IDs = 0L;

    //thread safe array because while one thread is reading another
    //might add delete some entries
    private CopyOnWriteArrayList<IpPort> activeClients;
    private Vector<MainCharacter> fullCharacters;

    private Helper.WrapperList tiles;
    private Helper.WrapperList gamePlay;

    private UdpConnectionsSend udpSend;


    public Server(int tcpPort){
        SERVER_PORT_TCP = tcpPort;
        activeClients = new CopyOnWriteArrayList<>();
        tiles = new Helper.WrapperList();
        gamePlay = new Helper.WrapperList();
        udpSend = new UdpConnectionsSend();
        fullCharacters = new Vector<>();
    }

    public void start(){

        gameStateRefresher();
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT_TCP);
            tiles = Helper.unmarsh(TILES_FILE);
            Socket clientSocket;
            while((clientSocket = serverSocket.accept()) != null){
                new Thread(new TcpConnection(this, clientSocket)).start();
            }

        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }

    }

    private void gameStateRefresher(){

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                updateGamePlay();
                udpSend.sendGamePlay();
            }

            private void updateGamePlay() {
                gamePlay.clear();
                for (MainCharacter mc : fullCharacters){
                    gamePlay.addAll(mc.update(tiles.realList, fullCharacters));
                }
            }

        },0, REFRESH_GAP);
    }

    synchronized long getId(){
        return IDs++;
    }

    /**
     * This function is called when new data about character arrives.
     * If this is a new character we update its state otherwise
     * we simply update velocity and etc.
     * @param data that we get from client
     */

    void includeCharacter(CharacterObj data){

        long specId = data.id;
        for (MainCharacter mc : fullCharacters){

            //if character already exists we just update its status
            if (specId == mc.getID()){
                mc.updateState(data);
                return ;
            }
        }
        //if it is new character then we add it to the list
        MainCharacter newMc = new MainCharacter(data);
        fullCharacters.add(newMc);
    }

    void removeCharacter(long id){

        Iterator<MainCharacter> i = fullCharacters.iterator();
        while(i.hasNext()){
            MainCharacter mc = i.next();
            if (mc.getID() == id){
                i.remove();
                return;
            }
        }
    }


    void addressBook(InetAddress address, int port){
        activeClients.add(new IpPort(address, port));
    }

    private static class IpPort{

        InetAddress address;
        int port;

        public IpPort(InetAddress address, int port){
            this.address = address;
            this.port = port;
        }
    }

    public Helper.WrapperList getMap(){
        return tiles;
    }

    private class UdpConnectionsSend{

        DatagramSocket gamePlaySocket;

        public UdpConnectionsSend() {

            try {
                gamePlaySocket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }


        public void sendGamePlay() {

            try{
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(Helper.marshall(gamePlay));
                byte [] bytes = baos.toByteArray();
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

                for (IpPort dest : activeClients){
                    packet.setAddress(dest.address);
                    packet.setPort(dest.port);
                    gamePlaySocket.send(packet);
                    packet.setData(bytes);
                    packet.setLength(bytes.length);
                }

            }catch (IOException | JAXBException e) {
                e.printStackTrace();
            }
        }
    }
}

