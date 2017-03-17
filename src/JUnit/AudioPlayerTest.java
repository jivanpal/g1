package JUnit;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import Audio.AudioPlayer;
import GameLogic.GameOptions;

import org.junit.Test;

public class AudioPlayerTest {
	
	@Before
	public void init(){
		
	}

	@Test
	public void test() {
		//asserting all the sound files are here 
		assertTrue((new File(AudioPlayer.FAILURE_EFFECT).exists()));
		assertTrue((new File(AudioPlayer.IN_GAME_TUNE).exists()));
		assertTrue((new File(AudioPlayer.KEY_PRESS_EFFECT).exists()));
		assertTrue((new File(AudioPlayer.KEY_SEQUENCE_FAILED).exists()));
		assertTrue((new File(AudioPlayer.LASER_FIRE_EFFECT).exists()));
		assertTrue((new File(AudioPlayer.MENU_SCREEN_TUNE).exists()));
		assertTrue((new File(AudioPlayer.MOUSE_CLICK_EFFECT).exists()));
		assertTrue((new File(AudioPlayer.PLASMA_FIRE_EFFECT).exists()));
		assertTrue((new File(AudioPlayer.SHIP_HEALTH_DECREASE_EFFECT).exists()));
		assertTrue((new File(AudioPlayer.SHIP_SHIELD_DECREASE_EFFECT).exists()));
		assertTrue((new File(AudioPlayer.TORPEDO_FIRE_EFFECT).exists()));
		assertTrue((new File(AudioPlayer.VICTORY_EFFECT).exists()));
	}

}
