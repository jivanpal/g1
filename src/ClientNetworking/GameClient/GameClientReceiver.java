package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

import ClientNetworking.GameHost.ChatMessage;
import GameLogic.Map;

public class GameClientReceiver extends Thread
{
	ObjectInputStream fromServer;
	private MapContainer gameMap = new MapContainer();
	private ChatContainer chatContainer = new ChatContainer();
	private boolean running=true;
	

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
					Object inObject = fromServer.readObject();
					
					if(inObject instanceof Map)
						gameMap.setMap((Map) inObject);
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
			System.out.println("GAME CLIENT RECEIVER: Server seems to have died " + e.getMessage());
			running = false;
			System.exit(1); // Give up.
		}
	}

	public Map getMap()
	{
		return gameMap.getMap();
	}

	public void setObserver(Observer o)
	{
		gameMap.addObserver(o);
	}
	public String getMessage()
	{
		return chatContainer.getMessage();
	}
	public void addChatObserver(Observer o)
	{
		chatContainer.addObserver(o);
	}
}

class MapContainer extends Observable
{
	private Map map = new Map(10000,10000,10000);

	public MapContainer()
	{
	}

	public void setMap(Map map)
	{
		this.map = map;
		setChanged();
		notifyObservers();
	}

	public Map getMap()
	{
		return map;
	}
}
class ChatContainer extends Observable
{
	private String lastMessage="";
	
	public ChatContainer()
	{

	}
	
	public void setMessage(String s)
	{
		lastMessage=s;
		setChanged();
		notifyObservers();
	}
	public String getMessage()
	{
		return lastMessage;
	}
}