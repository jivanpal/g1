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
	LobbyList lobbyList;

	ClientReceiver(ObjectInputStream reader, String name, Lobby clLobby, LinkedBlockingQueue<Object> queue,
			LobbyList lList)
	{
		nickname = name;
		fromServer = reader;
		clientLobby = clLobby;
		clientQueue = queue;
		lobbyList = lList;
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
						if (clientLobby.started)
							System.out.println("start game");
						clientLobby.change();
					}
					// INVITE
					else if (inObject instanceof Invite)
					{
						Invite inv = (Invite) inObject;
						// the 9 is used for the purpose of adding people
						clientQueue.offer(new Action(inv.lobby.getID(),
								new Player(nickname, InetAddress.getLocalHost(), false), 9));
					}
					// LobbyList
					else if (inObject instanceof LobbyList)
					{
						lobbyList = (LobbyList) inObject;

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
