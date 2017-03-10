package GameLogic;

import java.io.Serializable;

/**
 * A class which represents the shields of a ship in the game
 * @author Ivan Panchev
 *
 */
public class Shields implements Serializable {
	
	public static final int DEFAULT_SHIELDS_LEVEL = 50;
	public static final int DEFAULT_MAX_SHIELDS_LEVEL = 100;

	private Resource shieldsLevel;
	
	public Shields(){
		shieldsLevel = new Resource(DEFAULT_MAX_SHIELDS_LEVEL,DEFAULT_SHIELDS_LEVEL);
	}
	
	public int getShieldsLevel(){
		return this.shieldsLevel.get();
	}
	
	public void increaseShieldsLevel(){
		this.shieldsLevel.increase();
	}

	public void decreaseShieldsLevel(){
		this.shieldsLevel.decrease();
	}
	
	public void cusomChangeShieldsLevel(int change){
		this.shieldsLevel.alter(change);
	}
	
}
