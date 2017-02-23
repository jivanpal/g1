package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class GameClientSender extends Thread
{
	private LinkedBlockingQueue<String> clientQueue;
	private ObjectOutputStream out;
	public GameClientSender(ObjectOutputStream server, LinkedBlockingQueue<String> queue)
	{
		this.clientQueue = queue;
		out = server;	
	}

	public void run()
	{
		while (true)
		{
			String str;
			try {
				str = clientQueue.take();
				out.reset();
				out.writeObject(str);
				out.flush();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}

		}
	}
}
