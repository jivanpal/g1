package ClientNetworking.GameHost;

import ServerNetworking.ClientTable;

public class GameClock extends Thread {

	public ClientTable clientTable;
	public MapContainer mapContainer;
	public GameClock(ClientTable ct, MapContainer mc) {
		clientTable= ct;
		mapContainer=mc;
	}

	public void run()
	{
		boolean running=true;
		while(running)
		{
			mapContainer.updateMap();
			clientTable.queueToAll(mapContainer.gameMap);
			try {
				Thread.sleep(GameLogic.Global.REFRESH_RATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
