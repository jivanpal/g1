package GameLogic;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 * Class which hold all of the key bindings as well as holds thier values on
 * close
 * 
 * @author Ivan Panchev
 *
 */
public class GameOptions {

	// default values for each action
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
	
	public static final String SOUND_VOLUME = "SOUND_VOLUME";
	public static final String MUSIC_VOLUME = "MUSIC_VOLUME";
	
	public static final String DEFAULT_SOUND_VOLUME = "0";
	public static final String DEFAULT_MUSIC_VOLUME = "0";

	// the default filename that the values are saved at
	public final static String KEY_BINDINGS_FILE = System.getProperty("user.dir") + "/keybindings.txt";
	public final static String SOUND_VALUES_FILE = System.getProperty("user.dir") + "/soundvalues.txt";

	// hashtable which holds the current values
	private static Properties keyBindings = new Properties();
	private static Properties soundValues = new Properties();

	// reader and writer to change and write to the file
	private static Writer fileWriter;
	private static Reader fileReader;

	private static void initialiseReader(String fileName) throws FileNotFoundException {
		GameOptions.fileReader = new FileReader(new File(fileName));
	}

	private static void inisialiseWriter(String fileName) throws IOException {
		GameOptions.fileWriter = new PrintWriter(new File(fileName));
	}

	/** called when the game is started.
	 if there is a file with key bindings - it sets the current values to
	 those in the file
	 if there isn't - sets the default values as current and saves them in a
	 file
	 ideally the defaults are going to be used just once - the first time the
	 game is started */
	public static void setKeyBindings() {

		try {
			initialiseReader(KEY_BINDINGS_FILE);
			keyBindings.load(fileReader);
			GameOptions.fileReader.close();
		} catch (Exception e) {
			resetKeysToDefaults();
			saveKeyBindingsInFile();
		}
	}
	
	public static void setSoundValues() {
		
		try {
			initialiseReader(SOUND_VALUES_FILE);
			soundValues.load(fileReader);
			GameOptions.fileReader.close();
		} catch (Exception e) {
			resetSoundValuesToDefaults();
			saveKeyBindingsInFile();
		}
	}
	
	public static void resetSoundValuesToDefaults() {
		soundValues.setProperty(DEFAULT_SOUND_VOLUME, SOUND_VOLUME);
		soundValues.setProperty(DEFAULT_MUSIC_VOLUME, MUSIC_VOLUME);
	}

	public static void resetKeysToDefaults() {
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

	public static void saveKeyBindingsInFile() {
		try {
			inisialiseWriter(KEY_BINDINGS_FILE);
			GameOptions.keyBindings.store(fileWriter, "");
			GameOptions.fileWriter.close();
		} catch (IOException e) {
			// something went wrong
			e.printStackTrace();
		}

	}
	
	public static void saveSoundValuesInFile() {
		try {
			inisialiseWriter(SOUND_VALUES_FILE);
			GameOptions.soundValues.store(fileWriter, "");
			GameOptions.fileWriter.close();
		} catch (IOException e) {
			// something went wrong
			e.printStackTrace();
		}
		
	}

	public static void changeKeyByDefaultValue(String defaultValue, int newValue) {
		GameOptions.keyBindings.setProperty(defaultValue, String.valueOf(newValue));
	}
	
	public static void changeSoundByDefaultValue(String defaultValue, String newValue) {
		GameOptions.soundValues.setProperty(defaultValue, newValue);
	}

	public static int getCurrentKeyValueByDefault(String defaultValue) {
		return Integer.valueOf(keyBindings.getProperty(defaultValue));
	}
	
	public static int getCurrentSoundValueByDefault(String defaultValue) {
		return Integer.valueOf(soundValues.getProperty(defaultValue));
	}

	public static boolean checkIfKeyTaken(int keyCode) {
		return keyBindings.containsValue(String.valueOf(keyCode));

	}
	
	public Properties getKeyBindings(){
		return this.keyBindings;
	}

}
