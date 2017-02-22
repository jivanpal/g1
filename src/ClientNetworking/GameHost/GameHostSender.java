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
	ClientTable clientTable;
	private String nickname;
	
	public GameHostSender(ObjectOutputStream sender,ClientTable ct, String name)
	{
		clientOUT = sender;
		clientTable = ct;
		nickname = name;
	}

	public void run()
	{
		boolean running= true;
		// get the message queue for said client
		while (running)
		{
			try
			{
				//check if we got anything to send
					clientOUT.reset();
					clientOUT.writeObject(clientTable.getQueue(nickname).take());
					clientOUT.flush();
			}
			catch (IOException | InterruptedException e)
			{
				running = false;
				e.printStackTrace();
			}
		}
	}
}
