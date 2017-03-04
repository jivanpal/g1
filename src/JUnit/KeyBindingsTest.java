package JUnit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import GameLogic.GameOptions;

public class KeyBindingsTest {
	Properties defaultKeyBindings;
	Properties keyBindingsFromFile;
	
	@Before
	public void init(){
		defaultKeyBindings = new Properties();
		
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_FIRE_WEAPON_1_BUTTON, GameOptions.DEFAULT_FIRE_WEAPON_1_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_FIRE_WEAPON_2_BUTTON, GameOptions.DEFAULT_FIRE_WEAPON_2_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_FIRE_WEAPON_3_BUTTON, GameOptions.DEFAULT_FIRE_WEAPON_3_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_ACCELERATE_BUTTON, GameOptions.DEFAULT_ACCELERATE_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_DECELERATE_BUTTON, GameOptions.DEFAULT_DECELERATE_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_PITCH_DOWN_BUTTON, GameOptions.DEFAULT_PITCH_DOWN_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_PITCH_UP_BUTTON, GameOptions.DEFAULT_PITCH_UP_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_ROLL_LEFT_BUTTON, GameOptions.DEFAULT_ROLL_LEFT_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_ROLL_RIGHT_BUTTON, GameOptions.DEFAULT_ROLL_RIGHT_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_OVERDRIVE_BUTTON, GameOptions.DEFAULT_OVERDRIVE_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_MANUAL_BUTTON, GameOptions.DEFAULT_MANUAL_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_MANUAL_NEXT_BUTTON, GameOptions.DEFAULT_MANUAL_NEXT_BUTTON);
		defaultKeyBindings.setProperty(GameOptions.DEFAULT_MANUAL_PREV_BUTTON, GameOptions.DEFAULT_MANUAL_PREV_BUTTON);
		
		keyBindingsFromFile = new Properties();
		try {
			keyBindingsFromFile.load( new FileReader(new File(GameOptions.KEY_BINDINGS_FILE)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	@Test
	public void test() {
		GameOptions actualKeyBindigns = new GameOptions();
		actualKeyBindigns.setKeyBindings();
		assertEquals(keyBindingsFromFile, actualKeyBindigns.getKeyBindings());
		
		actualKeyBindigns.resetKeysToDefaults();
		assertEquals(defaultKeyBindings, actualKeyBindigns.getKeyBindings());
	}
}

