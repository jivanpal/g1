
package ClientNetworking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import ClientNetworking.*;
import GeneralNetworking.Action;
import GeneralNetworking.Lobby;
import GeneralNetworking.LobbyList;
import GeneralNetworking.Player;


/**
 * The Class Client.
 * @author Svetlin © 
 */
public class Client extends Thread
{

	private int port = ClientVariables.PORT;
	private String hostname = ClientVariables.HOSTNAME;
	public Lobby lobby = null;
	private String name;
	public LinkedBlockingQueue<Object> clientQueue;
	public LobbyList lobbyList = null;

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
		Socket server = null;
		//lobby.addObserver();
		// get a socket and the 2 streams
		try
		{
			server = new Socket(hostname, port);
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
		try
		{
			toServer.writeObject(name);
			toServer.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ClientSender sender = new ClientSender(toServer, clientQueue);
		ClientReceiver receiver = new ClientReceiver(fromServer, name, lobby, clientQueue, lobbyList);

		// Start the sender and receiver threads
		sender.start();
		receiver.start();
	}

	public Lobby getLobby()
	{
		return lobby;
	}
	public void setLobby(Lobby l)
	{
		lobby = l;
	}

	public synchronized void send(Object obj)
	{
		clientQueue.offer(obj);
	}

	public void updateList()
	{
		clientQueue.offer(name);
	}

	public LobbyList getLobbyList()
	{
		return lobbyList;
	}
	public void kick(Player presser,Player kicked)
	{
		clientQueue.offer(new Action(lobby.getID(),presser,kicked,10));
	}
}
