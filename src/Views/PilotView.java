package Views;

import static GameLogic.GameOptions.BUTTON_FONT;
import static Views.ViewConstants.UI_BACKGROUND_COLOR;
import static Views.ViewConstants.UI_HEALTH_DAMAGE_COLOR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;

import AI.EngineerAI;
import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;
import GameLogic.GameOptions;
import GameLogic.Global;
import GameLogic.Map;
import GameLogic.Ship;
import Graphics.Screen;
import Physics.Body;

/**
 * Created by James on 01/02/17.
 */
public class PilotView extends AbstractPlayerView implements Observer {

    private static final String WON_GAME = "VICTORY";
    private static final String LOSE_GAME = "FAILURE";

    private SpeedometerView speedometerView;

    private JButton manual;
    private ManualView instructions;
    private JLabel steeringWheelView = new JLabel();

    private boolean UIinitialised = false;

    private EngineerAI engAI = null;

    private JPanel UIBaseLayer;

    private MouseMotionListener screenMouseListener;
    private double steeringWheelAngle = 0;

    private final Set<String> pressedKeys = new HashSet<String>();

    /**
     * Creates a new PilotView. This encapsulates the entire View of the Pilot player.
     * @param playerNickname The nickname of the player controlling this view.
     * @param gameClient     The GameClient handling network connections for this player.
     */
    public PilotView(String playerNickname, GameClient gameClient, JFrame parentFrame, boolean ai) {
        super(playerNickname, gameClient, parentFrame);

        if (ai) {
            engAI = new EngineerAI(gameClient, playerNickname);
            gameClient.addObserver(engAI);
        }

        gameClient.addObserver(this);

        setFocusable(true);

        UIBaseLayer = new JPanel();

        parentFrame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                initialiseUI();
            }

            @Override
            public void componentMoved(ComponentEvent componentEvent) { }

            @Override
            public void componentShown(ComponentEvent componentEvent) {
                initialiseUI();
            }

