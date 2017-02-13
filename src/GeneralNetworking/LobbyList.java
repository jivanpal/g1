package GeneralNetworking;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LobbyList implements Serializable
{
	private LobbyInfo[] lobbyList;

	public LobbyList(LobbyInfo[] list)
	{
		lobbyList = list;
	}

	public LobbyInfo[] getLobbies()
	{
		return lobbyList;
	}
}
