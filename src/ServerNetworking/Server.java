//Server Class
package ServerNetworking;

import java.net.*;
import java.io.*;

public class Server
{

	/**
	 * The main method.
	 */
	public static void main(String[] args)
	{

		// This will be shared by the server threads:
		if (args.length != 1)
		{
			System.err.println("Usage: java Server Port Number");
			System.exit(1); // Give up.
		}

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

				// This is so that we can use readLine():
				BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// This is to print o the server
				PrintStream toClient = new PrintStream(socket.getOutputStream());

				// We create and start new threads to read from the
				// client(this one executes the commands):

				ServerReceiver positionReciever = new ServerReceiver(fromClient);
				positionReciever.start();
	
				ServerReceiver commandReciever = new ServerReceiver(fromClient);
				commandReciever.start();
				

				// We create and start a new thread to write to the client:
				ServerSender mapUpdater = new ServerSender(toClient);
				mapUpdater.start();
			}
		}
		catch (IOException e)
		{
			// Lazy approach:
			System.err.println("IO error " + e.getMessage());
			// A more sophisticated approach could try to establish a new
			// connection. But this is beyond this simple exercise.
		}
	}
}
