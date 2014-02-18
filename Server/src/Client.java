import java.net.*;
import java.util.Scanner;

class UDPClient
{
	private static DatagramSocket clientSocket;
	private static InetAddress IPAddress;
	static byte[] sendData;
	static byte[] receiveData;
	static boolean isRunning = true; //boolean to control when the program exits
	static Scanner sc;
	
	
	public UDPClient()
	{
		sc = new Scanner(System.in);
		sendData = new byte[1024];
		receiveData = new byte[1024];
	}
	
	public static void main(String args[]) throws Exception
	{
		UDPClient client = new UDPClient();
		client.start("192.168.1.4");
		
		while(isRunning)
		{
			//Need to create new buffers 
			sendData = new byte[1024];
			receiveData = new byte[1024];
			String sentence = sc.nextLine();
			if(sentence.equals("STOP") || sentence.equals("stop"))
			{
				isRunning = false;
				break;
			}
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String modifiedSentence = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + modifiedSentence);
			

		
		}
		clientSocket.close();
	}

	private void start(String Ip) throws SocketException, UnknownHostException {
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName(Ip);
		
	}
} 