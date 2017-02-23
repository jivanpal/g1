package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

import GameLogic.Map;
import GeneralNetworking.Lobby;

public class GameClient
{
	private int port = GameVariables.PORT;
	private InetAddress hostname = null;
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	GameClientReceiver receiver;
	GameClientSender sender;
	public GameClient(Lobby lobby)
	{
		for(int i=0;i<lobby.getPlayers().length;i++)
		{
			if(lobby.getPlayers()[i] != null &&  lobby.getPlayers()[i].isHost)
				hostname = lobby.getPlayers()[i].address;
		}
		System.out.println("Left for loop");
		System.out.println(hostname + " CLIENT ");
		
		// Open sockets:
		ObjectOutputStream toServer = null;
		ObjectInputStream fromServer = null;
		Socket server = null;

		// get a socket and the 2 streams
		try
		{
			server = new Socket(hostname,port);
			toServer = new ObjectOutputStream(server.getOutputStream());
			toServer.flush();
			fromServer = new ObjectInputStream(server.getInputStream());
			System.err.println("Created `fromServer` and 'toServer'");
		}
		catch (UnknownHostException e)
		{
			System.out.println("Unknown host exception");
			System.err.println("Unknown host: " + hostname);
			System.exit(1);
		}
		catch (IOException e)
		{
			System.out.println("IO exception");
			System.err.println("The server doesn't seem to be running " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		sender = new GameClientSender(toServer,queue);
		receiver = new GameClientReceiver(fromServer,queue);
		sender.start();
		receiver.start();
		System.out.println("end of gameclient const");
	}

	public synchronized void send(String str)
	{
		queue.offer(str);
	}
	public Map getMap()
	{
		 return receiver.getMap();
	};
	public void addObserver(Observer o)
	{
	    receiver.setObserver(o);
	}
}
