package ClientNetworking;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 
 * @author Svetlin
 *	Retrieve the ip of the machine 
 */
public class IpGetter
{
	public static InetAddress getRealIp()
	{
		try
		{
			Enumeration<NetworkInterface> s = NetworkInterface.getNetworkInterfaces();
			while (s.hasMoreElements())
			{
				Enumeration<InetAddress> x = s.nextElement().getInetAddresses();
				while(x.hasMoreElements())
				{
					InetAddress ip = x.nextElement();
					String str = ip.toString();
					if(str.contains("/192.168."))
						return ip;
				}
			}
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
