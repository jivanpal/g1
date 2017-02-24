package GeneralNetworking;

import java.io.Serializable;
import java.util.Observable;

/**
 * 
 * @author Svetlin
 * A class containing an array of LobbyInfo-s 
 */
@SuppressWarnings("serial")
public class LobbyList extends Observable implements Serializable
{
	private LobbyInfo[] lobbyList = new LobbyInfo[1000];

	/**
	 * Constructor
	 * @param list the lobby info array
	 */
	public LobbyList(LobbyInfo[] list)
	{
		lobbyList = list;
	}
	/**
	 * A general constructor that doesn't require a lobby info array 
	 */
	public LobbyList()
	{
	}
	/**
	 * Get the lobbies
	 * @return the lobby array
	 */
	public LobbyInfo[] getLobbies()
	{
		return lobbyList;
	}
	/**
	 * Unused
	 * Change the lobby info array and notify observers
	 * @param lInfo the new lobby Info
	 */
	public void change(LobbyInfo[] lInfo)
	{
		lobbyList = lInfo;
		setChanged();
		notifyObservers();
	}
}
