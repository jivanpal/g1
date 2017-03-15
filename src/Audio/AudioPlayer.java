package Audio;

import java.io.File;
import javax.sound.sampled.*;

import GameLogic.GameOptions;

/**
 * Class which play the audio in the game.Only .wav files are
 * supported!
 * 
 * @author Ivan Panchev
 *
 */
public class AudioPlayer {

	// the directories to each sound file --
	private static String pathToProjectAudio = System.getProperty("user.dir") + "/src/Audio/";
	public static String MENU_SCREEN_TUNE = pathToProjectAudio + "intro_screen.wav";
	public static String IN_GAME_TUNE = pathToProjectAudio + "game_audio.wav";
	public static String MOUSE_CLICK_EFFECT = pathToProjectAudio + "mouse_click.wav";
	public static String LASER_FIRE_EFFECT = pathToProjectAudio + "laser_fire.wav";
	public static String PLASMA_FIRE_EFFECT = pathToProjectAudio + "plasma_fire.wav";
	public static String TORPEDO_FIRE_EFFECT = pathToProjectAudio + "torpedo_fire.wav";
	public static String VICTORY_EFFECT = pathToProjectAudio + "victory.wav";
	public static String FAILURE_EFFECT = pathToProjectAudio + "failure.wav";
	public static String SHIP_SHIELD_DECREASE_EFFECT = pathToProjectAudio + "shield_decrease.wav";
	public static String SHIP_HEALTH_DECREASE_EFFECT = pathToProjectAudio + "health_decrease.wav";

	private static Clip musicClip;
	private static Clip soundEffectClip;

	private static FloatControl musicVolumeControl;
	private static FloatControl soundEffectVolumeControl;
	
	public static boolean isUsingMaster;

	/**
	 * Method which plays a non-looping sound effect
	 * 
	 * @param sound
	 *            The directory to the file to be played
	 */
	public static synchronized void playSoundEffect(String sound) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sound).getAbsoluteFile());
			AudioFormat format = audioInputStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);

			soundEffectClip = (Clip) AudioSystem.getLine(info);
			soundEffectClip.open(audioInputStream);
			
			try {
				soundEffectVolumeControl = (FloatControl) soundEffectClip.getControl(FloatControl.Type.MASTER_GAIN);
				isUsingMaster = true;
			} catch (IllegalArgumentException e) {
				soundEffectVolumeControl = (FloatControl) soundEffectClip.getControl(FloatControl.Type.VOLUME);
				isUsingMaster = false;
			}
			soundEffectVolumeControl.setValue(GameOptions.getCurrentSoundValue());
			soundEffectClip.start();
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public static void setMusicVolume(float volume) {
		musicVolumeControl.setValue(volume);
	}

	public static void setSoundEffectVolume(float volume) {
		soundEffectVolumeControl.setValue(volume);
	}

	/**
	 * Method which plays a looping music -- good for game menus
	 * 
	 * @param sound
	 *            The directory to the file to be played
	 */
	public static synchronized void playMusic(String sound) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sound).getAbsoluteFile());
			AudioFormat format = audioInputStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);

			musicClip = (Clip) AudioSystem.getLine(info);
			musicClip.open(audioInputStream);

			try {
				musicVolumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
				isUsingMaster = true;
			} catch (IllegalArgumentException e) {
				musicVolumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.VOLUME);
				isUsingMaster = false;
			}
			musicVolumeControl.setValue(GameOptions.getCurrentMusicValue());
			musicClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	/**
	 * Stops the looping music
	 */
	public static void stopMusic() {
		if (musicClip != null) {
			musicClip.stop();
		}
	}

	/**
	 * Stops the non-looping sound effect
	 */
	public static void stopSoundEffect() {
		if (soundEffectClip != null) {
			soundEffectClip.stop();
		}
	}

	public static Clip getMusicClip() {
		return musicClip;
	}

	public static Clip getSoundEffectClip() {
		return soundEffectClip;
	}

}
