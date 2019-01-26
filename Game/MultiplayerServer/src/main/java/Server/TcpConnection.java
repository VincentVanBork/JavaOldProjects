package Server;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This class establishes TCP connection and listens to client side
 * for tasks to do.
 */
class TcpConnection implements Runnable{
	
	private static final int GET_ID = 0;
	private static final int GET_MAP = 1;
	private static final int SEND_MAIN_CHARACTER = 2;
	private static final int GET_ID_IP_PORT = 3;
	private static final int REMOVE_CHARACTER = 4;

	private Server server;
	private Socket socket;
	
	TcpConnection(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		try(ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
			
			while(true){
				String msg = (String)ois.readObject();
				ServerMessage sm;
				try {
					sm = Helper.unmarshall(msg);
				} catch (JAXBException e) {
					e.printStackTrace();
					continue;
				}
				switch(sm.messageType){
					case GET_ID:
						oos.writeLong(server.getId());
						break;
					case GET_MAP:
						try {
							String data = Helper.marshall(server.getMap());
							oos.writeObject(data);
						} catch (JAXBException e) {
							e.printStackTrace();
						}
						break;
					case SEND_MAIN_CHARACTER:
						server.includeCharacter(sm.characterData);
						System.out.println(sm.characterData.id +sm.characterData.xVel+ sm.characterData.yVel);
						break;
					case GET_ID_IP_PORT: 
						String ipString = socket.getInetAddress().getHostName();
						InetAddress clientIp = InetAddress.getByName(ipString);
						System.err.println(ipString + " " + clientIp);
						server.addressBook(clientIp, sm.port);
						break;
					case REMOVE_CHARACTER:
						server.removeCharacter(sm.id);
						break;
					default:
						break;
				}
				oos.flush();
				
			}
		}catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
			System.out.println("Player leaves");
		}
	}

}
