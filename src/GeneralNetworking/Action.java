package GeneralNetworking;

import java.io.Serializable;
import java.util.UUID;
/**
 * 
 * @author Svetlin
 *		
 *	The Action class
 *	
 *	The Action object is dedicated to carrying relevant information needed for Lobby updates
 */
@SuppressWarnings("serial")
public class Action implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static int ADD = Lobby.LOBBY_SIZE + 1;
	public static int KICK = Lobby.LOBBY_SIZE + 2 ;
	public static int START = Lobby.LOBBY_SIZE + 3;
	private Player player, player2 = null;
	private int position;
	private UUID lobbyID;

	/**
	 * Regular action constructor
	 * @param lID the lobby ID
	 * @param p The player who is the target of the action
	 * @param pos the position for a player to be moved / the command to be executed (see Action.(KICK/ADD/START))
	 */
	public Action(UUID lID, Player p, int pos)
	{
		player = p;
		position = pos;
		lobbyID = lID;
	}

	/**
	 * A constructor for the kick function
	 * @param lID the lobby ID
	 * @param p the player who pressed the button
	 * @param p2 the target player
	 * @param pos the instruction
	 */
	public Action(UUID lID, Player p, Player p2, int pos)
	{
		player = p;
		player2 = p2;
		position = pos;
		lobbyID = lID;
	}
	/**
	 * Returns the Lobby ID
	 * @return the lobby ID of the target lobby
	 */
	public UUID getID()
	{
		return lobbyID;
	}

	/**
	 * Get the position/command number
	 * @return the position/command
	 */
	public int getPos()
	{
		return position;
	}

	/** 
	 * Get the target player / presser
	 * @return the player
	 */
	public Player getPlayer()
	{
		return player;
	}
	/**
	 * Get the person to be kicked
	 * @return the player to be kicked
	 */
	public Player getKicked()
	{
		return player2;
	}
}
