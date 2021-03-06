package ServerNetworking;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerSender extends Thread
{
	private ObjectOutputStream clientOUT;
	private ClientTable clientTable;
	private String nickname;

	public ServerSender(ObjectOutputStream sender, ClientTable ct,String nick)
	{
		clientOUT = sender;
		clientTable = ct;
		nickname=nick;
	}

	public void run()
	{
		boolean runs = true;
		// get the message queue for said client
		while (runs)
		{
			Object objectOut = null;
			try
			{	
				objectOut = clientTable.getQueue(nickname).take();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			try
			{
				if (objectOut != null)
				{
					clientOUT.reset();
					clientOUT.writeObject(objectOut);
					clientOUT.flush();
				}
			}
			catch (IOException e)
			{
				runs = false;
			}
		}
	}
}
