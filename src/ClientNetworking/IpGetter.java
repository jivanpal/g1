package ClientNetworking;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
	
/**
 * 
 * @author Svetlin
 *	Retrieve the ip of the machine (untested for ipv6)
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
					if(ip instanceof Inet4Address)
					{
						if(str.contains("/192.168."))
						{
							return ip;
						}
						/*if(!str.contains(":") && !str.contains("/192.168.") && !str.contains("/127."))
						{
							return ip;
						}*/
					}
					else if(ip instanceof Inet6Address)
					{
						return ip;
					}	
						
				}
			}
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			//System.out.println(java.net.Inet6Address.getAllByName("localhost")());
			return Inet6Address.getLocalHost();
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
