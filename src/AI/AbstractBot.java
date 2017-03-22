package AI;

import java.io.Serializable;
import GameLogic.Map;
import Physics.Body;

/**
 * A class that defines methods to wrap automated in-game behaviour (AI)
 * around an instance of `Body`.
 * @author jivan
 */
public abstract class AbstractBot implements Serializable {
/// FIELDS
    private Map map;
    private int ID;
    private Body body;
    private boolean enabled = true;
    
/// CONSTRUCTORS
    
    /**
     * Construct a bot wrapper around a body with a given ID that resides on a given map.
     * @param map the map that the body resides on. 
     * @param bodyID the ID of the body.
     */
    public AbstractBot(Map map, int bodyID) {
        this.map = map;
        body = map.get(bodyID);
        if (body == null) {
            System.err.println("Bot instantiation: body with ID "+bodyID+" doesn't exist on the specified map.");
            new RuntimeException().printStackTrace();
        } else {
            ID = map.nextBotID++;
        }
    }
    
/// INSTANCE METHODS
    
// Getters
    
    /**
     * Get the map that this bot resides on.
     */
    public Map getMap() {
        return map;
    }
    
    /**
     * Get the ID of this bot.
     */
    public int getID() {
        return ID;
    }
    
    /**
     * Get the body that this instance was constructed from.
     */
    public Body getBody() {
        return body;
    }
    
    /**
     * Determine whether this bot is enabled / actively
     * behaving as it is defined, or not.
     * @return `true` if enabled, `false` if not.
     */
    public boolean isEnabled() {
        return enabled;
    }
    
// Setters
    
    /**
     * Set whether this bot wrapper should act on its enclosed body.
     * @param newStatus `true` to enable this instance, `false` to disable it.
     */
    public void setEnabled(boolean newStatus) {
        enabled = newStatus;
    }
    
// Evolution
    
    /**
     * This method is where the behaviour for the bot is defined. The body of
     * this method should describe how the bot's state changes with each tick
     * of the in-game clock, as this method is called with every new frame
     * that is generated.
     */
    public abstract void update();
}
