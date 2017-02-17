package ClientNetworking.GameHost;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import GameLogic.Map;


public class GameHostReceiver extends Thread
{
	private ObjectInputStream clientIN;
	private Map gameMap=null;
	private BufferedReader in;	
	public GameHostReceiver(ObjectInputStream reader, Map gM)
	{		
		in = new BufferedReader(new InputStreamReader(reader));
		gameMap = gM;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				String str = in.readLine();
					
			}

			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}
}
