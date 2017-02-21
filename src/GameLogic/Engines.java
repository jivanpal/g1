package GameLogic;

java.io.Serializable;

/**
 * Class which represents the engines of the ship
 * @author Ivan Panchev
 */
public class Engines implements Serializable {
	
	private static final int DEFAULT_FUEL_LEVEL = 0;
	private static final int DEFAULT_FUEL_MAX_LEVEL = 0;
	
	private Resource fuel; //amount of fuel left as a percentage between 0.0 and 1.0
	private boolean isWorking; //whether or not it is working properly 
	
	public Engines(){
		fuel = new Resource(DEFAULT_FUEL_MAX_LEVEL,DEFAULT_FUEL_LEVEL);
		isWorking = true; 
	}
	
	public int getFuel(){
		return this.fuel.get();
	}
	
	public boolean getIsWorking(){
		return this.isWorking;
	}
	
	public void increaseFuel(){
		this.fuel.up();
	}
	
	public void decreaseFuel(){
		this.fuel.down();
	}
	
	public void customChangeFuel(int change){
		this.fuel.change(change);
	}
}
