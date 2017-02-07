package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;

public class GameClientReceiver extends Thread
{
	ObjectInputStream fromServer;

	GameClientReceiver(ObjectInputStream reader)
	{
		fromServer = reader;
	}

	public void run()
	{

		try
		{
			while (true)
			{
				try
				{
					Object inObject = fromServer.readObject();
					//TYPECAST
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("Server seems to have died " + e.getMessage());
			System.exit(1); // Give up.
		}
	}
}
