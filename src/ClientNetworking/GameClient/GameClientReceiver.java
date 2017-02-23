package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;
import GameLogic.Map;

public class GameClientReceiver extends Thread
{
	ObjectInputStream fromServer;
	private LinkedBlockingQueue<String> queue;
	private MapContainer gameMap = new MapContainer();
	private boolean running=true;

	GameClientReceiver(ObjectInputStream reader, LinkedBlockingQueue<String> q)
	{
		fromServer = reader;
		queue = q;
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
					gameMap.setMap((Map) inObject);
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