package GeneralNetworking;

import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
/**
 * 
 * @author Svetlin
 * A class containing information for a lobby
 */
public class LobbyInfo implements Serializable
{
	public UUID lobbyID;
	public String host;
	public int playerCount;
	
	/**
	 * Constructor
	 * @param id the lobby ID
	 * @param hostname the name of the host
	 * @param count the player count
	 */
	public LobbyInfo(UUID id,String hostname,int count)
	{
		lobbyID=id;
		host = hostname;
		playerCount = count;
	}
}
