package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class GameClientSender extends Thread
{
	private String nickname;
	private ObjectOutputStream toServer;
	private LinkedBlockingQueue<Object> clientQueue;

	public GameClientSender(ObjectOutputStream server, LinkedBlockingQueue<Object> queue)
	{
		this.toServer = server;
		this.clientQueue = queue;
	}

	public void run()
	{
		while (true)
		{
			Object obj;
			try {
				obj = clientQueue.take();
				if(!obj.equals(null))
				{
					toServer.writeObject(obj);
					toServer.flush();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
