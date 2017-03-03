package ClientNetworking.GameHost;

import java.util.Random;

public class KeySequenceGen {
	public static char[][] Generate(int number, int length) {
		Random r = new Random();
		char[][] arr = new char[number][length];
		for (int i = 0; i < number; i++) {
			for (int j = 0; j < length; j++) {
				arr[i][j] = (char) (97 + r.nextInt(26));
			}
		}
		return arr;
	}
}