            @Override
            public void componentHidden(ComponentEvent componentEvent) {
                initialiseUI();
            }
        });

        parentFrame.addKeyListener(new KeyListener() {

        	@Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
            	if(GameOptions.checkIfKeyToBeSentToServer(keyEvent.getKeyCode())){
            		pressedKeys.add(getKeyCodeToInstruction(keyEvent.getKeyCode()));
            	}
                /*if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_LEFT_BUTTON)) {
                	if(steeringWheelAngle != -Math.PI/4){
	                    steeringWheelAngle = -Math.PI/4;
	                    updateSteeringWheel();
                	}
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_RIGHT_BUTTON)) {
                	if(steeringWheelAngle != Math.PI/4){
	                    steeringWheelAngle = Math.PI/4;
	                    updateSteeringWheel();
                	}
                } else */if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_MANUAL_BUTTON)) {
                	showManual();
   
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_MANUAL_NEXT_BUTTON) && instructions.isVisible()) {
                	instructions.goToNextPage();
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_MANUAL_PREV_BUTTON) && instructions.isVisible()) {
                	instructions.goToPreviousPage();
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_CLOSE_MANUAL_BUTTON)
                        && instructions.isVisible()) {
            	    AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
            	    showManual();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            	pressedKeys.remove(getKeyCodeToInstruction(keyEvent.getKeyCode()));
            	/*if(keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_LEFT_BUTTON) || keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_RIGHT_BUTTON)){
            		steeringWheelAngle = 0;
                    updateSteeringWheel();
            	}*/
            }
        });

        parentFrame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                // If we click anywhere other than the chat window, send focus back to the game.
                if(!chatWindow.getBounds().contains(mouseEvent.getPoint())) {
                    System.out.println("Mouse clicked outside of chat");
                    parentFrame.requestFocusInWindow();
                    
                    if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                        gameClient.send("fireWeapon1");
                    } else if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
                        gameClient.send("fireWeapon2");
                    } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                        gameClient.send("fireWeapon3");
                    }
                } else {
                    System.out.println("Mouse clicked inside chat");
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // If we click anywhere other than the chat window, send focus back to the game.
                if(!chatWindow.getBounds().contains(mouseEvent.getPoint())) {
                    System.out.println("Mouse pressed outside of chat");
                    parentFrame.requestFocusInWindow();
                } else {
                    System.out.println("Mouse pressed inside chat");
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                // If we click anywhere other than the chat window, send focus back to the game.
                if(!chatWindow.getBounds().contains(mouseEvent.getPoint())) {
                    System.out.println("Mouse released outside of chat");
                    parentFrame.requestFocusInWindow();
                } else {
                    System.out.println("Mouse released inside chat");
                }

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        initialiseUI();

        // starting the in-game sounds
        AudioPlayer.stopMusic();
        AudioPlayer.playMusic(AudioPlayer.IN_GAME_TUNE);
    }
    
    private String getKeyCodeToInstruction(int keyCode){
    	String inst = "";
   	
    	if (keyCode == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_1_BUTTON)) {
        	inst = "fireWeapon1";
        } else if (keyCode == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_2_BUTTON)) {
        	inst = "fireWeapon2";
        } else if (keyCode == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_3_BUTTON)) {
        	inst = "fireWeapon3";
        } else if (keyCode == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ACCELERATE_BUTTON)) {
        	inst = "accelerate";
        } else if (keyCode == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_DECELERATE_BUTTON)) {
        	inst = "decelerate";
        } else if (keyCode == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_PITCH_DOWN_BUTTON)) {
        	inst = "pitchDown";
        } else if (keyCode == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_PITCH_UP_BUTTON)) {
        	inst = "pitchUp";
        } else if (keyCode == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_LEFT_BUTTON)) {
        	inst = "rollLeft";
        } else if (keyCode == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_RIGHT_BUTTON)) {
        	inst = "rollRight";
        }
    	
    	return inst;
    }

    /**
     * Creates all the elements of the UI and positions them on the screen. Sets all default values of the UI elements.
     */
    protected void initialiseUI() {
        if(UIinitialised) {
            UILayeredPane.removeAll();
            UIBaseLayer.removeAll();
        }

        currentShip = findPlayerShip(gameClient.getMap());
        while (currentShip == null) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentShip = findPlayerShip(gameClient.getMap());
        }

        // initialiseWeapons(s);
        initialiseManualButton();
        initialiseSpeedometer();
        initialiseScreen();
        initialiseChatWindow(gameClient, playerNickname);

        if(engAI != null) {
            initialiseRadar();
        }

        // Add mouse listener which swaps the cursor between being the default and a crosshair.
        this.removeMouseMotionListener(screenMouseListener);
        screenMouseListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                final int x = mouseEvent.getX();
                final int y = mouseEvent.getY();

                final Dimension screenDimension = screen.getSize();
                final Rectangle screenBounds = new Rectangle(0, 0, (int) screenDimension.getWidth(), (int) screenDimension.getHeight());
                 if (screenBounds.contains(x, y)) {
                    getParent().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };
        this.addMouseMotionListener(screenMouseListener);

        addAllComponents();

        this.repaint();
        this.revalidate();
        UIBaseLayer.repaint();
        UIBaseLayer.revalidate();
        UILayeredPane.repaint();
        UILayeredPane.revalidate();

        this.UIinitialised = true;
        System.out.println("Done initialising the UI. I am the Pilot");

        parentFrame.requestFocus();
        parentFrame.setFocusable(true);
        //parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //parentFrame.setUndecorated(true);

        update(null, null);
        screen.setMap(gameClient.getMap());
    }

    /**
     * Flashes some UI elements a color for a very brief peroid of time to indicate that the ship has been damaged
     */
    @Override
    protected void flashUIDamaged(Color c) {
        manual.setBackground(c);
        speedometerView.setBackground(c);

        Timer t = new Timer(DAMAGE_FLASH_TIME, e -> {
            manual.setBackground(UI_BACKGROUND_COLOR);
            speedometerView.setBackground(UI_BACKGROUND_COLOR);
        });
        t.setRepeats(false);
        t.start();
    }

    /**
     * Add all of the UI components to the JPanel.
     */
    private void addAllComponents() {
        UIBaseLayer.setLayout(new BorderLayout());
        UIBaseLayer.add(screen, BorderLayout.CENTER);
        UIBaseLayer.setBounds(0, 0, parentFrame.getWidth(), parentFrame.getHeight());

        JLayeredPaneLayoutManager layoutManager = new JLayeredPaneLayoutManager();

        UILayeredPane.setLayout(layoutManager);
        UILayeredPane.add(UIBaseLayer, JLayeredPane.DEFAULT_LAYER);

        chatWindow.setBounds(0,
                parentFrame.getHeight() - (parentFrame.getHeight() / 7) - (parentFrame.getHeight() / 6),
                parentFrame.getWidth() / 6,
                parentFrame.getHeight() / 6);

        chatWindow.setPreferredSize(new Dimension(parentFrame.getWidth() / 6, parentFrame.getHeight() / 6));
        UILayeredPane.add(chatWindow, JLayeredPane.PALETTE_LAYER);

        updateSteeringWheel();

        manual.setBounds(0,
                parentFrame.getHeight() - (parentFrame.getHeight() / 7),
                parentFrame.getWidth() / 7,
                parentFrame.getHeight() / 7);
        manual.setOpaque(true);
        manual.setBackground(UI_BACKGROUND_COLOR);
        manual.setForeground(Color.white);
        manual.setFont(BUTTON_FONT);
        UILayeredPane.add(manual, JLayeredPane.PALETTE_LAYER);

        speedometerView.setBounds(parentFrame.getWidth() - (parentFrame.getWidth() / 7),
                parentFrame.getHeight() - speedometerView.getPreferredSize().height,
                parentFrame.getWidth() / 7,
                speedometerView.getPreferredSize().height);
        speedometerView.setOpaque(true);
        speedometerView.setForeground(UI_BACKGROUND_COLOR);
        speedometerView.setBackground(UI_BACKGROUND_COLOR);
        UILayeredPane.add(speedometerView, JLayeredPane.PALETTE_LAYER);

        if(engAI != null) {
            radarView.setBounds(parentFrame.getWidth() - parentFrame.getHeight() / 4, 0, parentFrame.getHeight() / 4, parentFrame.getHeight() / 4);
            radarView.setPreferredSize(new Dimension(parentFrame.getHeight() / 4, parentFrame.getHeight() / 4));
            UILayeredPane.add(radarView, JLayeredPane.MODAL_LAYER);
        }
    }
    
    private void updateSteeringWheel(){
    	steeringWheelView.setIcon(null);
    	steeringWheelView.revalidate();
    	steeringWheelView.repaint();
    	BufferedImage steeringWheelImage;
		try {
			steeringWheelImage = ImageIO.read(new File(System.getProperty("user.dir") + "/res/img/steeringwheel.png"));
			AffineTransform t = new AffineTransform();
	        t.rotate(steeringWheelAngle, steeringWheelImage.getWidth()/2, steeringWheelImage.getHeight()/2);
	        AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BILINEAR);
	        steeringWheelImage = op.filter(steeringWheelImage, null);
	        Image resizedWheel = steeringWheelImage.getScaledInstance(parentFrame.getHeight()/5, parentFrame.getHeight()/5, Image.SCALE_SMOOTH);
	        ImageIcon imgIcon = new ImageIcon(resizedWheel);
	        steeringWheelView = new JLabel(imgIcon);

	        steeringWheelView.setMinimumSize(new Dimension(parentFrame.getWidth() / 3, parentFrame.getHeight() / 3));
	        steeringWheelView.setMaximumSize(new Dimension(parentFrame.getWidth() / 3, parentFrame.getHeight() / 3));
	        steeringWheelView.setPreferredSize(new Dimension(parentFrame.getWidth() / 3, parentFrame.getHeight() / 3));
	        steeringWheelView.setBounds((parentFrame.getWidth() / 2) - (parentFrame.getWidth() / 6),
	                2 * (parentFrame.getHeight() / 3),
	                parentFrame.getWidth() / 3,
	                parentFrame.getHeight() / 3);
	        
	        steeringWheelView.revalidate();
	    	steeringWheelView.repaint();
	        
	        UILayeredPane.add(steeringWheelView, JLayeredPane.PALETTE_LAYER);
	        
//	        UILayeredPane.revalidate();
//	        UILayeredPane.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void initialiseChatWindow(GameClient gameClient, String nickname) {
        this.chatWindow = new GameChat(this, gameClient, nickname);
        this.chatWindow.setFocusable(false);
    }

    /**
     * Initialise the Screen for the UI
     */
    private void initialiseScreen() {
        this.screen = new Screen(playerNickname, true, true);
        screen.setPreferredSize(new Dimension(this.getWidth(), 4 * (this.getHeight() / 5)));
        Global.SCREEN_WIDTH = parentFrame.getWidth();
        Global.SCREEN_HEIGHT = parentFrame.getHeight() - (parentFrame.getHeight() / 5);
    }

    /**
     * Initialises the speedometer to its initial values.
     */
    private void initialiseSpeedometer() {
        speedometerView = new SpeedometerView();
        speedometerView.setOpaque(false);
    }

    private void initialiseManualButton() {
        this.manual = new JButton("Manual");
        this.manual.addActionListener(e -> {
            AudioPlayer.playSoundEffect(AudioPlayer.MOUSE_CLICK_EFFECT);
            showManual();
        });
        this.manual.setFocusable(false);
    }

    private void showManual() {
    	if(this.instructions == null){
    		initialiseManualView(getHeight() - 100);
    		this.instructions.setBounds(50,50, getWidth() - 100, getHeight() - 100);
    		UILayeredPane.add(instructions, JLayeredPane.DRAG_LAYER);
        }

        this.instructions.setVisible(!instructions.isVisible());
    }

    private void initialiseManualView(int height) {
    	this.instructions = new ManualView(gameClient.keySequence.getAllKeys(), gameClient.keySequence.getKeysSize(), height);
        this.instructions.setVisible(false);
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);
        
        if(gameActive) {
        try {
            Map m = gameClient.getMap();
            System.out.println("Vel: " + currentShip.getVelocity());
            speedometerView.updateSpeedLevel(currentShip.getVelocity().length());

            if(engAI != null) {
                radarView.updateMap(m);
            }
        } catch (Exception e) {
        	//e.printStackTrace();
        	// Seems the UI hasn't been initialised yet
        }

        for(String inst : pressedKeys){
        	gameClient.send(inst);
        }
        }
    }
}
