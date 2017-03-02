package ClientNetworking.GameHost;

import java.io.Serializable;
import java.util.ArrayList;

public class KeySequence implements Serializable {
	private ArrayList<char[][]> keys = new ArrayList<char[][]>();;
	public KeySequence(int minLength,int maxLength,int number)
	{
		for(int i=minLength;i<=maxLength;i++)
		{
			keys.add(KeySequenceGen.Generate(number, i));
		}
	}
	public char[][] getSequencesByLength(int length) throws Exception
	{
		if(length-2>0)
			return keys.get(length-2);
		else
			throw(new Exception("Length of requested key sequence array is invalid"));
	}
}
