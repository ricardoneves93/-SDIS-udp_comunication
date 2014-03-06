import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashMap;

public class Server{

	private HashMap<String, String> data;
	private DatagramSocket serverSocket;
	private MulticastSocket multiSocket;
	private String sentence;
	private byte[] sendData;
	private byte[] receiveData;
	String[] Infos = {"The Instruction isn't well formed", "This data is already stored", "Item sucessefully registed", "Wrong plate format"};
	private int port = 9876;
	private String group = "224.0.0.2";
	private int ttl = 1;
	

	public Server() throws SocketException
	{
		data = new HashMap<String, String>(); 
		serverSocket = new DatagramSocket(port);

		sendData = new byte[1024];
		receiveData = new byte[1024];
	}

	private void init() throws IOException {
		
		multiSocket = new MulticastSocket();
		Thread t = new Thread()
		{
			@SuppressWarnings("deprecation")
			public void run()
			{
				while(true)
				{
					InetSocketAddress d = new InetSocketAddress(group, port);
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					byte[] hello = ("hello").getBytes();
					DatagramPacket h = new DatagramPacket(hello, hello.length, d.getAddress(), d.getPort());
					try {
						multiSocket.send(h, (byte) ttl);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		t.start();
		
		while(true)
		{
			receiveData = new byte[1024];
			sendData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			sentence = new String(receivePacket.getData());
			System.out.println("Recebido: " + sentence);
			String info;
			info = compareData(sentence);
			InetAddress IpAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			//if(info == null)
				//sendData = "Plate not found".getBytes();
			try{
				sendData = info.getBytes();
			}
			catch(Exception e)
			{
				sendData = "plate not found".getBytes();
			}
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
				if(data.get(splitted[1]) == null && splitted[1].matches("([0-9A-Z][0-9A-Z]-){2}[0-9A-Z][0-9A-Z]"))
				{
					data.put(splitted[1].trim(), splitted[2].trim());
					return Infos[2];
				}
				else if(!splitted[1].matches("([0-9A-Z][0-9A-Z]-){2}[0-9A-Z][0-9A-Z]"))
					return Infos[3]; //Wrong plate
				else if(data.get(splitted[1]) != null)
					return Infos[2]; //Already registed
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
		else return ownerName;
		
	}
	

	public static void main(String[] args) throws Exception
	{
		Server myServer = new Server();
		myServer.init();
	}
	
}