package GeneralNetworking;

import java.net.InetAddress;
import java.util.UUID;

public class Lobby
{
	private int hostPos = 0;
	private UUID id;
	public boolean started ;
	private Player[] players = new Player[8];

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
		for (int i = 1; i < 8; i++)
		{
			players[i] = null;
		}
	}

	public void add(Player invitee)
	{
		int i = 0;
		while (i < 8)
		{
			if (players[i] == null)
			{
				players[i] = invitee;
				break;
			}
			i++;
		}
		if (i == 8)
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
		if (pos > 7 || pos < 0)
			System.out.println("INVALID POSITION NUMBER");
		else
		{
			for (int i = 0; i < 8; i++)
			{
				if (players[i].equals(p))
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
			for (int i = 0; i < 8; i++)
			{
				if(players[i].equals(leaver))
					players[i]=null;
				//NOTIFY
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
}
