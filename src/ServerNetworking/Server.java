//Server Class
package ServerNetworking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import GeneralNetworking.Action;
import GeneralNetworking.Lobby;

public class Server
{
	
	public static ArrayList<Lobby> lobbies = new ArrayList<Lobby>();

	/**
	 * The main method.
	 */
	public static void main(String[] args)
	{
		//TODO import from somewhere??
		ClientTable clientTable= new ClientTable();

		// Open a server socket:
		ServerSocket serverSocket = null;
		try
		{
			serverSocket = new ServerSocket(ServerVariables.PORT);
		}
		catch (IOException e)
		{
			System.err.println("Couldn't listen on port " + ServerVariables.PORT);
			System.exit(1); // Give up.
		}

		try
		{
			while (true)
			{

				// Listen to the socket, accepting connections from new clients:
				Socket socket = serverSocket.accept();
				System.out.println("server");
				
				// This is to print o the server
				ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
				toClient.flush();
				// This is so that we can use readLine():
				ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());

				String nickname ="";
				while(true)
				{
					try
					{
						nickname = (String)fromClient.readObject();
						toClient.reset();
						if(clientTable.getQueue(nickname)!=null)
						{
							toClient.writeBoolean(false);
							toClient.flush();
						}
						else
						{
							toClient.writeBoolean(true);
							break;
						}
						toClient.flush();
					}
					catch (ClassNotFoundException e)
					{
						e.printStackTrace();
					}
				}
				clientTable.add(nickname);
				LinkedBlockingQueue<Object> queue = clientTable.getQueue(nickname); 
				System.out.println(nickname);
				
				// We create and start new threads to read from the
				// client(this one executes the commands):
	
				ServerReceiver clientInput = new ServerReceiver(fromClient,clientTable,lobbies,nickname);
				clientInput.start();
				
				// We create and start a new thread to write to the client:
				ServerSender clientOutput = new ServerSender(toClient,clientTable,nickname);
				clientOutput.start();
			}
		}
		catch (IOException e)
		{
			System.err.println("IO error " + e.getMessage());
		}
	}
}
