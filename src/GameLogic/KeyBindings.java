package GameLogic;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;


/**
 * Class which hold all of the key bindings
 * 
 * @author Ivan Panchev
 *
 */
public class KeyBindings {

	public static final String DEFAULT_FIRE_WEAPON_1_BUTTON = String.valueOf(KeyEvent.VK_1);
	public static final String DEFAULT_FIRE_WEAPON_2_BUTTON = String.valueOf(KeyEvent.VK_2);
	public static final String DEFAULT_FIRE_WEAPON_3_BUTTON = String.valueOf(KeyEvent.VK_3);
	public static final String DEFAULT_ACCELERATE_BUTTON = String.valueOf(KeyEvent.VK_E);
	public static final String DEFAULT_DECELERATE_BUTTON = String.valueOf(KeyEvent.VK_Q);
	public static final String DEFAULT_PITCH_DOWN_BUTTON = String.valueOf(KeyEvent.VK_W);
	public static final String DEFAULT_PITCH_UP_BUTTON = String.valueOf(KeyEvent.VK_S);
	public static final String DEFAULT_ROLL_LEFT_BUTTON = String.valueOf(KeyEvent.VK_A);
	public static final String DEFAULT_ROLL_RIGHT_BUTTON = String.valueOf(KeyEvent.VK_D);
	public static final String DEFAULT_OVERDRIVE_BUTTON = String.valueOf(KeyEvent.VK_SPACE);
	public static final String DEFAULT_MANUAL_BUTTON = String.valueOf(KeyEvent.VK_X);
	public static final String DEFAULT_MANUAL_NEXT_BUTTON = String.valueOf(KeyEvent.VK_Z);
	public static final String DEFAULT_MANUAL_PREV_BUTTON = String.valueOf(KeyEvent.VK_C);

	private final static String FILE_NAME = System.getProperty("user.dir") + "\\keybindings.txt";
	
	private static Properties keyBindings = new Properties();
	private static Writer fileWriter;
	private static Reader fileReader;
	
	private static void initialiseReader() throws FileNotFoundException{
		KeyBindings.fileReader = new FileReader(new File(FILE_NAME));
	}
	
	private static void inisialiseWriter() throws IOException{
		KeyBindings.fileWriter = new PrintWriter(new File(FILE_NAME));
	}
	
	public static void setKeyBindings(){
		
		try {
			initialiseReader();
			keyBindings.load(fileReader);
			KeyBindings.fileReader.close();
		} catch (Exception e) {
			resetKeysToDefaults();
			saveKeyBindingsInFile();
		} 
	}
	
	public static void resetKeysToDefaults(){
		keyBindings.setProperty(DEFAULT_FIRE_WEAPON_1_BUTTON, DEFAULT_FIRE_WEAPON_1_BUTTON);
		keyBindings.setProperty(DEFAULT_FIRE_WEAPON_2_BUTTON, DEFAULT_FIRE_WEAPON_2_BUTTON);
		keyBindings.setProperty(DEFAULT_FIRE_WEAPON_3_BUTTON, DEFAULT_FIRE_WEAPON_3_BUTTON);
		keyBindings.setProperty(DEFAULT_ACCELERATE_BUTTON, DEFAULT_ACCELERATE_BUTTON);
		keyBindings.setProperty(DEFAULT_DECELERATE_BUTTON, DEFAULT_DECELERATE_BUTTON);
		keyBindings.setProperty(DEFAULT_PITCH_DOWN_BUTTON, DEFAULT_PITCH_DOWN_BUTTON);
		keyBindings.setProperty(DEFAULT_PITCH_UP_BUTTON, DEFAULT_PITCH_UP_BUTTON);
		keyBindings.setProperty(DEFAULT_ROLL_LEFT_BUTTON, DEFAULT_ROLL_LEFT_BUTTON);
		keyBindings.setProperty(DEFAULT_ROLL_RIGHT_BUTTON, DEFAULT_ROLL_RIGHT_BUTTON);
		keyBindings.setProperty(DEFAULT_OVERDRIVE_BUTTON, DEFAULT_OVERDRIVE_BUTTON);
		keyBindings.setProperty(DEFAULT_MANUAL_BUTTON, DEFAULT_MANUAL_BUTTON);
		keyBindings.setProperty(DEFAULT_MANUAL_NEXT_BUTTON, DEFAULT_MANUAL_NEXT_BUTTON);
		keyBindings.setProperty(DEFAULT_MANUAL_PREV_BUTTON, DEFAULT_MANUAL_PREV_BUTTON);
	}
	
	private static void saveKeyBindingsInFile(){
		try {
			inisialiseWriter();
			KeyBindings.keyBindings.store(fileWriter, "");
			KeyBindings.fileWriter.close();
		} catch (IOException e) {
			//something went wrong
			e.printStackTrace();
		}
		
	}
	
	public static void changeKeyByCurrentValue(String currentValue, int newValue){
		Set keys = KeyBindings.keyBindings.keySet();
		Iterator itr = keys.iterator();
		String key;
		while(itr.hasNext()){
			 key = (String) itr.next();
			 if(keyBindings.getProperty(key).equals(currentValue)){
				 keyBindings.setProperty(key, String.valueOf(newValue));
			 }
		}
		saveKeyBindingsInFile();
	}
	
	public static void changeKeyByDefaultValue(String defaultValue, int newValue){
		KeyBindings.keyBindings.setProperty(defaultValue, String.valueOf(newValue));
	}
	
	public static int getCurrentValueByDefault(String defaultValue){
		Set keys = KeyBindings.keyBindings.keySet();
		Iterator itr = keys.iterator();
		String key;
		int currentKeyCode = -1;
		while(itr.hasNext()){
			 key = (String) itr.next();
			 if(key.equals(defaultValue)){
				 currentKeyCode = Integer.valueOf(keyBindings.getProperty(key));
				 break;
			 }
		}
		return currentKeyCode;
	}
	
	public void printKeys(){
		Set keys = KeyBindings.keyBindings.keySet();
		Iterator itr = keys.iterator();
		String key;
		while(itr.hasNext()){
			 key = (String) itr.next();
			 System.out.println(key + "  " + KeyBindings.keyBindings.getProperty(key));
		}
	}
	
	public static boolean checkIfKeyTaken(int keyCode){
		return KeyBindings.keyBindings.contains(String.valueOf(keyCode));
	}

}
