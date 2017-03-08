package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

import ClientNetworking.GameHost.KeySequence;
import GameLogic.Map;
import GeneralNetworking.Lobby;
import GeneralNetworking.Player;

public class GameClient
{
	private int port = GameVariables.PORT;
	private InetAddress hostname = null;
	public KeySequence keySequence;
	private LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
	GameClientReceiver receiver;
	GameClientSender sender;
	
	public GameClient(Lobby lobby, Player player)
	{
		this.hostname = lobby.getHostAddress();
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
			System.out.println("writing nickname to server");
			toServer.writeObject(player.nickname);
			fromServer = new ObjectInputStream(server.getInputStream());
			System.err.println("Created `fromServer` and 'toServer'");
			keySequence = (KeySequence)fromServer.readObject();
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
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		sender = new GameClientSender(toServer,queue);
		receiver = new GameClientReceiver(fromServer);
		sender.start();
		receiver.start();
		System.out.println("end of gameclient const");
	}

	public synchronized void send(Object o)
	{
		queue.offer(o);
	}
	public Map getMap()
	{
		 return receiver.getMap();
	};
	public void addObserver(Observer o)
	{
	    receiver.setObserver(o);
	}
	public String getMessage()
	{
		 return receiver.getMessage();
	};
	public void addChatObserver(Observer o)
	{
	    receiver.addChatObserver(o);
	}
}
