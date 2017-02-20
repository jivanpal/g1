package ClientNetworking.GameClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;

public class GameClientSender extends Thread
{
	private String nickname;
	private LinkedBlockingQueue<String> clientQueue;
	PrintWriter out;
	public GameClientSender(OutputStream server, LinkedBlockingQueue<String> queue)
	{
		this.clientQueue = queue;
		out = new PrintWriter(server);	
	}

	public void run()
	{
		while (true)
		{
			String str;
			try {
				str = clientQueue.take() + "\n";
				out.append(str);
				out.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
