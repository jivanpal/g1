package ClientNetworking.GameHost;

import java.util.ArrayList;
import java.util.Random;

public class KeySequenceGen {
	
	public static ArrayList<char[]> Generate(int length)
	{
		ArrayList<char[]> list = new ArrayList<char[]>();
		Random r = new Random();
		for(int i=0;i<4;i++)
		{
			char[] arr = new char[length]; 
			for(int j=0;j<length;j++)
			{
				arr[j] = (char)(97+r.nextInt(26));
			}
			list.add(arr);
		}
		return list;
	}
}
