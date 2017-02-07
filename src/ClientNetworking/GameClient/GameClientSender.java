package ClientNetworking.GameClient;

import java.io.ObjectOutputStream;

public class GameClientSender extends Thread
{
	private String nickname;
	private ObjectOutputStream toServer;

	public GameClientSender(ObjectOutputStream server)
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
