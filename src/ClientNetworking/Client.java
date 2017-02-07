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

import GeneralNetworking.Lobby;

// TODO: Auto-generated Javadoc
/**
 * The Class Client.
 */
class Client extends Thread
{

	private int port = ClientVariables.PORT;
	private String hostname = ClientVariables.HOSTNAME;
	private Lobby lobby = null;
	private String name;

	public Client(String nickname)
	{
		this.name = nickname;
	}

	public void run()
	{
		// Open sockets:
		ObjectOutputStream toServer = null;
		ObjectInputStream fromServer = null;
		Socket server = null;
		LinkedBlockingQueue<Object> clientQueue = new LinkedBlockingQueue<Object>();
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

		ClientSender sender = new ClientSender(toServer, clientQueue);
		ClientReceiver receiver = new ClientReceiver(fromServer, name, lobby, clientQueue);

		// Start the sender and receiver threads
		sender.start();
		receiver.start();

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

	}
}
