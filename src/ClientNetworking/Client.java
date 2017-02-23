
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
 * @author Svetlin � 
 */
public class Client extends Thread
{

	private int port = ClientVariables.PORT;
	private String hostname = ClientVariables.HOSTNAME;
	public String name;
	public LinkedBlockingQueue<Object> clientQueue;
	private ClientReceiver receiver;
	private ClientSender sender;

	public Client(String nickname)
	{
		this.name = nickname;
		clientQueue = new LinkedBlockingQueue<Object>();
	}

	//what's the point of this thing being a thread? 
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
		try
		{
			toServer.writeObject(name);
			toServer.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		sender = new ClientSender(toServer, clientQueue);
		receiver = new ClientReceiver(fromServer, name, clientQueue);

		// Start the sender and receiver threads
		sender.start();
		receiver.start();
		try {
			receiver.join();
			sender.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Lobby getLobby()
	{
		return receiver.getLobby();
	}
	public void setLobby(Lobby lobby)
	{
		receiver.setLobby(lobby);	
	}
	public LobbyList getLobbyList()
	{
		return receiver.getList();
	}
	public synchronized void send(Object obj)
	{
		clientQueue.offer(obj);
	}

	public void updateList()
	{
		clientQueue.offer(name);
	}

	public void kick(Player presser,Player kicked)
	{
		clientQueue.offer(new Action(getLobby().getID(),presser,kicked,10));
	}
	public void addLobbyObserver(Observer obs)
	{
		System.out.println("HostLobbyPanel added as observer to ClientReceiver");
		receiver.addObserver(obs);
	}
} 