// Usage:
//        java Client user-nickname port hostname
//
// After initializing and opening appropriate sockets, we start two
// client threads, one to send messages, and another one to get
// messages.
//
//
// Another limitation is that there is no provision to terminate when
// the server dies.
package ClientNetworking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import ClientNetworking.*;

import GeneralNetworking.Lobby;
import GeneralNetworking.LobbyList;

// TODO: Auto-generated Javadoc
/**
 * The Class Client.
 */
public class Client extends Thread
{

	private int port = ClientVariables.PORT;
	private String hostname = ClientVariables.HOSTNAME;
	private Lobby lobby = null;
	private String name;
	LinkedBlockingQueue<Object> clientQueue;
	private LobbyList lobbyList = null;
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
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ClientSender sender = new ClientSender(toServer, clientQueue);
		ClientReceiver receiver = new ClientReceiver(fromServer, name, lobby, clientQueue,lobbyList);

		// Start the sender and receiver threads
		sender.start();
		receiver.start();
/*
		// Wait for them to end and close sockets.
		try
		{
			sender.join();
			toServer.close();
			receiver.join();
			fromServer.close();
			server.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			System.exit(1);
		}
*/
	}
	public Lobby getLobby()
	{	
		return lobby;
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
}
