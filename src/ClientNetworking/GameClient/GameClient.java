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

public class GameClient extends Thread
{
	private int port = GameVariables.PORT;
	private InetAddress hostname = null;
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	GameClientReceiver receiver;
	GameClientSender sender;
	public GameClient(Lobby lobby)
	{
		for(int i=0;i<8;i++)
		{
			if(lobby.getPlayers()[i] == null) {
				continue;
			} else if(lobby.getPlayers()[i].isHost)
				hostname = lobby.getPlayers()[i].address;
		}
		System.out.println(hostname + " CLIENT ");
		
		// Open sockets:
		OutputStream toServer = null;
		ObjectInputStream fromServer = null;
		Socket server = null;

		// get a socket and the 2 streams
		try
		{
			server = new Socket(hostname,port);
			System.out.println("b4 getoutputstream");
			toServer = server.getOutputStream();
			System.out.println("couldnt get fromserver" + (toServer == null));
			fromServer = new ObjectInputStream(server.getInputStream());
			System.out.println("??????????????????????????????????????????? "+ (fromServer==null));
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
		System.out.println("b4 init");
		sender = new GameClientSender(toServer,queue);
		System.out.println("sender didnt fuck it up");
		receiver = new GameClientReceiver(fromServer,queue);
		System.out.println("b4 start");
		sender.start();
		System.out.println("sender didn't fuck it up 2");
		receiver.start();
		System.out.println("end of gameclient const");
	}
	public void run()
	{
		System.out.println("does it start");
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
