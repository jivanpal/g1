package ClientNetworking.GameHost;

import java.io.ObjectInputStream;
import ServerNetworking.ClientTable;


public class GameHostReceiver extends Thread
{
	private ObjectInputStream clientIn;
	private ClientTable clientTable;
	private int position;
	public MapContainer gameMap;
	public int mapEntryNumber;
	public GameHostReceiver(ObjectInputStream reader,MapContainer map, ClientTable cT, int playerPos, String nickname,int mapEntry)
	{		
		gameMap = map;
		position = playerPos;
		clientTable = cT;
		clientIn = reader;
		mapEntryNumber=mapEntry;
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
					gameMap.updateMap(str, mapEntryNumber);
				}
				else
				{
					ChatMessage m = (ChatMessage) obj;
					int teammatePos = position + position%2==0? 1 : -1; 
					clientTable.getQueue(""+position).offer(m);
					if(clientTable.getQueue(""+teammatePos)!=null)
						clientTable.getQueue(""+teammatePos).offer(m);
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
