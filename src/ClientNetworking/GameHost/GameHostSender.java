package ClientNetworking.GameHost;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class GameHostSender extends Thread
{
	private ObjectOutputStream clientOUT;
	private LinkedBlockingQueue<Object> queue;

	public GameHostSender(ObjectOutputStream sender, LinkedBlockingQueue<Object> q)
	{
		clientOUT = sender;
		queue=q;
	}

	public void run()
	{
		// get the message queue for said client
		while (true)
		{
			Object objectOut = null;
			try
			{
				objectOut = queue.take();
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
