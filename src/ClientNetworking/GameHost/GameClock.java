package ClientNetworking.GameHost;

import GameLogic.Map;
import GameLogic.Ship;
import Physics.Body;
import ServerNetworking.ClientTable;

import java.util.List;
import java.util.stream.Collectors;

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
