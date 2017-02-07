package GameLogic;

/**
 * Class which represents the engines of the ship
 */
public class Engines {
	
	private static final int DEFAULT_FUEL_LEVEL = 0;
	private static final int DEFAULT_FUEL_MAX_LEVEL = 0;
	private static final int DEFAULT_FUEL_CHANGE = 0;
	
	private Resource fuel; //amount of fuel left as a percentage between 0.0 and 1.0
	private boolean isWorking; //whether or not it is working properly 
	
	public Engines(){
		fuel = new Resource(DEFAULT_FUEL_LEVEL, DEFAULT_FUEL_MAX_LEVEL, DEFAULT_FUEL_CHANGE);
		isWorking = true; 
	}
	
	public int getFuel(){
		return this.fuel.get();
	}
	
	public boolean getIsWorking(){
		return this.isWorking;
	}
	
	public void increaseFuel(){
		this.fuel.increase();
	}
	
	public void decreaseFuel(){
		this.fuel.decrease();
	}
	
	public void customChangeFuel(int change){
		this.fuel.change(change);
	}
}
