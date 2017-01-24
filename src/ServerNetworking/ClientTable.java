package ServerNetworking;
//Each nickname has a different incomming-message queue.

import java.util.concurrent.*;

/**
 * The Class ClientTable.
 */
public class ClientTable
{

	public ClientTable()
	{
	}

	/** The queue table. */
	private ConcurrentMap<String, LinkedBlockingQueue<Object>> queueTable = new ConcurrentHashMap<String, LinkedBlockingQueue<Object>>();

	/**
	 * Adds a new client.
	 * 
	 * @param nickname
	 *            the nickname of the client
	 */
	public void add(String nickname)
	{
		queueTable.put(nickname, new LinkedBlockingQueue<Object>());
	}

	/**
	 * Gets the queue.
	 * 
	 * @param nickname
	 *            the nickname of the player
	 * @return the queue
	 */
	// Returns null if the nickname is not in the table:
	public LinkedBlockingQueue<Object> getQueue(String nickname)
	{
		return queueTable.get(nickname);
	}

}
