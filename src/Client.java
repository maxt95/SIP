import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread
{
	private int port;
	private String clientAddress;
	Socket clientSocket;
	String serverAddress = "kepler.covenant.edu";
	int clientPort;
	boolean isClientServer = false;
	public Client()
	{
		
	}
	//
	// FIX RUN SECTION. IT IS NOT WEB SERVER. IT DOES NOT NEED SERVERSOCKET
	//
	public void run() // open the socket
	{
		System.out.println("running");
		ServerSocket connection;
		try {
			
			
			while(true)
			{
				
				//s.clientName = inFromClient.readLine();
				//s.clientNames.add(s.clientName);
				String portNumber = "0000";
				
				
				if(!isClientServer) // if the client is not running a server
				{
					portNumber = inFromServer.readLine();
					System.out.println("Read port number from server");
				}
				else
				{
					//String welcomeMessage = inFromServer.readLine();  // get the 1st message from client server
					//System.out.println(welcomeMessage);
					//outToServer.writeBytes("connected");
					System.out.println("closing client");
					clientSocket.close();
					
				}
				
			
				if(isClientServer) // if connected to client's server
				{
					System.out.println("connected to client's server.");
					//outToServer.writeBytes("Connected to client's server.");
				}
				else
				{
					System.out.println("closing client");
					clientSocket.close();
					port = Integer.parseInt(portNumber);
					
					Server s = new Server(clientAddress, port);
					System.out.println("starting server...");
					s.start();
				}
				
				//outToServer.writeBytes("Connected to client's server.");
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String... args) throws Exception
	{
		
		Client c = new Client();
		c.start();
		if(args.length > 0)
		{
			System.out.println("clientserver");
			c.isClientServer = true;
		}
		if(!c.isClientServer)
		{
			try
			{
				c.clientPort = 8090;
				c.clientSocket = new Socket(c.serverAddress, 8090);
				System.out.println("8090 client");
				
			}
			catch(BindException e)
			{
				c.isClientServer = true;
			}
		}
		
		if(c.isClientServer)	
		{
			System.out.println("8091");
			try
			{
				c.clientPort = 8091;
				System.out.println("8091");
				
				File clientAddresses = new File("clientAddressFile.txt");
				Scanner readFile = new Scanner(clientAddresses);
				while(readFile.hasNext())
				{
					System.out.println(readFile.nextLine());
				}
				System.out.println();
				System.out.println("Enter address to connect to. ");
				Scanner address = new Scanner(System.in);
				c.serverAddress = address.nextLine(); // set server address to selected client address
				System.out.println(c.serverAddress);
				address.close();
				c.clientSocket = new Socket(c.serverAddress, 8091);
			
			}
			catch(Exception e)
			{
				
			}
		}
		
		
		if(!c.isClientServer)
		{
			DataOutputStream os = new DataOutputStream(c.clientSocket.getOutputStream());
			//BufferedReader commandFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			c.clientAddress = InetAddress.getLocalHost().getHostAddress();
			//String command = commandFromServer.readLine();
			System.out.println(c.clientAddress);
			os.writeBytes(c.clientAddress);
			//os.writeBoolean(true);
		}
		
		
		
		
		
		
	}

}
