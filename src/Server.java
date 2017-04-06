import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends Thread
{
	ArrayList<String> clientNames = new ArrayList<String>();
	String command = "";
	Scanner commandReader = new Scanner(System.in);
	String clientName = "";
	int portNumber = 8090;
	boolean clientServer = false;
	File clientAddressFile;
	
	public Server()
	{
		
	}
	public Server(String address, int port)
	{
		portNumber = port;
		clientServer = true;
	}
	public void help()
	{
		System.out.println("The following commands are available.");
		System.out.println("list - lists the names of connected clients. ");
		System.out.println("select # - selects a client from the list");
	}
	
	public void list()
	{
		for(int i = 0; i < clientNames.size(); i++)
		{
			System.out.println(i + ": " + clientNames.get(i));
		}
		
	}

	public void connect(String address) throws Exception
	{
		//Socket serverSocket = new Socket(address, 8090);
		//DataOutputStream os = new DataOutputStream(serverSocket.getOutputStream());
		//os.writeBytes("help");
	}
	
	public void run() // listening for client connections
	{
		System.out.println("running");
		ServerSocket connection;
		try {
			connection = new ServerSocket(portNumber);
			while(true)
			{
				Socket connectionSocket = connection.accept();
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
						
				if(clientServer)
				{
					outToClient.writeBytes("Connected to host server.");
					//String welcomeMessage = inFromClient.readLine();  // welcome message for connecting to client server
					//System.out.println(welcomeMessage);
					
				}
				else // host server 
				{
					try
					{
						clientAddressFile = new File("clientAddressFile.txt");  // create file if it doesn't exist already
						clientAddressFile.createNewFile();
					}
					catch(Exception e)
					{
						
					}
					
					Scanner readFile = new Scanner(clientAddressFile);
					while(readFile.hasNextLine())
					{
						System.out.println(readFile.nextLine());
					}
					String clientAddress = inFromClient.readLine();  // get the client's address
					System.out.println("Connected to: " + clientAddress);
					
					clientNames.add(clientAddress);
					
					outToClient.writeBytes("8091");	// send port for new connection
					FileWriter fileWriter = new FileWriter(clientAddressFile);
					BufferedWriter bw = new BufferedWriter(fileWriter);
					bw.write(clientAddress+"\n");
					bw.close();
					readFile.close();
					connectionSocket.close();
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) 
	{
		Server s = new Server();
		System.out.println("Starting server");
		s.start();
		
		
		//System.out.println("connected");
		
		
		
		
		
		

	}

}
