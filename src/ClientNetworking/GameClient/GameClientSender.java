package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * The in-game sender class
 * @author Svetlin
 *
 */
public class GameClientSender extends Thread
{
	private LinkedBlockingQueue<Object> clientQueue;
	private ObjectOutputStream out;
	/**
	 * Constructor
	 * @param server the output stream to the server
	 * @param queue the queue from which we can pull objects to send
	 */
	public GameClientSender(ObjectOutputStream server, LinkedBlockingQueue<Object> queue)
	{
		this.clientQueue = queue;
		out = server;	
	}

	public void run()
	{
		boolean running = true;
		while (running)
		{
			Object o;
			try {
				//take an object from the queue
				o = clientQueue.take();
				//reset the stream, write and flush
				out.reset();
				out.writeObject(o);
				out.flush();
			} catch (InterruptedException | IOException e) {
				running=false;
				e.printStackTrace();
			}

		}
	}
}
