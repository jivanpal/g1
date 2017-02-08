package ClientNetworking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import GeneralNetworking.*;

public class ClientReceiver extends Thread
{

	ObjectInputStream fromServer;
	Lobby clientLobby;
	private String nickname;
	private LinkedBlockingQueue<Object> clientQueue;
	ClientReceiver(ObjectInputStream reader,String name, Lobby clLobby, LinkedBlockingQueue<Object> queue)
	{
		nickname = name;
		fromServer = reader;
		clientLobby = clLobby;
		clientQueue = queue;
	}

	public void run()
	{

		try
		{
			while (true)
			{
				try
				{
					Object inObject = fromServer.readObject();
					// LOBBY
					if (inObject instanceof Lobby)
					{
						clientLobby = (Lobby) inObject;
						if(clientLobby.started)
							System.out.println("start game");
					}
					// INVITE
					if (inObject instanceof Invite)
					{
						//if accepted
						clientLobby.add(new Player(nickname,InetAddress.getLocalHost(),false));
						clientQueue.offer(clientLobby);
						//add to queue
					}
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("Server seems to have died " + e.getMessage());
			System.exit(1); // Give up.
		}
	}
}
