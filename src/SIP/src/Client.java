import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
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
	
	/*
	Connect to amazon to get public ip address of client.
	*/
	public String getIPAddress() throws Exception
	{
		URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try 
        {
            in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } 
        finally 
        {
            if (in != null) 
            {
                try 
                {
                    in.close();
                } 
                catch (IOException e) 
                {
                    //e.printStackTrace();
                }
            }
        }
	}
	public void commandList()
	{
		System.out.println("disconnect");
		System.out.println("connect");
		System.out.println("exit");
		System.out.println("help");
	}
	public String parseCommand(String command)
	{
		if(command.equalsIgnoreCase("help"))
		{
			System.out.println("For a list of commands, type list");
			return null;
		}
		else if(command.equalsIgnoreCase("list"))
		{
			System.out.println("The following commands are available.");
			return "";
		}
		else if(command.equalsIgnoreCase("disconnect"))
		{
			return "disconnect";
		}
		else if(command.equalsIgnoreCase("connect"))
		{
			return "connect";
		}
		else
		{
			return "";
		}
	}
	
	//
	// FIX RUN SECTION. IT IS NOT WEB SERVER. IT DOES NOT NEED SERVERSOCKET
	//
	public void run() // open the socket
	{
		System.out.println("running");
		
		try {
				//s.clientName = inFromClient.readLine();
				//s.clientNames.add(s.clientName);
				String portNumber = "0000";
				
			
				if(!isClientServer) // if the client isn't on server side
				{
					try
					{
						clientPort = 8090;
						clientSocket = new Socket(serverAddress, 8090);
						System.out.println("8090 client");
						
					}
					catch(BindException e)
					{
						isClientServer = true;
					}
					
					DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
					BufferedReader serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					
					os.writeBytes("client"+"\n");
							
					try
					{
						clientAddress = getIPAddress();
					}
					catch(Exception e)
					{
						
					}
					System.out.println("Public ip: "+clientAddress);				
					
					
					System.out.println("Sending the client address: " + clientAddress);
					os.writeBytes(clientAddress + "\n");  // send the ip address to the server
					//System.out.println("Wrote bytes");
					//os.writeBoolean(true);
					portNumber = serverInput.readLine(); // receive port number from server
					
					System.out.println("closing client");
					clientSocket.close();
					port = Integer.parseInt(portNumber);
					
					Server s = new Server(clientAddress, port);
					System.out.println("Starting server...");
					s.start();
					
				}
				
				if(isClientServer) // client on the server side
				{
					System.out.println("8091");
					try
					{
						clientPort = 8091;
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
						serverAddress = address.nextLine(); // set server address to selected client address
						
						System.out.println("You chose: " + serverAddress);
						
						address.close();
						clientSocket = new Socket(serverAddress, 8091);
						
						DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
						BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						
						String con = inFromClient.readLine();
						System.out.println(con);
						os.writeBytes("host client" + "\n");
						String welcomeMessage = inFromClient.readLine();
						System.out.println(welcomeMessage);
						System.out.println();
						System.out.println("Enter command. For help enter: help");
						
						Scanner userInput = new Scanner(System.in);
						String command = "";
						while(!command.equalsIgnoreCase("exit"))
						{
							command = userInput.nextLine();
							String commandResult = parseCommand(command);
							
							if(commandResult == null)
							{
								
							}
							
						}
						
						
						
						os.writeBytes("");
						
					}
					catch(Exception e)
					{
						
					}
				}
			
				//outToServer.writeBytes("Connected to client's server.");
			
			
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
		
		
		
	}

}
