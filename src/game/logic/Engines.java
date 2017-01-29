package game.logic;

import Temp.Resource;

/**
 * Class which represents the engines of the ship
 */
public class Engines {
	
	private static final int DEFAULT_FUEL_LEVEL = 0;
	private static final int DEFAULT_FUEL_MAX_LEVEL = 0;
	private static final int DEFAULT_FUEL_CHANGE = 0;
	
	private Resource fuel; //amount of fuel left as a percentage between 0.0 and 1.0
	
	public Engines(){
		fuel = new Resource(DEFAULT_FUEL_LEVEL, DEFAULT_FUEL_MAX_LEVEL, DEFAULT_FUEL_CHANGE);
	}
	
	public int getFuel(){
		return this.fuel.get();
	}
}
