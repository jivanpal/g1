package ClientNetworking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import ClientNetworking.GameClient.GameClient;
import GeneralNetworking.*;

public class ClientReceiver extends Thread
{

	ObjectInputStream fromServer;
	public LobbyContainer clientLobby = new LobbyContainer();
	private String nickname;
	public LinkedBlockingQueue<Object> clientQueue;
	public LobbyList lobbyList;

	ClientReceiver(ObjectInputStream reader, String name, LinkedBlockingQueue<Object> queue)
	{
		nickname = name;
		fromServer = reader;
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
						clientLobby.setLobby((Lobby) inObject);
						if (clientLobby.getLobby().started)
						{
							GameClient gameClient = new GameClient(clientLobby.getLobby())
						}
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
						System.out.println("clientrec: "+lList.getLobbies().length);
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
	public Lobby getLobby()
	{
		return clientLobby.getLobby();
	}
	public void setLobby(Lobby l)
	{
		clientLobby.setLobby(l);
	}
	public void addChangeListener(LobbyListener a)
	{
		clientLobby.addChangeListener(a);
	}
}

class LobbyContainer
{
	private Lobby lobby = null;
	private ArrayList<LobbyListener> listeners = new ArrayList<>();
	public LobbyContainer()
	{
	}
	public void setLobby(Lobby l)
	{
		lobby=l;
		for(LobbyListener lListener : listeners)
		{
			lListener.lobbyChanged();
		}
	}
	public Lobby getLobby()
	{
		return lobby;
	}
	public void addChangeListener(LobbyListener a)
	{
		listeners.add(a);
	}
}