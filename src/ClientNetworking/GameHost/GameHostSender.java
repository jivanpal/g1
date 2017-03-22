package ClientNetworking.GameHost;

import java.io.IOException;
import java.io.ObjectOutputStream;
import ServerNetworking.ClientTable;
/**
 * A Game Server sender (1 per client)
 * @author Svetlin
 *
 */
public class GameHostSender extends Thread
{
	private ObjectOutputStream clientOUT;
	ClientTable clientTable;
	private String nickname;
	/**
	 * Constructor
	 * @param sender the output stream
	 * @param ct the client table
	 * @param name the name of the player, for which this thread is created
	 */
	public GameHostSender(ObjectOutputStream sender,ClientTable ct, String name)
	{
		clientOUT = sender;
		clientTable = ct;
		nickname = name;
	}

	public void run()
	{
		boolean running= true;
		// get the message queue for said client
		while (running)
		{
			try
			{
					//retrieve an object to send from the queue
					Object o = clientTable.getQueue(nickname).take();
					//reset the output stream and send
					clientOUT.reset();
					clientOUT.writeObject(o);
					clientOUT.flush();
			}
			catch (IOException | InterruptedException e)
			{
				running = false;
				e.printStackTrace();
			}
		}
	}
}
