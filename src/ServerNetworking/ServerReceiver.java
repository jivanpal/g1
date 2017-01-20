package ServerNetworking;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerReceiver extends Thread
{
	private BufferedReader clientIN;

	public ServerReceiver(BufferedReader reader)
	{
		// set reader
		clientIN = reader;
	}

	public void run()
	{
		while (true)
		{
			String message = "";
			// read from client
			try
			{
				message = clientIN.readLine();

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	}
}
