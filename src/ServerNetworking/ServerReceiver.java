package ServerNetworking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import GeneralNetworking.Action;
import GeneralNetworking.Invite;
import GeneralNetworking.Lobby;
import GeneralNetworking.LobbyInfo;
import GeneralNetworking.LobbyList;
import GeneralNetworking.Player;

public class ServerReceiver extends Thread
{
	private ObjectInputStream clientIN;
	public ClientTable clientTable;
	public ArrayList<Lobby> lobbies;

	public ServerReceiver(ObjectInputStream reader, ClientTable cT, ArrayList<Lobby> lobbies)
	{
		clientIN = reader;
		clientTable = cT;
		this.lobbies = lobbies;
	}

	public void run()
	{
		boolean runs = true;
		while (runs)
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
				else if (inObject instanceof Lobby)
				{
					Lobby lobby = (Lobby) inObject;
					lobbies.add(lobby);
				}
				// Action
				else if (inObject instanceof Action)
				{
					Action a = (Action) inObject;
					for (int i = 0; i < lobbies.size(); i++)
					{
						if (lobbies.get(i).getID().equals(a.getID()))
						{
							Lobby l = lobbies.get(i);
							int pos = a.getPos();
							Player p = a.getPlayer();
							// ADD PLAYER
							if (pos == 9)
							{
								l.add(p);
							}
							// KICK PLAYER
							if (pos == 10)
							{
								Player kicked = a.getKicked();
								l.kick(p, kicked);
							}
							// MOVE PLAYER
							else
							{
								l.move(p, pos);
							}
							Player[] players = l.getPlayers();
							for (int j = 0; j < 8; j++)
							{
								if (players[j] != null)
									clientTable.getQueue(players[j].nickname).offer(l);
							}
							break;
						}
					}
				}
				// get lobby list
				else if (inObject instanceof String)
				{
					LobbyInfo[] infos = new LobbyInfo[lobbies.size()];
					System.out.println(lobbies.size());
					int i = 0, count = 0;
					String hostname = "";
					for (Lobby l : lobbies)
					{
						Player[] players = l.getPlayers();
						for (Player p : players)
							if (p != null)
							{
								count++;
								if (p.isHost)
									hostname = p.nickname;
							}
						infos[i++] = new LobbyInfo(l.getID(), hostname, count);
						count = 0;
					}
					LobbyList lList = new LobbyList(infos);
					clientTable.getQueue((String) inObject).offer(lList);
					//System.out.println(lList.getLobbies().length);
				}
			}
			catch (Exception e)
			{
				System.out.println("client disconnected");
				runs=false;
				e.printStackTrace();
			}

		}
	}
}
