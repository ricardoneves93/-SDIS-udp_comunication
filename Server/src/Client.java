import java.io.IOException;
import java.net.*;
import java.util.Scanner;

import javax.swing.JFrame;

class UDPClient
{
	private DatagramSocket clientSocket;
	private InetAddress IPAddress;
	private byte[] sendData;
	private byte[] receiveData; 
	private String action;
	private String plate;
	private String name;
	private String sentence;

	public UDPClient()
	{
		
		sendData = new byte[1024];
		receiveData = new byte[1024];
	}

	public static void main(String args[]) throws Exception
	{
		UDPClient client = new UDPClient();
		client.start("255.255.255.255", args);
	}
	
	public void start(String Ip, String args[]) throws IOException {
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName(Ip);

		
		//lookup e register
		action = args[0];
		plate = args[1];
		//register
		if(args.length>2)
		{
			name = args[2];
			sentence = new String(action + " " + plate + " " + name);
		}
		else sentence = new String(action + " " + plate);
		
		System.out.println(sentence);
		//Need to create new buffers 
		sendData = new byte[1024];
		receiveData = new byte[1024];
		


		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println("FROM SERVER:" + modifiedSentence);

		clientSocket.close();
	}
} 