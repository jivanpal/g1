package GeneralNetworking;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

@SuppressWarnings("serial")
public class Lobby implements Serializable
{
	public static int LOBBY_SIZE = 8;
	private UUID id;
	public boolean started ;
	private Player[] players = new Player[LOBBY_SIZE];

	/**
	 * nickname - host nickname hA - host address (get it using
	 * InetAddress.getLocalHost(), java.net.InetAddress needed)
	 */
	// private
	// a game lobby allows up to 8 players
	//
	public Lobby(String nickname, InetAddress hostAddress)
	{
		started = false;
		id = UUID.randomUUID(); 
		players[0] = new Player(nickname, hostAddress, true);
		for (int i = 1; i < LOBBY_SIZE; i++)
		{
			players[i] = null;
		}
	}

	public void add(Player invitee)
	{
		int i = 0;
		while (i < LOBBY_SIZE)
		{
			if (players[i] == null)
			{
				players[i] = invitee;
				break;
			}
			i++;
		}
		if (i == LOBBY_SIZE)
		{
			System.out.println("ROOM IS FULL");
		}
	}

	/**
	 * 
	 * @param p
	 *            the player to move
	 * @param pos
	 *            the position to which he should be moved
	 */
	public void move(Player p, int pos)
	{
		if (pos > LOBBY_SIZE-1 || pos < 0)
			System.out.println("INVALID POSITION NUMBER");
		else
		{
			for (int i = 0; i < LOBBY_SIZE; i++)
			{
				if (players[i]!=null && players[i].equals(p))
				{
					if (players[pos] == null)
					{
						players[pos] = p;
						players[i] = null;
						break;
					}
					else
					{
						System.out.println("INVALID MOVING SPOT");
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param player
	 *            the player who pressed the start button
	 */
	public void start(Player p)
	{
		if (p.isHost)
		{
			System.out.println("Lobby class, started is true");
			started = true;
		}
	}
	/**
	 * 
	 * @param presser the player who pressed the leave/kick button
	 * @param leaver the person to leave/be kicked
	 */
	public void kick(Player presser, Player leaver)
	{
		if (presser.equals(leaver) || presser.isHost)
		{
			for (int i = 0; i < LOBBY_SIZE; i++)
			{
				
				if(players[i] != null && players[i].equals(leaver))
				{
					if(players[i].isHost)
					 	{
							players[i]=null;
							for (int j = 0; j < LOBBY_SIZE; j++)
								if(players[j]!=null)
								{
									players[j].isHost=true;
									break;
								}
					 	}
					else players[i]=null;
				}
				
			}
		}
	}
	public Player[] getPlayers()
	{
		return players;
	}
	public Player getHost()
	{
		for(Player player : players )
		{
			if(player.isHost)
				return player;
		}
		return null;
	}
	public UUID getID()
	{
		return id;
	}
	
	public InetAddress getHostAddress(){
		InetAddress hostAddress = null;
		for(int i=0;i<LOBBY_SIZE;i++){
			if(players[i] != null &&  players[i].isHost){
				hostAddress = players[i].address;
			}
		}
		return hostAddress;
	}
	
	public int countPlayers()
	{
		int c=0;
		for(Player p:players)
		{
			if (p!=null)
				c++;
		}
		return c;
	}
	public int getPlayerPosByName(String name){
		for(int i =0; i< LOBBY_SIZE;i++){
			if(players[i].nickname.equals(name)){
				return i;
			}
		}
		return -1;
	}
}

