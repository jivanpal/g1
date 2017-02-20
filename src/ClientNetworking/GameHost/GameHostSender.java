package ClientNetworking.GameHost;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import GameLogic.Global;
import GameLogic.Map;
import ServerNetworking.ClientTable;

public class GameHostSender extends Thread
{
	private ObjectOutputStream clientOUT;
	private Map gameMap;
	private String pos;

	public GameHostSender(ObjectOutputStream sender, Map gM,String name)
	{
		clientOUT = sender;
		gameMap = gM;
		pos = name;
	}

	public void run()
	{
		// get the message queue for said client
		while (true)
		{
			try
			{
				//check if we got anything to send
					clientOUT.reset();
					clientOUT.writeObject(gameMap);
					clientOUT.flush();
					try {
						Thread.sleep((long)(Global.REFRESH_PERIOD*1000));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
