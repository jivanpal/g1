package JUnit;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetLocalHostTest {
	public static void main(String[] args) {
		try {
			System.out.println(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
