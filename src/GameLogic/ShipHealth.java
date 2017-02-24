package GameLogic;

import java.io.Serializable;

/**
 * Class which represents the engines of the ship
 * @author Ivan Panchev
 */
public class ShipHealth implements Serializable {
	
	public static final int DEFAULT_SHIP_HEALTH_LEVEL = 0;
	public static final int DEFAULT_MAX_SHIP_HEALTH_LEVEL = 0;
	
	private Resource shipHealth; 
	
	public ShipHealth(){
		shipHealth = new Resource(DEFAULT_MAX_SHIP_HEALTH_LEVEL,DEFAULT_SHIP_HEALTH_LEVEL);
	}
	
	public int getHealth(){
		return this.shipHealth.get();
	}
	
	public void increaseFuel(){
		this.shipHealth.up();
	}
	
	public void decreaseFuel(){
		this.shipHealth.down();
	}
	
	public void customChangeFuel(int change){
		this.shipHealth.change(change);
	}
}
