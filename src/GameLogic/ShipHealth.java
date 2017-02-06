package game.logic;

public class ShipHealth {
	
	private static final int DEFAULT_HEALTH= 0;
	private static final int DEFAULT_MAX_HEALTH = 0;
	private static final int DEFAULT_HEALTH_CHANGE = 0;

	private Resource health;
	
	public ShipHealth(){
		health = new Resource(DEFAULT_HEALTH, DEFAULT_MAX_HEALTH, DEFAULT_HEALTH_CHANGE);
	}
	
	public int getHealth(){
		return this.health.get();
	}
	
	public void increaseHealth(){
		this.health.increase();
	}

	public void decreaseHealth(){
		this.health.decrease();
	}
	
	public void cusomChangeHealth(int change){
		this.health.change(change);
	}
	
}
