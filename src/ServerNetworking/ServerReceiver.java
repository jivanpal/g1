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
	private String name;

	public ServerReceiver(ObjectInputStream reader, ClientTable cT, ArrayList<Lobby> lobbies,String nickname)
	{
		clientIN = reader;
		clientTable = cT;
		this.lobbies = lobbies;
		name = nickname;
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
					System.out.println("lobb count "+ lobbies.size());
				}
				// Action
				else if (inObject instanceof Action)
				{
					Action a = (Action) inObject;
					System.out.println("ACTION "+ a.getPos());
					for (int i = 0; i < lobbies.size(); i++)
					{
						if (lobbies.get(i).getID().equals(a.getID()))
						{
							Lobby l = lobbies.get(i);
							int pos = a.getPos();
							Player p = a.getPlayer();
							// ADD PLAYER
							if (pos == Action.ADD)
							{
								l.add(p);
							}
							// KICK PLAYER
							else if (pos == Action.KICK)
							{
								Player kicked = a.getKicked();
								l.kick(p, kicked);
								if(l.countPlayers()==0)
									lobbies.remove(i);
								clientTable.getQueue(kicked.nickname).offer(l);
							}
							// START GAME
							else if (pos == Action.START)
							{
								l.start(p);
								System.out.println("started lobby");
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
							if(pos == Action.START)
								lobbies.remove(i);
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
				System.out.println("A client disconnected");
				clientTable.remove(name);
				runs=false;
			}

		}
	}
}
