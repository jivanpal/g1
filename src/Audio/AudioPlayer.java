package Audio;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
/**
 * Class which play the audio in the game
 * at the moment only .wav files are supported!
 * @author Ivan Panchev
 *
 */
public class AudioPlayer {
	
	//the directories to each sound file --
	private static String pathToProjectAudio = System.getProperty("user.dir") + "/src/Audio/";
	public static String MENU_SCREEN_TUNE = pathToProjectAudio + "intro_screen.wav";
	public static String IN_GAME_TUNE = pathToProjectAudio + "game_audio.wav";
	public static String MOUSE_CLICK_EFFECT = pathToProjectAudio + "mouse_click.wav";
	
	private static Clip musicClip;
	private static Clip soundEffectClip;
	
	private static FloatControl musicVolumeControl;
	private static FloatControl soundEffectVolumeControl;
	/**
	 * Method which plays a non-looping sound effect
	 * @param sound The directory to the file to be played 
	 */
	public static synchronized void playSoundEffect(String sound) {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(sound).getAbsoluteFile());
			soundEffectClip = AudioSystem.getClip();
			soundEffectClip.open(audioInputStream);
			soundEffectVolumeControl = (FloatControl) AudioPlayer.getMusicClip().getControl(FloatControl.Type.MASTER_GAIN);
			soundEffectVolumeControl.setValue(0.0f);
			soundEffectClip.start();
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}
	
	public static void setMusicVolume(float volume){
		musicVolumeControl.setValue(volume);
	}
	
	public static void setSoundEffectVolume(float volume){
		soundEffectVolumeControl.setValue(volume);
	}
	
	/**
	 * Method which plays a looping music -- good for game menus
	 * @param sound The directory to the file to be played 
	 */
	public static synchronized void playMusic(String sound) {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(sound).getAbsoluteFile());
			musicClip = AudioSystem.getClip();
			musicClip.open(audioInputStream);
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
		if(musicClip != null){
			musicClip.stop();
		}
	}
	
	/**
	 * Stops the non-looping sound effect
	 */
	public static void stopSoundEffect() {
		if(soundEffectClip != null){
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
