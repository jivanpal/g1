package GeneralNetworking;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * @author Svetlin
 * The player class
 */
@SuppressWarnings("serial")
public class Player implements Serializable
{
	public String nickname = "";
	public InetAddress address = null;
	public boolean isHost=false;

	/**
	 * Constructor
	 * @param nick the player name
	 * @param add the player i-net address
	 * @param iH is the player the host of the Lobby 
	 */
	public Player(String nick, InetAddress add,boolean iH)
	{
		address = add;
		nickname = nick;
		isHost=iH;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + (isHost ? 1231 : 1237);
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (isHost != other.isHost)
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		return true;
	}
}
