package JUnit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import GameLogic.KeyBindings;

public class KeyBidingsTest {
	
	Properties defaultKeyBindings;
	Properties keyBindingsFromFile;
	
	@Before
	public void init(){
		defaultKeyBindings = new Properties();
		
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_FIRE_WEAPON_1_BUTTON, KeyBindings.DEFAULT_FIRE_WEAPON_1_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_FIRE_WEAPON_2_BUTTON, KeyBindings.DEFAULT_FIRE_WEAPON_2_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_FIRE_WEAPON_3_BUTTON, KeyBindings.DEFAULT_FIRE_WEAPON_3_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_ACCELERATE_BUTTON, KeyBindings.DEFAULT_ACCELERATE_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_DECELERATE_BUTTON, KeyBindings.DEFAULT_DECELERATE_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_PITCH_DOWN_BUTTON, KeyBindings.DEFAULT_PITCH_DOWN_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_PITCH_UP_BUTTON, KeyBindings.DEFAULT_PITCH_UP_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_ROLL_LEFT_BUTTON, KeyBindings.DEFAULT_ROLL_LEFT_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_ROLL_RIGHT_BUTTON, KeyBindings.DEFAULT_ROLL_RIGHT_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_OVERDRIVE_BUTTON, KeyBindings.DEFAULT_OVERDRIVE_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_MANUAL_BUTTON, KeyBindings.DEFAULT_MANUAL_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_MANUAL_NEXT_BUTTON, KeyBindings.DEFAULT_MANUAL_NEXT_BUTTON);
		defaultKeyBindings.setProperty(KeyBindings.DEFAULT_MANUAL_PREV_BUTTON, KeyBindings.DEFAULT_MANUAL_PREV_BUTTON);
		
		keyBindingsFromFile = new Properties();
		try {
			keyBindingsFromFile.load( new FileReader(new File(KeyBindings.FILE_NAME)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("static-access")
	@Test
	public void test() {
		KeyBindings actualKeyBindigns = new KeyBindings();
		actualKeyBindigns.setKeyBindings();
		assertEquals(keyBindingsFromFile, actualKeyBindigns.getKeyBindings());
		
		actualKeyBindigns.resetKeysToDefaults();
		assertEquals(defaultKeyBindings, actualKeyBindigns.getKeyBindings());
	}

}
