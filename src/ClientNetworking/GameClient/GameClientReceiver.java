package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observable;
import java.util.Observer;

import ClientNetworking.GameHost.ChatMessage;
import GameLogic.Map;
/**
 * 
 * @author Svetlin
 * The receiver for the in-game client
 */
public class GameClientReceiver extends Thread
{
	ObjectInputStream fromServer;
	private MapContainer gameMap = new MapContainer();
	private ChatContainer chatContainer = new ChatContainer();
	private boolean running=true;
	
	/**
	 * Constructor
	 * @param reader  the input stream from the server
	 */
	GameClientReceiver(ObjectInputStream reader)
	{
		fromServer = reader;
	}

	public void run()
	{
		try
		{
			while (running)
			{
				try
				{
					//receive an object
					Object inObject = fromServer.readObject();
					//MAP -> update the local map
					if(inObject instanceof Map)
						{
							gameMap.setMap((Map) inObject);
						}
					//ChatMessage - update the last message received
					else
					{
						ChatMessage m =(ChatMessage)inObject;
						String message = m.nickname + ": " + m.message; 
						chatContainer.setMessage(message);
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
			running = false;
			//System.exit(1); // Give up.
		}
	}
	/**
	 * Get the map
	 * @return the map
	 */
	public Map getMap()
	{
		return gameMap.getMap();
	}
	
	/**
	 * set an observer for the map
	 * @param o the Observer
	 */
	public void setObserver(Observer o)
	{
		gameMap.addObserver(o);
	}
	
	/**
	 * get the last message received
	 * @return the message (sender: message)
	 */
	public String getMessage()
	{
		return chatContainer.getMessage();
	}
	
	/**
	 * Add an observer to the chat
	 * @param o the observer
	 */
	public void addChatObserver(Observer o)
	{
		chatContainer.addObserver(o);
	}
}
/**
 * The local chat container class
 * @author Svetlin
 *	Allows us to attach observer to an unobservable object like a string
 */
class ChatContainer extends Observable
{
	private String lastMessage="";
	
	/**
	 * empty constructor
	 */
	public ChatContainer()
	{

	}
	/**
	 * set a new message as last, notify the observers
	 * @param s the new last received message
	 */
	public void setMessage(String s)
	{
		lastMessage=s;
		setChanged();
		notifyObservers();
	}
	/**
	 * Retrieve the last message
	 * @return the message
	 */
	public String getMessage()
	{
		return lastMessage;
	}
}