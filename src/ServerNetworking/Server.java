//Server Class
package ServerNetworking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Server
{

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
				String nickname ="";
				LinkedBlockingQueue<Object> queue = clientTable.getQueue(nickname); 
				// Listen to the socket, accepting connections from new clients:
				Socket socket = serverSocket.accept();

				// This is so that we can use readLine():
				ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());

				// This is to print o the server
				ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
				
				// We create and start new threads to read from the
				// client(this one executes the commands):
	
				ServerReceiver clientInput = new ServerReceiver(fromClient);
				clientInput.start();
				
				// We create and start a new thread to write to the client:
				ServerSender clientOutput = new ServerSender(toClient,queue);
				clientOutput.start();
			}
		}
		catch (IOException e)
		{
			System.err.println("IO error " + e.getMessage());
		}
	}
}
