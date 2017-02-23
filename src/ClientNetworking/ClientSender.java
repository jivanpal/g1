package ClientNetworking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSender extends Thread
{

	private ObjectOutputStream toServer;
	public LinkedBlockingQueue<Object> clientQueue;

	public ClientSender(ObjectOutputStream server, LinkedBlockingQueue<Object> queue)
	{
		this.toServer = server;
		clientQueue = queue;
	}

	public void run()
	{
		while (true)
		{
			Object o;
			try {
				toServer.reset();
				o = clientQueue.take();
				toServer.writeObject(o);
				toServer.flush();
			}
			catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
