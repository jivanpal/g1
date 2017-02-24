package GeneralNetworking;

import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
public class Action implements Serializable
{
	public static int ADD = Lobby.LOBBY_SIZE + 1;
	public static int KICK = Lobby.LOBBY_SIZE + 2 ;
	public static int START = Lobby.LOBBY_SIZE + 3;
	private Player player, player2 = null;
	private int position;
	private UUID lobbyID;

	public Action(UUID lID, Player p, int pos)
	{
		player = p;
		position = pos;
		lobbyID = lID;
	}

	public Action(UUID lID, Player p, Player p2, int pos)
	{
		player = p;
		player2 = p2;
		position = pos;
		lobbyID = lID;
	}

	public UUID getID()
	{
		return lobbyID;
	}

	public int getPos()
	{
		return position;
	}

	public Player getPlayer()
	{
		return player;
	}

	public Player getKicked()
	{
		return player2;
	}
}
