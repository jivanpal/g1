package ClientNetworking.GameHost;

import java.io.ObjectInputStream;
import java.util.ArrayList;

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
	private KeySequence keySequence;
	public GameHostReceiver(ObjectInputStream reader,MapContainer map, ClientTable cT, int playerPos, String nickname,String teammate,int ship,KeySequence k)
	{		
		gameMap = map;
		position = playerPos;
		clientTable = cT;
		clientIn = reader;
		teamMate=teammate;
		playerName = nickname;
		shipID = ship;
		keySequence=k;
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
					if(teamMate.equals("") && position%2==1)
					{
						if(m.message.contains("instruction"))
						{
							String id = "";
							String[] split = m.message.split(" ");
							for(int i=0;i<split.length;i++)
							{
								if(split[i].equals("instruction"))
								{
									id = split[i+1];
								}
							}
							ArrayList<String> seq=keySequence.getAllKeys();  
							for(int i=0;i<seq.size();i++)
							{
								if(seq.get(i).contains(id))
								{
									clientTable.getQueue(playerName).offer(new ChatMessage("Pilot",seq.get(i).split(":")[1]));
								}
							}
						}
						
					}
					else if(!teamMate.equals(""))
					{
						clientTable.getQueue(teamMate).offer(m);
					}

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
