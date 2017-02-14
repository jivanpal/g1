package ClientNetworking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import GeneralNetworking.*;

public class ClientReceiver extends Thread
{

	ObjectInputStream fromServer;
	public Lobby clientLobby;
	private String nickname;
	public LinkedBlockingQueue<Object> clientQueue;
	public LobbyList lobbyList;

	ClientReceiver(ObjectInputStream reader, String name, Lobby clLobby, LinkedBlockingQueue<Object> queue)
	{
		nickname = name;
		fromServer = reader;
		clientLobby = clLobby;
		clientQueue = queue;
		lobbyList = new LobbyList();
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
						LobbyList lList = (LobbyList) inObject;
						lobbyList.change(lList.getLobbies());
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
	public LobbyList getList()
	{
		return lobbyList;
	}
}
