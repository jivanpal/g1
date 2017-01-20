package ServerNetworking;

import java.io.PrintStream;

public class ServerSender extends Thread
{
	private PrintStream clientOUT;

	public ServerSender(PrintStream sender)
	{
		clientOUT = sender;
	}

	public void run()
	{
		while (true)
		{

		}
	}
}
