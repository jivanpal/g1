package GeneralNetworking;

public class Action {

	private Player player;
	private int position;
	private Lobby lobby;
	public Action(Lobby l, Player p, int pos)
	{
		player =p;
		position=pos;
		lobby=l;
	}
	public Lobby getLobby()
	{
		return lobby;
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
