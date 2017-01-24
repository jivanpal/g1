package ServerNetworking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerReceiver extends Thread
{
	private ObjectInputStream clientIN;
	private LinkedBlockingQueue<Object> queue;

	public ServerReceiver(ObjectInputStream reader, LinkedBlockingQueue<Object> q)
	{
		clientIN = reader;
		queue = q;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				try
				{
					Object inObject = clientIN.readObject();
					try
					{
						// typecast here or at client side?
						//if(inObject instanceof String)
						queue.put(inObject);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				catch (ClassNotFoundException e)
				{
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
