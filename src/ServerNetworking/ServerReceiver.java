package ServerNetworking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import GeneralNetworking.Action;
import GeneralNetworking.Invite;
import GeneralNetworking.Lobby;
import GeneralNetworking.Player;

public class ServerReceiver extends Thread
{
	private ObjectInputStream clientIN;
	private ClientTable clientTable;
	private ArrayList<Lobby> lobbies;

	public ServerReceiver(ObjectInputStream reader, ClientTable cT,ArrayList<Lobby> lobbies)
	{
		clientIN = reader;
		clientTable = cT;
		this.lobbies = lobbies;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				Object inObject = clientIN.readObject();
				// INVITE
				if (inObject instanceof Invite)
				{
					Invite inv = (Invite) inObject;
					clientTable.getQueue(inv.nickname).offer(inv);
				}
				// LOBBY
				if (inObject instanceof Lobby)
				{
					Lobby lobby = (Lobby) inObject;
					lobbies.add(lobby);
				}
				//Action
				if(inObject instanceof Action)
				{
					Action a = (Action) inObject;
					for(int i=0;i<lobbies.size();i++)
					{
						if(lobbies.get(i).getID().equals(a.getLobby().getID()))
						{
							Lobby l = lobbies.get(i);
							int pos = a.getPos();
							Player p = a.getPlayer();
							//ADD PLAYER
							if(pos == 9)
							{
								l.add(p);
							}
							//MOVE PLAYER 
							else
							{
								l.move(p, pos);
							}
							Player[] players = l.getPlayers();
							for(int j=0;j<8;j++)
							{
								if(players[j] != null)
									clientTable.getQueue(players[j].nickname).offer(l);
							}
							break;
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}
}
