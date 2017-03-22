package GameLogic;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import Audio.AudioPlayer;

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
	public static final String DEFAULT_MANUAL_NEXT_BUTTON = String.valueOf(KeyEvent.VK_C);
	public static final String DEFAULT_MANUAL_PREV_BUTTON = String.valueOf(KeyEvent.VK_Z);
	public static final String DEFAULT_CLOSE_MANUAL_BUTTON = String.valueOf(KeyEvent.VK_ESCAPE);

	public static final String SOUND_VOLUME_MASTER = "SOUND_VOLUME_MASTER";
	public static final String MUSIC_VOLUME_MASTER = "MUSIC_VOLUME_MASTER";
	
	public static final String SOUND_VOLUME_VOLUME = "SOUND_VOLUME_VOLUME";
	public static final String MUSIC_VOLUME_VOLUME = "MUSIC_VOLUME_VOLUME";
	
	public static final String DEFAULT_SOUND_VOLUME = "65536";
	public static final String DEFAULT_MUSIC_VOLUME = "65536";
	
	public static final String DEFAULT_SOUND_MASTER = "0";
	public static final String DEFAULT_MUSIC_MASTER = "0";

	// the default filename that the values are saved at
	public final static String KEY_BINDINGS_FILE = System.getProperty("user.dir") + "/keybindings.txt";
	public final static String SOUND_VALUES_FILE = System.getProperty("user.dir") + "/soundvalues.txt";
	public final static String XIROD_FONT_FILE = System.getProperty("user.dir") + "/res/fonts/xirod/xirod.ttf";
	
	//fonts used throughout the UI
    public static Font FULLSCREEN_BOLD_REAL_BIG_TEXT_FONT;
	public static Font FULLSCREEN_BOLD_TEXT_FONT;
    public static Font REGULAR_TEXT_FONT;
	public static Font LARGE_BOLD_TEXT_FONT;
	public static Font BUTTON_FONT;

	//hashtables which holds the current values
	private static Properties keyBindings = new Properties();
	private static Properties soundValues = new Properties();

	// reader and writer to change and write to the file
	private static Writer fileWriter;
	private static Reader fileReader;
	
	/**
	 * Initialises the filer reader with given file name
	 * @param fileName The wanted file
	 * @throws FileNotFoundException Thrown if the file is not present in the default project directory
	 */
	private static void initialiseReader(String fileName) throws FileNotFoundException {
		GameOptions.fileReader = new FileReader(new File(fileName));
	}
	/**
	 * Initialises the filer writer with given file name
	 * @param fileName The wanted file
	 * @throws IOException Thrown if an error occures while writing to the file
	 */
	private static void inisialiseWriter(String fileName) throws IOException {
		GameOptions.fileWriter = new PrintWriter(new File(fileName));
	}

	/** 
	 * Called when the game is started.
	 * If there is a file with key bindings - it sets the current values to
	 * those from the file.
	 * If there isn't - sets the default values as current and saves them in a
	 * file.
	 * Ideally the defaults are going to be used just once - the first time the
	 * game is started */
	public static void setKeyBindings() {

		try {
			initialiseReader(KEY_BINDINGS_FILE);
			keyBindings.load(fileReader);
			GameOptions.fileReader.close();
		} catch (Exception e) {
			System.out.println("exception caught");
			resetKeysToDefaults();
			saveKeyBindingsInFile();
		}
	}
	
	/** 
	 * Called when the game is started.
	 * If there is a file with sound values - it sets the current values to
	 * those from the file.
	 * If there isn't - sets the default values as current and saves them in a
	 * file.
	 * Ideally the defaults are going to be used just once - the first time the
	 * game is started */
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
	
	/**
	 * Resets the current sound values to the default ones
	 */
	public static void resetSoundValuesToDefaults() {
		soundValues.setProperty(SOUND_VOLUME_VOLUME,DEFAULT_SOUND_VOLUME);
		soundValues.setProperty(MUSIC_VOLUME_VOLUME,DEFAULT_MUSIC_VOLUME);
		soundValues.setProperty(SOUND_VOLUME_MASTER,DEFAULT_SOUND_MASTER);
		soundValues.setProperty(MUSIC_VOLUME_MASTER,DEFAULT_MUSIC_MASTER);
	}
	
	/**
	 * Resets the current key bindings values to the default ones
	 */
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
		keyBindings.setProperty(DEFAULT_CLOSE_MANUAL_BUTTON, DEFAULT_CLOSE_MANUAL_BUTTON);
	}
	
	/**
	 * Saves the current key bindings to a file 
	 */
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
	
	/**
	 * Save the current sound values to a file
	 */
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

	/**
	 * Changes key binding value by provided default value.
	 * For example, if you want to change the key binding for pitching up from S to M, you call
	 * changeKeyByDefualtValue(GameOptions.DEFAULT_PITCH_UP_BUTTON, KeyEvent.VK_M);
	 * @param default Value The default key value to be changed. Constants provided in the class.
	 * @param newValue The new value wanted. Preferably, use KeyEvent's constants
	 */
	public static void changeKeyByDefaultValue(String defaultValue, int newValue) {
		GameOptions.keyBindings.setProperty(defaultValue, String.valueOf(newValue));
	}
	
	/**
	 * Change the value for the sound effects with provided value 
	 * @param newValue The value provided
	 */
	public static void changeSound(String newValue) {
		if(AudioPlayer.isUsingMaster){
			GameOptions.soundValues.setProperty(SOUND_VOLUME_MASTER, String.valueOf(newValue));
		} else {
			GameOptions.soundValues.setProperty(SOUND_VOLUME_VOLUME, String.valueOf(newValue));
		}
	}
	
	/**
	 * Change the value for the in-game music with provided value 
	 * @param newValue The value provided
	 */
	public static void changeMusic(String newValue) {
		if(AudioPlayer.isUsingMaster){
			GameOptions.soundValues.setProperty(MUSIC_VOLUME_MASTER, String.valueOf(newValue));
		} else {
			GameOptions.soundValues.setProperty(MUSIC_VOLUME_VOLUME, String.valueOf(newValue));
		}
	}
	
	/**
	 * Getting current key binding value by providing the default one
	 * @param defaultValue The default value provided. Those are provided as constants in this class.
	 * @return The current value for the wanted action
	 */
	public static int getCurrentKeyValueByDefault(String defaultValue) {
		return Integer.valueOf(keyBindings.getProperty(defaultValue));
	}
	
	/**
	 * Gets the current sound effect volume
	 * @return Current sound effect volume  
	 */
	public static float getCurrentSoundValue() {
		if(AudioPlayer.isUsingMaster){
			return Float.valueOf(soundValues.getProperty(SOUND_VOLUME_MASTER));
		} else {
			return Float.valueOf(soundValues.getProperty(SOUND_VOLUME_VOLUME));
		}
	}
	
	/**
	 * Gets the current in-game music volume
	 * @return Current in-game music volume
	 */
	public static float getCurrentMusicValue() {
		if(AudioPlayer.isUsingMaster){
			return Float.valueOf(soundValues.getProperty(MUSIC_VOLUME_MASTER));
		} else {
			return Float.valueOf(soundValues.getProperty(SOUND_VOLUME_VOLUME));
		}
	}
	
	/**
	 * Checks if a key is taken in the current key bindings
	 * @param keyCode Provided key code
	 * @return Boolean for if the key is taken(true) or not(false)
	 */
	public static boolean checkIfKeyTaken(int keyCode) {
		return keyBindings.containsValue(String.valueOf(keyCode));

	}
	
	/**
	 * Checks if a key pressed is for an action that needs to be sent to the server
	 * For example, opening the manual with a key is not an action that needs to be sent to the server,
	 * but firing a bullet - is 
	 * @param keyCode Key code of the pressed key
	 * @return Boolean of whether the key needs to be sent to the server(true) or not 
	 */
	public static boolean checkIfKeyToBeSentToServer(int keyCode){
		if(keyCode == getCurrentKeyValueByDefault(DEFAULT_MANUAL_BUTTON) ||
				keyCode == getCurrentKeyValueByDefault(DEFAULT_MANUAL_NEXT_BUTTON) ||
				keyCode == getCurrentKeyValueByDefault(DEFAULT_MANUAL_PREV_BUTTON) ||
				keyCode == getCurrentKeyValueByDefault(DEFAULT_CLOSE_MANUAL_BUTTON)){
			return false;
		} else {
			return checkIfKeyTaken(keyCode);
		}
	}
	
	/**
	 * Gets all the key bindings. Used just in JUnit testing
	 * @return
	 */
	public Properties getKeyBindings(){
		return this.keyBindings;
	}

}
