package GeneralNetworking;

import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
public class LobbyInfo implements Serializable
{
	public UUID lobbyID;
	public String host;
	public int playerCount;
	
	public LobbyInfo(UUID id,String hostname,int count)
	{
		lobbyID=id;
		host = hostname;
		playerCount = count;
	}
}
