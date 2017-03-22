package ClientNetworking.GameHost;

import java.util.Random;

import GameLogic.Asteroid;
import GameLogic.Map;
import GameLogic.Resource;
import GameLogic.Ship;
import GameLogic.Weapon;
import Geometry.*;
import Physics.Body;
/**
 * The Server side map container
 * @author Svetlin
 *
 */
public class MapContainer {
	//variables

	public static int ASTEROID_NUMBER = 90;

	public static final int MAP_SIZE = 500;
	public Map gameMap = new Map(MAP_SIZE, MAP_SIZE, 100);

	/**
	 * Empty Constructor
	 */
	public MapContainer() {
	}

	/**
	 * Add a ship to the map, it's position based on the id of the player
	 * @param position the position of the pilot in the lobby
	 * @param pilot the pilot's name
	 * @param engineer the engineer's name
	 * @return the id of the ship in the map
	 */
	public int addShip(int position, String pilot, String engineer) {
		if (position % 2 == 0) {
			Ship ship = (new Ship(pilot, engineer));
			ship.setPosition(new Vector(position % 4 == 0 ? 0 : gameMap.getDimensions().getX() / 2,
					position < 4 ? 0 : gameMap.getDimensions().getY() / 2, 0));
			gameMap.add(ship);
			System.out.println("added ship" + position / 2);
			return ship.getID();
		}
		return -1;
	}
	/**
	 * Generate the asteroids 
	 */
	public void generateTerrain() {
		Random r = new Random();
		for (int i = 0; i < ASTEROID_NUMBER; i++) {
			Asteroid a = new Asteroid(new Vector(r.nextDouble() * gameMap.getDimensions().getX(),
					r.nextDouble() * gameMap.getDimensions().getY(), r.nextDouble() * gameMap.getDimensions().getZ()),
					new Rotation(r.nextDouble() * Math.PI * 2, r.nextDouble() * Math.PI, r.nextDouble() * Math.PI * 2));

			gameMap.add(a);

			boolean overlaps = true;
			while (overlaps) {
				overlaps = false;

				for (int bID : gameMap.bodyIDs()) {
					if (gameMap.overlaps(a.getID(), bID) && bID != a.getID()) {
						overlaps = true;
						generateNewAsteroidPosition(a);
						break;
					}
				}
			}
		}
	}

	private void generateNewAsteroidPosition(Asteroid a) {
		Random r = new Random();
		a.setPosition(new Vector(r.nextDouble() * gameMap.getDimensions().getX(),
				r.nextDouble() * gameMap.getDimensions().getY(), r.nextDouble() * gameMap.getDimensions().getZ()));
		a.setOrientation(
				new Rotation(r.nextDouble() * Math.PI * 2, r.nextDouble() * Math.PI, r.nextDouble() * Math.PI * 2));
	}
	
	/**
	 * Update the map with an instruction from a client
	 * @param str the instruction sent by the client
	 * @param name the name of the client
	 * @param ID the Id of the ship
	 */
	public synchronized void updateMap(String str, String name,int ID) {
		Ship playerShip = gameMap.get(ID)==null? null : (Ship)gameMap.get(ID);
		boolean onmap = false;
		onmap = playerShip != null;
		if (onmap) {
			try {
				switch (str) {
				case "fireWeapon1":
					gameMap.add(playerShip.fire(Weapon.Type.LASER));
					break;
				case "fireWeapon2":
					gameMap.add(playerShip.fire(Weapon.Type.PLASMA));
					break;
				case "fireWeapon3":
					gameMap.add(playerShip.fire(Weapon.Type.TORPEDO));
					break;
				case "accelerate":
					playerShip.thrustForward();
					break;
				case "decelerate":
					playerShip.thrustReverse();
					break;
				case "pitchDown":
					playerShip.pitchDown();
					break;
				case "pitchUp":
					playerShip.pitchUp();
					break;
				case "rollLeft":
					playerShip.rotateLeft();
					break;
				case "rollRight":
					playerShip.rotateRight();
					break;
				case "shieldReplenish":
					playerShip.getResource(Resource.Type.SHIELDS).increase();
					break;
				case "fuelReplenish":
					playerShip.getResource(Resource.Type.ENGINES).increase();
					break;
				case "laserReplenish":
					playerShip.getWeapon(Weapon.Type.LASER).increaseAmmo();
					break;
				case "torpedoReplenish":
					playerShip.getWeapon(Weapon.Type.TORPEDO).increaseAmmo();
					;
					break;
				case "plasmaReplenish":
					playerShip.getWeapon(Weapon.Type.PLASMA).increaseAmmo();
					;
					break;
				default:
					throw new IllegalArgumentException("You done sent the wrong string yo");
				}
			} catch (Exception e) {
				System.out.println("error in updateMap()");
				e.printStackTrace();
			}
		}
	}
	/**
	 * call the update method for the map (done in GameClock)
	 */
	public synchronized void updateMap() {
		gameMap.update();
	}

	/**
	 * unused
	 */
	public void addDummyBody() {
		boolean x = gameMap.add(new Body());
		System.out.println("Bot added is " + x);
	}
	/**
	 * Destroy an object from the map based on id
	 * @param ID the id of the object
	 */
	public void delete(int ID) {
		gameMap.get(ID).destroy();
	}
}
