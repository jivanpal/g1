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
		System.out.println(lobbyList.length);
		return lobbyList;
	}
	public void change()
	{
		setChanged();
		notifyObservers();
	}
}
