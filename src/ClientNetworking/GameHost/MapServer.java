//Server Class
package ClientNetworking.GameHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import GeneralNetworking.Lobby;
import GeneralNetworking.Player;



public class MapServer extends Thread
{
	private final int PORT = 1273;
	private final Lobby lobby;
	private ServerSocket serverSocket = null;
	public MapServer(Lobby l)
	{
		lobby = l;
		// Open a server socket:
		try
		{
			serverSocket = new ServerSocket(PORT);
		}
		catch (IOException e)
		{
			System.err.println("Couldn't listen on port " + PORT);
		}
	}
	public void run()
	{
		try
		{
			LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
			while (true)
			{
				// Listen to the socket, accepting connections from new clients:
				Socket socket = serverSocket.accept();
				boolean flag = false;
				for(Player player : lobby.getPlayers())
				{
					if(player.address == socket.getInetAddress())
						flag = true;
				}
				if(!flag)
				{
					socket.close();
				}
				else{
					// This is so that we can use readLine():
					ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());

					// This is to print o the server
					ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
					
					// We create and start new threads to read from the
					// client(this one executes the commands):
		
					GameHostReceiver clientInput = new GameHostReceiver(fromClient);
					clientInput.start();
					
					// We create and start a new thread to write to the client:
					GameHostSender clientOutput = new GameHostSender(toClient,queue);
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
