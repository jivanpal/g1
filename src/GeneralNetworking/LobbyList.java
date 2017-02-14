package GeneralNetworking;

import java.io.Serializable;
import java.util.Observable;

@SuppressWarnings("serial")
public class LobbyList extends Observable implements Serializable
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
	public void change()
	{
		setChanged();
		notifyObservers();
	}
}
