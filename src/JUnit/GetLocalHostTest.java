package JUnit;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
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
					//System.out.println(ip);
				}
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			System.out.println(Inet6Address.);
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
