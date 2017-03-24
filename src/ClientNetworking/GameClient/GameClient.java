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
/**
 * 
 * @author Svetlin
 * The in-game client class
 * Automatically connects to the server and opens ports 
 */
public class GameClient
{
	private int port = GameVariables.PORT;
	private InetAddress hostname = null;
	public KeySequence keySequence;
	private LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
	GameClientReceiver receiver;
	GameClientSender sender;
	/**
	 * 
	 * @param lobby the game lobby
	 * @param player the name of the player(for this client)
	 */
	public GameClient(Lobby lobby, Player player)
	{
		//get the host's address
		this.hostname = lobby.getHostAddress();
		
		// Create the socket and the streams
		ObjectOutputStream toServer = null;
		ObjectInputStream fromServer = null;
		Socket server = null;

		// get the socket and the streams
		try
		{
			server = new Socket(hostname,port);
			toServer = new ObjectOutputStream(server.getOutputStream());
			toServer.flush();
			toServer.writeObject(player.nickname);
			fromServer = new ObjectInputStream(server.getInputStream());
			//receive the KeySequences from the server
			keySequence = (KeySequence)fromServer.readObject();
		}
		catch (UnknownHostException e)
		{
			System.exit(1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//Create receiver and sender threads and start them
		sender = new GameClientSender(toServer,queue);
		receiver = new GameClientReceiver(fromServer);
		sender.start();
		receiver.start();
	}

	/**
	 * Queue an object to be sent
	 * @param o the object
	 */
	public synchronized void send(Object o)
	{
		queue.offer(o);
	}
	/**
	 * retrieve the map from the receiver
	 * @return the map
	 */
	public Map getMap()
	{
		 return receiver.getMap();
	}
	/**
	 * add an observer to the map
	 * @param o the observer
	 */
	public void addObserver(Observer o)
	{
	    receiver.setObserver(o);
	}
	/**
	 * Get the last message received
	 * @return the message
	 */
	public String getMessage()
	{
		 return receiver.getMessage();
	};
	/**
	 * Add an observer to the chatContainer
	 * @param o the Observer
	 */
	public void addChatObserver(Observer o)
	{
	    receiver.addChatObserver(o);
	}
}
