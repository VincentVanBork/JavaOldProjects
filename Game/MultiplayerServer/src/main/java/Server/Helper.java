package Server;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.jar.JarException;

public class Helper {
	
	
	
	public static ServerMessage unmarshall(String data) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(ServerMessage.class);
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader sr = new StringReader(data);
		
		return (ServerMessage) unmarshaller.unmarshal(sr);
	}
	
	
	public static String marshall(WrapperList wl) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(WrapperList.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(wl, sw);
        
		return sw.toString();
	}

	public static Helper.WrapperList unmarsh(String pathname) throws JAXBException{
		JAXBContext jax = JAXBContext.newInstance(Helper.WrapperList.class);
		Unmarshaller unmarshaller = jax.createUnmarshaller();
		return (WrapperList) unmarshaller.unmarshal(new File(pathname));
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class WrapperList{
		List<Box> realList;
		
		public WrapperList(){
			realList = new ArrayList<Box>();
		}
		public void clear() {
			realList.clear();			
		}

		public void addAll(List<Box> update) {
			realList.addAll(update);
		}
		public void add(Box box) {
			realList.add(box);
		}
	}


//	public static String marsh(WrapperList tiles,String file) throws JAXBException{
//			JAXBContext jax = JAXBContext.newInstance(Helper.WrapperList.class);
//            Marshaller marsh = jax.createMarshaller();
//            marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			InputStream ff = Helper.class.getResourceAsStream(file);
//            Scanner fileReader = new Scanner(ff);
//            while(fileReader.hasNext()){
//                tiles.add(new Box(fileReader.nextInt(), fileReader.nextInt(),
//                        fileReader.nextInt(), fileReader.nextInt(), 1f,
//                        1f, 1f, -1L, -1));
//            }
//            marsh.marshal(tiles,new File("Server.xml"));
//		return file;
//	}
}
