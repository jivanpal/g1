package JUnit;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class GetLocalHostTest
{
	public static void main(String[] args)
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
					if(!str.contains(":") && !str.contains("/192.168.") && !str.equals("/127.0.0.1"))
						System.out.println(ip);
				}
			}
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
