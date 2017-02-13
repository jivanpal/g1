package GeneralNetworking;

import java.io.Serializable;
import java.net.InetAddress;

@SuppressWarnings("serial")
public class Player implements Serializable
{
	/**
	 * 
	 */
	public String nickname = "";
	public InetAddress address = null;
	public boolean isHost=false;

	public Player(String nick, InetAddress add,boolean iH)
	{
		address = add;
		nickname = nick;
		isHost=iH;
	}
}
