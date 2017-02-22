//Server Class
package ClientNetworking.GameHost;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import GeneralNetworking.Lobby;
import GeneralNetworking.Player;
import Geometry.Vector;
import Physics.Body;
import ServerNetworking.ClientTable;
import GameLogic.*;

public class MapServer extends Thread
{
	private final int PORT = 1273;
	private final Lobby lobby;
	private ServerSocket serverSocket = null;
	private Map gameMap = new Map(10000, 10000, 10000);

	public MapServer(Lobby l)
	{
		lobby = l;

		// Open a server socket:
		try
		{
			serverSocket = new ServerSocket(PORT,8,InetAddress.getLocalHost());
		}
		catch (IOException e)
		{
			System.err.println("Couldn't listen on port " + PORT);
		}
		System.out.println("MapServer is a go");
	}

	public void run()
	{
		try
		{
			ClientTable clientTable = new ClientTable();
			System.out.println(serverSocket.getInetAddress());
			
			System.out.println("I HAVE STARTED THE SERVER");
			while (true)
			{
				// Listen to the socket, accepting connectioxns from new clients:
				Socket socket = serverSocket.accept();
				System.out.println("SOMEONE JOINED LOL");
				InetAddress address = socket.getInetAddress();
			
				boolean gameShouldStart = false;		
				int pos = 0;
				String name = "";
				Player[] players = lobby.getPlayers();
				for (pos=0;pos<players.length;pos++)
				{
					if (players[pos]!= null && players[pos].address.equals(address))
					{
						name = players[pos].nickname;
						gameShouldStart = true;
						break;
					}
				}
				if (!gameShouldStart)
				{
					System.out.println("I CLOSED THE SOCKET XD");
					socket.close();
				}
				else
				{
					clientTable.add(""+pos);
					// This is to print o the server
					ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
					toClient.flush();
					// This is so that we can use readLine():
					InputStream fromClient = socket.getInputStream();

					// We create and start new threads to read from the
					// client(this one executes the commands):
					GameHostReceiver clientInput = new GameHostReceiver(fromClient, gameMap,clientTable,""+pos,name);
					clientInput.start();
					// We create and start a new thread to write to the client:
					GameHostSender clientOutput = new GameHostSender(toClient,gameMap,""+pos);
					clientOutput.start();
					
				}

			}
		}
		catch (IOException e)
		{
			System.err.println("IO error " + e.getMessage());
		}
	}
}
