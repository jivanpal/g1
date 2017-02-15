package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

import ClientNetworking.ClientReceiver;
import ClientNetworking.ClientSender;
import ClientNetworking.ClientVariables;
import ClientNetworking.GameHost.GameVariables;
import GeneralNetworking.Lobby;

public class GameClient extends Thread
{
	private int port = GameVariables.PORT;
	private InetAddress hostname = null;
	private LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
	public GameClient(Lobby lobby)
	{
		for(int i=0;i<8;i++)
		{
			if(lobby.getPlayers()[i].isHost)
				hostname = lobby.getPlayers()[i].address;
		}
	}
	public void run()
	{
		// Open sockets:
		ObjectOutputStream toServer = null;
		ObjectInputStream fromServer = null;
		Socket server = null;

		// get a socket and the 2 streams
		try
		{
			server = new Socket(hostname,port);
			toServer = new ObjectOutputStream(server.getOutputStream());
			fromServer = new ObjectInputStream(server.getInputStream());
		}
		catch (UnknownHostException e)
		{
			System.err.println("Unknown host: " + hostname);
			System.exit(1);
		}
		catch (IOException e)
		{
			System.err.println("The server doesn't seem to be running " + e.getMessage());
			System.exit(1);
		}

		GameClientSender sender = new GameClientSender(toServer,queue);
		GameClientReceiver receiver = new GameClientReceiver(fromServer);

		// Start the sender and receiver threads
		sender.start();
		receiver.start();

		// Wait for them to end and close sockets.
		try
		{
			sender.join();
			receiver.join();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			System.exit(1);
		}

	}
	public synchronized void send(Object obj)
	{
		queue.offer(obj);
	}
}
