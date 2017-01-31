package ClientNetworking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import GeneralNetworking.Invite;
import GeneralNetworking.Lobby;

public class ClientReceiver extends Thread
{

	ObjectInputStream fromServer;

	ClientReceiver(ObjectInputStream reader)
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
					// LOBBY
					if (inObject instanceof Lobby)
					{

					}
					// INVITE
					if (inObject instanceof Invite)
					{

					}
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
