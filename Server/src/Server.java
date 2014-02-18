import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class Server {

	private HashMap<String, String> data;
	private DatagramSocket serverSocket;
	private String sentence;
	private byte[] sendData;
	private byte[] receiveData;
	String[] Infos = {"The Instruction isn't well formed", "This data is already stored", "Item sucessefully registed"};

	public Server() throws SocketException
	{
		data = new HashMap<String, String>(); 
		serverSocket = new DatagramSocket(9876);//igor1234

		sendData = new byte[1024];//apaga os comentarios IGOR já!
		receiveData = new byte[1024];
	}

	private void start() throws IOException {
		while(true)
		{
			receiveData = new byte[1024];
			sendData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			System.out.println("tamanho buffer: " + receivePacket.getLength());
			sentence = new String(receivePacket.getData());
			System.out.println(sentence);
			String info;
			info = compareData(sentence);
			InetAddress IpAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			sendData = info.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IpAddress, port);
			serverSocket.send(sendPacket);
			sentence=null; 
		}
	}

	private String compareData(String sentence) {

		String[] splitted = sentence.split(" ");//Editado no browser

		if(splitted[0].equals("REGISTER"))
		{
			if(splitted.length == 3)
			{
				if(!splitted[1].matches("([0-9A-Z][0-9A-Z]-){2}[0-9A-Z][0-9A-Z]"))
					return "Matricula mal formada";
				if(data.get(splitted[1]) == null && splitted[1].matches("([0-9A-Z][0-9A-Z]-){2}[0-9A-Z][0-9A-Z]"))
				{
					data.put(splitted[1].trim(), splitted[2].trim());
					return Infos[2];
				}
				else
					return Infos[1]; //Is already registed

			}
		}
		else if(splitted[0].equals("LOOKUP"))
		{
			if(splitted.length == 2)
			{
				String ownerName;
				ownerName = makeLookup(splitted[1].trim());
				System.out.println(splitted[1]);
				return ownerName;//The lookup is possible
			}
		}
		return Infos[0];


	}

	private String makeLookup(String plate) {
		String ownerName = data.get(plate);
		if(ownerName != null)
			return ownerName;
		return ownerName;
		
	}

	public static void main(String[] args) throws Exception
	{
		if("AA-AA-AA".matches("([0-9A-Z][0-9A-Z]-){2}[0-9A-Z][0-9A-Z]"))
			System.out.println("match"); 
		else System.out.println("nao match");
		Server myServer = new Server();
		myServer.start();
	}
}