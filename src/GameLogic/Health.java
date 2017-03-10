package GameLogic;

import java.io.Serializable;

/**
 * Class which represents the engines of the ship
 * @author Ivan Panchev
 */
public class Health implements Serializable {
	
	public static final int DEFAULT_SHIP_HEALTH_LEVEL = 50;
	public static final int DEFAULT_MAX_SHIP_HEALTH_LEVEL = 100;
	
	private Resource shipHealth; 
	
	public Health(){
		shipHealth = new Resource(DEFAULT_MAX_SHIP_HEALTH_LEVEL,DEFAULT_SHIP_HEALTH_LEVEL);
	}
	
	public int getHealth(){
		return this.shipHealth.get();
	}
	
	public void increaseFuel(){
		this.shipHealth.increase();
	}
	
	public void decreaseFuel(){
		this.shipHealth.decrease();
	}
	
	public void customChangeFuel(int change){
		this.shipHealth.alter(change);
	}
}
