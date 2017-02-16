package GeneralNetworking;

import java.io.Serializable;
import java.util.Observable;

@SuppressWarnings("serial")
public class LobbyList extends Observable implements Serializable
{
	private LobbyInfo[] lobbyList = new LobbyInfo[1000];

	public LobbyList(LobbyInfo[] list)
	{
		lobbyList = list;
	}
	public LobbyList()
	{
		
	}
	public LobbyInfo[] getLobbies()
	{
		return lobbyList;
	}
	public void change(LobbyInfo[] lInfo)
	{
		lobbyList = lInfo;
		setChanged();
		notifyObservers();
	}
}
