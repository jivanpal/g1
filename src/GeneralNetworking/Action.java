package GeneralNetworking;

import java.util.UUID;

public class Action {

	private Player player;
	private int position;
	private UUID lobbyID;
	public Action(UUID lID, Player p, int pos)
	{
		player =p;
		position=pos;
		lobbyID=lID;
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
}
