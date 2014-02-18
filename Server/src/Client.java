import java.io.*;
import java.net.*;

class UDPClient
{
	private static BufferedReader inFromUser;
	private static DatagramSocket clientSocket;
	private static InetAddress IPAddress;
	static byte[] sendData;
	static byte[] receiveData;
	static boolean isRunning = true;
	
	
	public UDPClient()
	{
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		sendData = new byte[1024];
		receiveData = new byte[1024];
	}
	
	public static void main(String args[]) throws Exception
	{
		UDPClient client = new UDPClient();
		client.start("172.30.74.194");
		
		while(isRunning)
		{
			String sentence = inFromUser.readLine();
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