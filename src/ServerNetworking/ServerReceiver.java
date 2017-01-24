package ServerNetworking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerReceiver extends Thread
{
	private ObjectInputStream clientIN;
	public ServerReceiver(ObjectInputStream reader)
	{
		clientIN = reader;
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
					//TODO typecast 
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
