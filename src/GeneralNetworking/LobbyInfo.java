package GeneralNetworking;

import java.util.UUID;

public class LobbyInfo
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
