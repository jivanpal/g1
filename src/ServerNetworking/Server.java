//Server Class
package ServerNetworking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import GeneralNetworking.Lobby;

public class Server
{

	public static ArrayList<Lobby> lobbies = new ArrayList<Lobby>();

	/**
	 * The main method.
	 */
	public static void main(String[] args)
	{
		// TODO import from somewhere??
		ClientTable clientTable = new ClientTable();

		// Open a server socket:
		ServerSocket serverSocket = null;
		try
		{
			serverSocket = new ServerSocket(ServerVariables.PORT);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1); // Give up.
		}

		try
		{
			while (true)
			{

				// Listen to the socket, accepting connections from new clients:
				Socket socket = serverSocket.accept();

				// This is to print o the server
				ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
				// This is so that we can use readLine():
				ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());

				String nickname = "";
				String taggedName = "";
				
				try
				{
					nickname = (String) fromClient.readObject();
					toClient.reset();
					int i = 1;
					taggedName = nickname + "#" + i;
					while (true)
					{
						if (clientTable.getQueue(taggedName) != null)
						{
							i++;
							taggedName = nickname + "#" + i;
						}
						else
							break;
					}

					toClient.writeObject(taggedName);
					toClient.flush();
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}

				clientTable.add(taggedName);

				// We create and start new threads to read from the
				// client(this one executes the commands):

				ServerReceiver clientInput = new ServerReceiver(fromClient, clientTable, lobbies, taggedName);
				clientInput.start();

				// We create and start a new thread to write to the client:
				ServerSender clientOutput = new ServerSender(toClient, clientTable, taggedName);
				clientOutput.start();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
