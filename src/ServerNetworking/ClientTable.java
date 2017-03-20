package ServerNetworking;
//Each nickname has a different incomming-message queue.

import GameLogic.Map;
import GameLogic.Ship;
import Physics.Body;

import java.util.Set;
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
	/**
	 * queue an object to each queue
	 * @param o the object
	 */
	public void queueToAll(Object o)
	{
		Set<String> names = queueTable.keySet();
		for(String name: names)
		{
			queueTable.get(name).offer(o);
		}
	}
	public void remove(String name)
	{
		queueTable.remove(name);
	}
}
