package ClientNetworking.GameHost;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Random;

import GameLogic.*;
import Geometry.Vector;
import Physics.Body;
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
				String str = (String)clientIn.readObject();
				gameMap.updateMap(str, mapEntryNumber);
				clientTable.queueToAll(gameMap.gameMap);
			}

			catch (Exception e)
			{
				running=false;
				e.printStackTrace();
			}

		}
		
	}
}
