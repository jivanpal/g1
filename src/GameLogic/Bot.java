package GameLogic;

import Physics.*;

/**
 * A superclass for game objects that implement some kind of AI.
 * @author jivan
 */
public abstract class Bot {
    private Map map;
    private Body bot;
    
/// CONSTRUCTORS
    
    /**
     * Given a body and the game map that it is on, turn that body into a bot.
     * @param   map The game map that the body is on.
     * @param   bot The body to define some AI behaviour for; the body to turn into a bot.
     */
    public Bot(Map map, Body bot) {
        this.map = map;
        if (map.contains(bot)) {
            this.bot = bot;
        } else {
            throw new IllegalArgumentException("The specified body does not exist on the specified map.");
        }
        
        map.getBotList().add(this);
    }
    
    /**
     * Given a map and the index of a body on that map, turn that body into a bot.
     * @param   map         The game map that the body is on.
     * @param   botMapIndex The map index of the body to turn into a bot.
     */
    public Bot(Map map, int botMapIndex) {
        this.map = map;
        bot = map.get(botMapIndex);
        
        map.getBotList().add(this);
    }
    
/// INSTANCE METHODS
    
// Getters
    
    /**
     * Get the map that this instance is on / was constructed from.
     */
    public Map getMap() {
        return map;
    }
    
    /**
     * Get the body that this instance was constructed from.
     */
    public Body getBotBody() {
        return bot;
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
