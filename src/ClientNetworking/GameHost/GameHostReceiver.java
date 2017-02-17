package ClientNetworking.GameHost;


import java.io.ObjectInputStream;

import GameLogic.Map;


public class GameHostReceiver extends Thread
{
	private ObjectInputStream clientIN;
	private Map gameMap=null;
		
	public GameHostReceiver(ObjectInputStream reader, Map gM)
	{
		clientIN = reader;
		gameMap = gM;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				Object inObject = clientIN.readObject();
				//add instanceof checks for type
			}

			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}
}
