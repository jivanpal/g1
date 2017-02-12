package GeneralNetworking;

public class LobbyList
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
