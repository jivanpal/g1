package ClientNetworking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;
import GeneralNetworking.Action;
import GeneralNetworking.Invite;
import GeneralNetworking.Lobby;
import GeneralNetworking.LobbyList;
import GeneralNetworking.Player;
/**
 * 
 * @author Svetlin
 * A thread that listens from the server
 * Also stores the current lobby
 */
public class ClientReceiver extends Thread
{

	ObjectInputStream fromServer;
	public LobbyContainer clientLobby = new LobbyContainer();
	private String nickname;
	public LinkedBlockingQueue<Object> clientQueue;
	public LobbyList lobbyList;
	/**
	 * Constructor
	 * @param reader the input stream from the server
	 * @param name the player nickname
	 * @param queue the queue from which objects are sent to the server
	 */
	ClientReceiver(ObjectInputStream reader, String name, LinkedBlockingQueue<Object> queue)
	{
		nickname = name;
		fromServer = reader;
		clientQueue = queue;
		lobbyList = new LobbyList();
	}

	public void run()
	{

		try
		{
			while (true)
			{
				try
				{
					//read an object from the server and typecast it
					Object inObject = fromServer.readObject();
					// LOBBY
					if (inObject instanceof Lobby)
					{
						Lobby lobby = (Lobby) inObject;
						clientLobby.setLobby(lobby);
					}
					// INVITE
					else if (inObject instanceof Invite)
					{
						Invite inv = (Invite) inObject;
						// the 9 is used for the purpose of adding people
						clientQueue.offer(new Action(inv.lobby.getID(),
								new Player(nickname, IpGetter.getRealIp(), false), Action.ADD));
					}
					// LobbyList
					else if (inObject instanceof LobbyList)
					{
						LobbyList lList = (LobbyList) inObject;
						lobbyList.change(lList.getLobbies());
					}
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			// System.exit(1); // Give up.
		}
	}
	/**
	 * get the Lobby List
	 * @return the list
	 */
	public LobbyList getList()
	{
		return lobbyList;
	}
	/**
	 * Get the Lobby
	 * @return the lobby
	 */
	public Lobby getLobby()
	{
		return clientLobby.getLobby();
	}
	/**
	 * set the lobby
	 * @param l the lobby
	 */
	public void setLobby(Lobby l)
	{
		clientLobby.setLobby(l);
	}
	/**
	 * Add an observer to the Lobby Container
	 * @param obs the observer
	 */
	public void addObserver(Observer obs)
	{
		clientLobby.addObserver(obs);
	}
	
	public void deleteObserver(Observer obs) {
		clientLobby.deleteObserver(obs);
	}
}
/**
 * 
 * @author Svetlin
 * the Lobby Container
 * allows us to attach Observers to the lobby without sending them along with the lobby to the server
 */
class LobbyContainer extends Observable
{
	private Lobby lobby = null;
	/**
	 * Constructor
	 */
	public LobbyContainer()
	{
	}
	/**
	 * set the lobby, notify the observers
	 * @param l the new lobby
	 */
	public void setLobby(Lobby l)
	{
		lobby=l;
		setChanged();
		notifyObservers();
	}
	/**
	 * get the Lobby
	 * @return the lobby
	 */
	public Lobby getLobby()
	{
		return lobby;
	}
}