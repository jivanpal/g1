package GeneralNetworking;

/**
 * 
 * @author Svetlin
 *	An invite to join the lobby
 */
public class Invite
{
	public Lobby lobby;
	public String nickname;
	/**
	 * 
	 * @param l the lobby
	 * @param name the player to be invited
	 */
	public Invite(Lobby l,String name)
	{
		lobby=l;
		nickname=name;
	}
	
}
