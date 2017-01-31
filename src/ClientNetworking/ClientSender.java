package ClientNetworking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

public class ClientSender extends Thread
{

	private String nickname;
	private ObjectOutputStream toServer;

	ClientSender(ObjectOutputStream server)
	{
		this.toServer = server;
	}

	public void run()
	{
		while (true)
		{
			// Write to Server (from a queue)
		}
	}
}
