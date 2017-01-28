package GeneralNetworking;

import java.net.InetAddress;

public class Player
{
	public String nickname = "";
	public InetAddress address = null;
	public boolean isHost=false;

	public Player(String nick, InetAddress add,boolean iH)
	{
		address = add;
		nickname = nick;
		isHost=iH;
	}
	public void setName(String name)
	{
		nickname = name;
	}

	public void setAddress(InetAddress add)
	{
		address = add;
	}
}
