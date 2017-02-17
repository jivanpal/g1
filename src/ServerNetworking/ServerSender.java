package ServerNetworking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

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
			try {
				clientOUT.reset();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
					clientOUT.writeObject(objectOut);
					clientOUT.flush();
				}
			}
			catch (IOException e)
			{
				runs = false;
				e.printStackTrace();
			}
		}
	}
}
