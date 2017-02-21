package GameLogic;

import java.io.Serializable;

/**
 * A class which represents the shilds of a ship in the game
 * @author Ivan Panchev
 *
 */
public class Shields implements Serializable {
	
	private static final int DEFAULT_SHIELDS_LEVEL = 0;
	private static final int DEFAULT_MAX_SHIELDS_LEVEL = 0;
	private static final int DEFAULT_SHIELDS_LEVEL_CHANGE = 0;

	private Resource shieldsLevel;
	
	public Shields(){
		shieldsLevel = new Resource(DEFAULT_MAX_SHIELDS_LEVEL,DEFAULT_SHIELDS_LEVEL);
	}
	
	public int getShieldsLevel(){
		return this.shieldsLevel.get();
	}
	
	public void increaseShieldsLevel(){
		this.shieldsLevel.up();
	}

	public void decreaseShieldsLevel(){
		this.shieldsLevel.down();
	}
	
	public void cusomChangeShieldsLevel(int change){
		this.shieldsLevel.change(change);
	}
	
}
