
package ClientNetworking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;
import GeneralNetworking.Action;
import GeneralNetworking.Lobby;
import GeneralNetworking.LobbyList;
import GeneralNetworking.Player;


/**
 * The Class Client.
 * @author Svetlin
 */
public class Client extends Thread
{

	private int port = ClientVariables.PORT;
	private String hostname = ClientVariables.HOSTNAME;
	public String name;
	public LinkedBlockingQueue<Object> clientQueue;
	private ClientReceiver receiver;
	private ClientSender sender;
	Socket server = null;

	/**
	 * Constructor
	 * @param nickname the player nickname
	 */
	public Client(String nickname)
	{
		this.name = nickname;
		clientQueue = new LinkedBlockingQueue<Object>();
	}


	public void run()
	{
		// Open sockets:
		ObjectOutputStream toServer = null;
		ObjectInputStream fromServer = null;

		
		// get a socket and the 2 streams
		try
		{
			server = new Socket(hostname, port);
			toServer = new ObjectOutputStream(server.getOutputStream());
			toServer.flush();
			fromServer = new ObjectInputStream(server.getInputStream());
		}
		catch (UnknownHostException e)
		{
			System.err.println("Unknown host: " + hostname);
			System.exit(1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("The server doesn't seem to be running " + e.getMessage());
			System.exit(1);
		}
		// tell our name to the server
		try
		{
			toServer.writeObject(name);
			toServer.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//Create the receiver and the sender
		sender = new ClientSender(toServer, clientQueue);
		receiver = new ClientReceiver(fromServer, name, clientQueue);

		// Start the sender and receiver threads
		sender.start();
		receiver.start();
		try {
			receiver.join();
			sender.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public void disconnect()
	{
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Get the Lobby
	 * @return the lobby
	 */
	public Lobby getLobby()
	{
		return receiver.getLobby();
	}
	/**
	 * Unused
	 * set the Lobby
	 * @param lobby the new Lobby
	 */
	public void setLobby(Lobby lobby)
	{
		receiver.setLobby(lobby);	
	}
	/**
	 * get the list of Lobbies
	 * @return the list
	 */
	public LobbyList getLobbyList()
	{
		return receiver.getList();
	}
	/**
	 * Send an object to the server
	 * @param obj the object to be sent
	 */
	public synchronized void send(Object obj)
	{
		clientQueue.offer(obj);
	}
	/**
	 * Ask the server to give us an updated Lobby list
	 */
	public void updateList()
	{
		clientQueue.offer(name);
	}
	/**
	 * Kick a player from the lobby
	 * @param presser the person who is kicking
	 * @param kicked the kicked person
	 */
	public void kick(Player presser,Player kicked)
	{
		clientQueue.offer(new Action(getLobby().getID(),presser,kicked,10));
	}
	/**
	 * Add an observer to the LobbyContainer in the receiver thread
	 * @param obs the observer
	 */
	public void addLobbyObserver(Observer obs)
	{
		System.out.println("HostLobbyPanel added as observer to ClientReceiver");
		receiver.addObserver(obs);
	}
	
	public void deleteLobbyObserver(Observer obs) {
		receiver.deleteObserver(obs);
	}
} 