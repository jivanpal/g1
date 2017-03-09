
package ClientNetworking.GameHost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;


public class KeySequence implements Serializable {
	private ArrayList<String> keys = new ArrayList<String>();
	private Random r = new Random();

	public KeySequence(int minLength, int maxLength, int blocks) {
		ArrayList<Integer> a = generateRandomNumber(blocks*(maxLength-minLength + 1));
		for (int j = 0; j < blocks; j++) {
			for (int i = minLength; i <= maxLength; i++) {
				keys.add(generate(i, String.valueOf(a.get(0))));
				a.remove(0);
			}
		}
	}

	public ArrayList<String> getAllKeys() {
		return this.keys;
	}

	public int getKeysSize() {
		return this.keys.size();
	}

	public String generate(int length, String number) {
		String s = number + ":";
		for (int j = 0; j < length; j++) {
			s += (char) (97 + r.nextInt(26));
		}
		return s;
	}
	
	private ArrayList<Integer> generateRandomNumber(int num){
		System.out.println("number of inst. " + num);
		ArrayList<Integer> a = new ArrayList<>();
		for(int i= 1; i<= num;i++){
			a.add(i);
		}
		Collections.shuffle(a);
		return a;
	}
}
