package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class GameClientSender extends Thread
{
	private LinkedBlockingQueue<Object> clientQueue;
	private ObjectOutputStream out;
	public GameClientSender(ObjectOutputStream server, LinkedBlockingQueue<Object> queue)
	{
		this.clientQueue = queue;
		out = server;	
	}

	public void run()
	{
		while (true)
		{
			Object o;
			try {
				o = clientQueue.take();
				out.reset();
				out.writeObject(o);
				out.flush();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}

		}
	}
}
