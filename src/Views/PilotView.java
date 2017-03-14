package Views;

import static GameLogic.GameOptions.BUTTON_FONT;
import static Views.ViewConstants.UI_BACKGROUND_COLOR;

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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

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
public class PilotView extends JPanel implements Observer {

    private static final String WON_GAME = "VICTORY";
    private static final String LOSE_GAME = "FAILURE";

    private Screen screen;
    private SpeedometerView speedometerView;

    private JButton manual;
    private ManualView instructions;
    private JLabel steeringWheelView;

    private GameClient gameClient;
    private String playerNickname;

    private boolean UIinitialised = false;

    private EngineerAI engAI;

    private JLayeredPane UILayeredPane;
    private JPanel UIBaseLayer;
    public JFrame parentFrame;
    private JPanel UIpanel;

    private MouseMotionListener screenMouseListener;
    private double steeringWheelAngle = 0;
    private GameChat chatWindow;
    
    private final Set<String> pressedKeys = new HashSet<String>();

    /**
     * Creates a new PilotView. This encapsulates the entire View of the Pilot player.
     * @param playerNickname The nickname of the player controlling this view.
     * @param gameClient     The GameClient handling network connections for this player.
     */
    public PilotView(String playerNickname, GameClient gameClient, JFrame parentFrame, boolean ai) {
        super();

        if (ai) {
            engAI = new EngineerAI(gameClient, playerNickname);
            gameClient.addObserver(engAI);
        }

        this.playerNickname = playerNickname;

        this.gameClient = gameClient;
        gameClient.addObserver(this);

        setFocusable(true);

        this.parentFrame = parentFrame;
        this.UILayeredPane = parentFrame.getLayeredPane();
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
            	if(GameOptions.checkIfKeyTaken(keyEvent.getKeyCode())){
            		pressedKeys.add(getKeyCodeToInstruction(keyEvent.getKeyCode()));
            	}
                if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_1_BUTTON)) {
                    AudioPlayer.playSoundEffect(AudioPlayer.LASER_FIRE_EFFECT);
                } else if (keyEvent.getKeyCode() == GameOptions
                        .getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_2_BUTTON)) {
                    AudioPlayer.playSoundEffect(AudioPlayer.PLASMA_FIRE_EFFECT);
                } else if (keyEvent.getKeyCode() == GameOptions
                        .getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_3_BUTTON)) {
                    AudioPlayer.playSoundEffect(AudioPlayer.TORPEDO_FIRE_EFFECT);
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_LEFT_BUTTON)) {
                    steeringWheelAngle = -Math.PI/4;
                    steeringWheelView.revalidate();
                    steeringWheelView.repaint();
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_RIGHT_BUTTON)) {
                    steeringWheelAngle = Math.PI/4;
                    steeringWheelView.revalidate();
                    steeringWheelView.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            	pressedKeys.remove(getKeyCodeToInstruction(keyEvent.getKeyCode()));
            	if(keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_LEFT_BUTTON) || keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_RIGHT_BUTTON)){
            		steeringWheelAngle = 0;
                    steeringWheelView.revalidate();
                    steeringWheelView.repaint();
            	}
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                // If we click anywhere other than the chat window, send focus back to the game.
                if(!chatWindow.getBounds().contains(mouseEvent.getPoint())) {
                    System.out.println("Mouse clicked outside of chat");
                    parentFrame.requestFocusInWindow();
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
    private void initialiseUI() {
        if(UIinitialised) {
            UILayeredPane.removeAll();
            UIBaseLayer.removeAll();
        }

        Ship s = findPlayerShip();
        while (s == null) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s = findPlayerShip();
        }

        // initialiseWeapons(s);
        initialiseManualButton();
        initialiseSpeedometer();
        initialiseScreen();
        initialiseChatWindow(gameClient, playerNickname);

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
     * Add all of the UI components to the JPanel.
     */
    private void addAllComponents() {
        try {
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

            BufferedImage steeringWheelImage = ImageIO.read(new File(System.getProperty("user.dir") + "/res/img/steeringwheel.png"));
            AffineTransform t = new AffineTransform();
            t.rotate(steeringWheelAngle);
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

            System.out.println("Width: " + steeringWheelView.getWidth());
            System.out.println("Height: " + steeringWheelView.getHeight());

            UILayeredPane.add(steeringWheelView, JLayeredPane.PALETTE_LAYER);

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
                    parentFrame.getHeight() - (parentFrame.getHeight() / 7),
                    parentFrame.getWidth() / 7,
                    parentFrame.getHeight() / 7);
            speedometerView.setOpaque(true);
            speedometerView.setForeground(UI_BACKGROUND_COLOR);
            speedometerView.setBackground(UI_BACKGROUND_COLOR);
            UILayeredPane.add(speedometerView, JLayeredPane.PALETTE_LAYER);


        } catch (IOException e) {
            // should never get here.
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
        this.screen = new Screen(playerNickname, true);
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
        this.manual.addActionListener(e -> showManual());
        this.manual.setFocusable(false);
    }

    private void showManual() {
    	if(this.instructions == null){
    		initialiseManualView(getHeight() - 100);
    		this.instructions.setBounds(50,50, getWidth() - 100, getHeight() - 100);
    		UILayeredPane.add(instructions, JLayeredPane.MODAL_LAYER);
        }

        this.instructions.setVisible(!instructions.isVisible());
    }

    private void initialiseManualView(int height) {
    	this.instructions = new ManualView(gameClient.keySequence.getAllKeys(), gameClient.keySequence.getKeysSize(), height);
        this.instructions.setVisible(false);
    }


    /**
     * Given a Ship, this will initialise the weapon progress bars to their initial values and set their maximum values
     *
     * @param s This players Ship object
     */
/*    private void initialiseWeapons(Ship s) {
        if (s != null) {
            plasmaBlasterView = new WeaponView("Plasma Blaster", false);
            laserBlasterView = new WeaponView("Laser Blaster", false);
            torpedosView = new WeaponView("Torpedos", false);
        } else {
            System.out.println("Ship is null? Oh dear oh dear");
        }
    }*/

    /**
     * Finds the players Ship within all of the objects in the Map.
     * @return The players Ship object. If the ship could not be found, this will return null.
     */
    private Ship findPlayerShip() {
        Map m = gameClient.getMap();

        for (Body b : m.bodies()) {
            if (b instanceof Ship) {
                Ship s = (Ship) b;
                if (s.getPilotName().equals(playerNickname)) {
                    return s;
                }
            }
        }

        return null;
    }


    @Override
    public void update(Observable observable, Object o) {
        if (!UIinitialised) {
            try {
                initialiseUI();
                UIinitialised = true;
            } catch (Exception e) {
                // Oh well.
                System.out.println("Update Error: " + e.getMessage());
                System.out.println("Localised message: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        } else {
            Map m = gameClient.getMap();
            screen.setMap(m);

            Ship s = findPlayerShip();
            speedometerView.updateSpeedLevel(s.getVelocity().length());
        }
        for(String inst : pressedKeys){
        	gameClient.send(inst);
        }
    }

    private void displayFullScreenMessage(String message) {
        JLabel l = new JLabel(message);
        l.setFont(GameOptions.FULLSCREEN_BOLD_TEXT_FONT);
        // TODO: Find the real size of this
        l.setBounds(100, 100, 600, 600);
        UILayeredPane.add(l, JLayeredPane.DRAG_LAYER);
    }
}
