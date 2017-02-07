package ServerNetworking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.LinkedBlockingQueue;

import GeneralNetworking.Invite;
import GeneralNetworking.Lobby;
import GeneralNetworking.Player;

public class ServerReceiver extends Thread
{
	private ObjectInputStream clientIN;
	private ClientTable clientTable;

	public ServerReceiver(ObjectInputStream reader, ClientTable cT)
	{
		clientIN = reader;
		clientTable = cT;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				Object inObject = clientIN.readObject();
				// INVITE
				if (inObject instanceof Invite)
				{
					Invite inv = (Invite) inObject;
					clientTable.getQueue(inv.nickname).offer(inv);
				}
				// LOBBY
				if (inObject instanceof Lobby)
				{
					Lobby lobby = (Lobby) inObject;
					Player[] players = lobby.getPlayers();
					for (int i = 0; i < players.length; i++)
					{
						if (players[i] != null)
						{
							clientTable.getQueue(players[i].nickname).offer(lobby);
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}
}
