package GeneralNetworking;

import java.util.UUID;

public class Action {

	private Player player,player2=null;	
	private int position;
	private UUID lobbyID;
	public Action(UUID lID, Player p, int pos)
	{
		player =p;
		position=pos;
		lobbyID=lID;
	}
	public Action(UUID lID, Player p,Player p2, int pos)
	{
		player =p;
		player2 = p2;
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
	public Player getKicked()
	{
		return player2;
	}
}
