package ClientNetworking.GameHost;

import ServerNetworking.ClientTable;
/**
 * A server sided game clock, sending map state to all players every 1/GameLogic.Global.REFRESH_RATE seconds
 * @author Svetlin
 */
public class GameClock extends Thread {

	public ClientTable clientTable;
	public MapContainer mapContainer;
	/**
	 * Constructor
	 * @param ct the client table
	 * @param mc the Map Container
	 */
	public GameClock(ClientTable ct, MapContainer mc) {
		clientTable= ct;
		mapContainer=mc;
	}

	public void run()
	{
		boolean running=true;
		while(running)
		{
			//update the map each tick
			mapContainer.updateMap();
			//send it to each player
			clientTable.queueToAll(mapContainer.gameMap);

			try {
				Thread.sleep(GameLogic.Global.REFRESH_RATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
