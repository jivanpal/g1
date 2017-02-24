package Views;

import ClientNetworking.GameClient.GameClientReceiver;
import ClientNetworking.GameHost.MapContainer;
import GameLogic.Map;
import GameLogic.Ship;
import Graphics.Screen;
import UI.ClientShipObservable;

import javax.swing.*;
import javax.swing.border.Border;

import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;
import GameLogic.GameOptions;
import sun.swing.UIClientPropertyKey;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by James on 01/02/17.
 */
public class PilotView extends JPanel implements KeyListener, Observer
{

	private final Screen screen;
	private final SpeedometerView speedometerView;
	private final WeaponView plasmaBlasterView;
	private final WeaponView laserBlasterView;
	private final WeaponView torpedosView;

	private final InstructionsView instructionsView;

	private GameClient gameClient;

	private JLayeredPane UIContainer;
	private String playerNickname;

	public PilotView(String playerNickname, GameClient gameClient)
	{
		this.playerNickname = playerNickname;
		this.setLayout(new BorderLayout());
		this.gameClient = gameClient;
		gameClient.addObserver(this);
		screen = new Screen(playerNickname, true);
		screen.setSize(1000, 800);
		screen.setMaximumSize(new Dimension(1000, 800));
		screen.setMinimumSize(new Dimension(1000, 800));
		screen.setPreferredSize(new Dimension(1000, 800));

		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent mouseEvent)
			{

			}

			@Override
			public void mouseMoved(MouseEvent mouseEvent)
			{
				final int x = mouseEvent.getX();
				final int y = mouseEvent.getY();

				final Rectangle screenBounds = screen.getBounds();
				if (screenBounds != null && screenBounds.contains(x, y))
				{
					getParent().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				}
				else
				{
					getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		System.out.println("Adding the screen");
		this.add(screen, BorderLayout.CENTER);

		speedometerView = new SpeedometerView();

		plasmaBlasterView = new WeaponView("Plasma Blaster", false);

		// default plasma blaster to be highlighted, remove at a later date!
		plasmaBlasterView.setHighlightWeapon(true);

		laserBlasterView = new WeaponView("Laser Blaster", false);
		torpedosView = new WeaponView("Torpedos", false);

		instructionsView = new InstructionsView();

		for(int i = 0; i < gameClient.keySequence.length; i++) {
			String instruction = String.valueOf(gameClient.keySequence[i]);
			instructionsView.addInstruction(instruction);
		}

		System.out.println("Making the weapon panel");
		Container weaponPanel = new Container();
		weaponPanel.add(plasmaBlasterView);
		weaponPanel.add(laserBlasterView);
		weaponPanel.add(torpedosView);
		weaponPanel.setLayout(new BoxLayout(weaponPanel, BoxLayout.Y_AXIS));

		Container UIpanel = new Container();
		UIpanel.setPreferredSize(new Dimension(1920, 300));
		UIpanel.setMinimumSize(new Dimension(1920, 300));
		UIpanel.setMaximumSize(new Dimension(1920, 300));
		UIpanel.setSize(new Dimension(1920, 300));
		System.out.println("Adding weapons, speed and instructions to the UIPanel");
		UIpanel.add(weaponPanel);
		UIpanel.add(speedometerView);
		UIpanel.add(instructionsView);
		UIpanel.setLayout(new BoxLayout(UIpanel, BoxLayout.X_AXIS));

		System.out.println("Adding UIPanel");
		this.add(UIpanel, BorderLayout.SOUTH);

		addKeyListener(this);
		setFocusable(true);

		// starting the in-game sounds
		/*try {
			AudioPlayer.stopMusic();
			AudioPlayer.playMusic(AudioPlayer.IN_GAME_TUNE);
		} catch (Exception e) {
			// TODO: Fix
			// In game sound failed to load? Hopefully the game will no longer hang. This fix
			// doesn't appear to work. Never starting the sound allows me to load though? - James
			AudioPlayer.stopMusic();
			AudioPlayer.stopSoundEffect();
			e.printStackTrace();

			getParent().revalidate();
			getParent().repaint();
		}*/
	}

	@Override
	public void keyTyped(KeyEvent keyEvent)
	{
	}

	@Override
	public void keyPressed(KeyEvent keyEvent)
	{
		if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_1_BUTTON))
		{
			gameClient.send("fireWeapon1");
		}
		else if (keyEvent.getKeyCode() == GameOptions
				.getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_2_BUTTON))
		{
			gameClient.send("fireWeapon2");
		}
		else if (keyEvent.getKeyCode() == GameOptions
				.getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_3_BUTTON))
		{
			gameClient.send("fireWeapon3");
		}
		else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ACCELERATE_BUTTON))
		{
			gameClient.send("accelerate");
		}
		else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_DECELERATE_BUTTON))
		{
			gameClient.send("decelerate");
		}
		else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_PITCH_DOWN_BUTTON))
		{
			gameClient.send("pitchDown");
		}
		else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_PITCH_UP_BUTTON))
		{
			gameClient.send("pitchUp");
		}
		else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_LEFT_BUTTON))
		{
			gameClient.send("rollLeft");
		}
		else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_RIGHT_BUTTON))
		{
			gameClient.send("rollRight");
		}
	}

	@Override
	public void keyReleased(KeyEvent keyEvent)
	{

	}

	@Override
	public void update(Observable observable, Object o)
	{
		Map m = gameClient.getMap();
		screen.setMap(m);

		for(int i = MapContainer.ASTEROID_NUMBER; i < m.size(); i++) {
			if(m.get(i) instanceof Ship) {
				Ship s = (Ship) m.get(i);
				if(s.getPilotName().equals(playerNickname)) {
					speedometerView.updateSpeedLevel(s.getVelocity().length());
					laserBlasterView.updateWeaponAmmoLevel(s.getLaserBlasterAmmo());
					plasmaBlasterView.updateWeaponAmmoLevel(s.getPlasmaBlasterAmmo());
					torpedosView.updateWeaponAmmoLevel(s.getTorpedoWeaponAmmo());
				}
			}
		}
	}
}
