package ClientNetworking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * 
 * @author Svetlin
 * The Client sender class
 */
public class ClientSender extends Thread
{

	//the output stream
	private ObjectOutputStream toServer;
	
	//the queue, through which objects are put to be sent
	public LinkedBlockingQueue<Object> clientQueue;

	/**
	 * Constructor
	 * @param server output stream to the server
	 * @param queue the object queue
	 */
	public ClientSender(ObjectOutputStream server, LinkedBlockingQueue<Object> queue)
	{
		this.toServer = server;
		clientQueue = queue;
	}

	/**
	 * The run method, inherited from Thread
	 */
	public void run()
	{
	    boolean running = true;
		while (running)
		{
			Object o;
			try {
				//reset the output
				toServer.reset();
				//take an item from the queue
				o = clientQueue.take();
				//write to server and flush
				toServer.writeObject(o);
				toServer.flush();
			}
			catch (InterruptedException | IOException e1)
			{
			    running = false;
				e1.printStackTrace();
			}
		}
	}
}
