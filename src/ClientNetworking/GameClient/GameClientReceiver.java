package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

import GameLogic.Map;

public class GameClientReceiver extends Thread
{
	ObjectInputStream fromServer;
	private LinkedBlockingQueue<String> queue;
	private MapContainer gameMap= new MapContainer();
	GameClientReceiver(ObjectInputStream reader, LinkedBlockingQueue<String> q)
	{
		fromServer = reader;
		queue = q;
	}

	public void run()
	{

		try
		{
			while (true)
			{
				try
				{
					Object inObject = fromServer.readObject();
					gameMap.setMap((Map)inObject);
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("Server seems to have died " + e.getMessage());
			System.exit(1); // Give up.
		}
	}
	public Map getMap()
	{return gameMap.getMap();}
}
class MapContainer extends Observable
{
        private Map map;
        public MapContainer()
        {}
        public void setMap(Map map)
        {
            this.map=map;
            setChanged();
            notifyObservers();
        }
        public Map getMap()
        {
            return map;
        }
 }