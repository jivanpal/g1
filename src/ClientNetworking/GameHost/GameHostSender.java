package ClientNetworking.GameHost;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import ServerNetworking.ClientTable;

public class GameHostSender extends Thread
{
	private ObjectOutputStream clientOUT;
	private LinkedBlockingQueue<Object> queue;
	private ClientTable clientTable;
	private String pos;

	public GameHostSender(ObjectOutputStream sender, ClientTable cT,String name)
	{
		clientOUT = sender;
		clientTable = cT;
		pos = name;
	}

	public void run()
	{
		// get the message queue for said client
		while (true)
		{
			Object objectOut = null;
			try
			{
				objectOut = clientTable.getQueue(pos).take();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			try
			{
				//check if we got anything to send
				if (objectOut != null)
				{
					clientOUT.reset();
					clientOUT.writeObject(objectOut);
					clientOUT.flush();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
