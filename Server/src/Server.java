import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class Server {
	
	private HashMap<String, String> data;
	private DatagramSocket serverSocket;
	private byte[] sendData;
	private byte[] receiveData;
	String errorString, alreadyOnString, registedString;
	
	public Server() throws SocketException
	{
		data = new HashMap<String, String>(); 
		serverSocket = new DatagramSocket(9876);//igor12
		
		sendData = new byte[1024];
		receiveData = new byte[1024];
		errorString = "The Instruction isn't well formed";
		alreadyOnString = "This data is already stored";
		registedString = "Item sucessefully registed";
	}
	
	private void start() throws IOException {
		while(true)
		{
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			Integer info;
			
			info = compareData(sentence);
			
			InetAddress IpAddress = receivePacket.getAddress();
			
			int port = receivePacket.getPort();
			
			if(info == 0)
			{
				sendData = registedString.getBytes();
			}
			if(info == -1)
			{
				sendData = errorString.getBytes();
			}
			else if(info == -2)
			{
				sendData = alreadyOnString.getBytes();
			}
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IpAddress, port);
			
			serverSocket.send(sendPacket);
		}
	}
	
	private int compareData(String sentence) {
		
		String[] splitted = sentence.split(" ");//Editado no browser
		
		if(splitted[0].equals("REGISTER"))
		{
			if(splitted.length == 3)
			{
				
				if(data.get(splitted[1]) == null)
				{
					data.put(splitted[1], splitted[2]);
					return 0;
				}
				else
					return -2; //Is already registed
				
			}
		}
		else if(splitted[0].equals("LOOKUP"))
		{
			if(splitted.length == 2)
			{
				return 1;//The lookup is possible
			}
		}
		return -1;
		
		
	}

	public static void main(String[] args) throws Exception
	{
		Server myServer = new Server();
		myServer.start();
	}
}
