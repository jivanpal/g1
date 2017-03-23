
package ClientNetworking.GameHost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

/**
 * The key sequence class and generator
 * @author Svetlin,Ivan,James
 * 
 */
public class KeySequence implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<String> keys = new ArrayList<String>();
	private Random r = new Random();

	/**
	 * Constructor
	 * @param minLength the minimum sequence length
	 * @param maxLength the maximum sequence length
	 * @param blocks the number of blocks per sequence
	 */
	public KeySequence(int minLength, int maxLength, int blocks) {
		//generate the numbers and shuffle them
		ArrayList<Integer> a = generateRandomNumber(blocks*(maxLength-minLength + 1));
		for (int j = 0; j < blocks; j++) {
			for (int i = minLength; i <= maxLength; i++) {
				//generate the key sequences
				keys.add(generate(i, String.valueOf(a.get(0))));
				a.remove(0);
			}
		}
	}
	/**
	 * Get the keySequences
	 * @return the keySequences
	 */
	public ArrayList<String> getAllKeys() {
		return this.keys;
	}
	/**
	 * get the number of key sequences
	 * @return the number of key sequences
	 */
	public int getKeysSize() {
		return this.keys.size();
	}
	/**
	 * Generate a key sequence and concatenate it to its number
	 * @param length the length of the sequence
	 * @param number the number of the sequence
	 * @return the sequence
	 */
	public String generate(int length, String number) {
		String s = number + ":";
		for (int j = 0; j < length; j++) {
			s += (char) (97 + r.nextInt(26));
		}
		return s;
	}
	/**
	 * generate a shuffled set of numbers between 1 and num
	 * @param num the number
	 * @return the shuffled set
	 */
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
