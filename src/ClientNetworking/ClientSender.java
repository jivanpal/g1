package ClientNetworking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSender extends Thread
{

	private ObjectOutputStream toServer;
	private LinkedBlockingQueue<Object> clientQueue;

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
			try
			{
				o = clientQueue.take();
				if (o != null)
					toServer.writeObject(o);
			}
			catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
