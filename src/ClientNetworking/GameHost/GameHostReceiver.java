package ClientNetworking.GameHost;


import java.io.ObjectInputStream;


public class GameHostReceiver extends Thread
{
	private ObjectInputStream clientIN;

	public GameHostReceiver(ObjectInputStream reader)
	{
		clientIN = reader;
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
