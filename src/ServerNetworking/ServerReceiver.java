package ServerNetworking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.LinkedBlockingQueue;

import GeneralNetworking.Invite;
import GeneralNetworking.Lobby;

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
				Object inObject = clientIN.readObject();
				// LOBBY
				if (inObject instanceof Lobby)
				{
					
				}
				// INVITE
				if (inObject instanceof Invite)
				{

				}
			}

			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}
}
