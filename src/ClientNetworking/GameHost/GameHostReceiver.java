package ClientNetworking.GameHost;

import java.io.ObjectInputStream;
import ServerNetworking.ClientTable;


public class GameHostReceiver extends Thread
{
	private ObjectInputStream clientIn;
	private ClientTable clientTable;
	private int position;
	public MapContainer gameMap;
	public String teamMate;
	private String playerName;
	private int shipID;
	public GameHostReceiver(ObjectInputStream reader,MapContainer map, ClientTable cT, int playerPos, String nickname,String teammate,int ship)
	{		
		gameMap = map;
		position = playerPos;
		clientTable = cT;
		clientIn = reader;
		teamMate=teammate;
		playerName = nickname;
		shipID = ship;
	}

	public void run()
	{
		boolean running = true;
		while (running)
		{
			try
			{
				Object obj = clientIn.readObject();
				if(obj instanceof String)
				{
					String str = (String)obj;
					gameMap.updateMap(str, playerName,shipID);
				}
				else
				{
					ChatMessage m = (ChatMessage) obj;
					clientTable.getQueue(playerName).offer(m);
					if(!teamMate.equals(""))
						clientTable.getQueue(teamMate).offer(m);
				}
			}

			catch (Exception e)
			{
				running=false;
				e.printStackTrace();
			}

		}
		
	}
}
