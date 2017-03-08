package ClientNetworking.GameHost;

import java.util.Random;

import GameLogic.Asteroid;
import GameLogic.Map;
import GameLogic.Ship;
import Geometry.Vector;
import Physics.Body;

public class MapContainer {
	
	public static int ASTEROID_NUMBER = 50;
	public static final int MAP_SIZE = 100;
	public Map gameMap = new Map(MAP_SIZE, MAP_SIZE, MAP_SIZE);

	public MapContainer() {
	}

	public int addShip(int position, String pilot,String engineer) {
		if (position % 2 == 0) {
			Ship ship = (new Ship(pilot,engineer));
			ship.setPosition(new Vector(position % 4 == 0 ? 0 : gameMap.getDimensions().getX() / 2,
					position < 4 ? 0 : gameMap.getDimensions().getY() / 2, 0));
			gameMap.add(ship);
		}
		return gameMap.size()-1;
	}

	public void generateTerrain() {
		Random r = new Random();
		for (int i = 0; i < ASTEROID_NUMBER; i++) {
			Asteroid a = new Asteroid();
			a.setPosition(new Vector(r.nextDouble() * gameMap.getDimensions().getX(),
					r.nextDouble() * gameMap.getDimensions().getY(), r.nextDouble() * gameMap.getDimensions().getZ()));
			boolean overlaps = false;
			for (Body b : gameMap) {
				if (a.isTouching(b)) {
					overlaps = true;
					break;
				}
			}

			if (!overlaps) {
//				a.setVelocity(new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble()).scale(10));
				gameMap.add(a);
			} else {
				i--;
			}
		}
	}

	public synchronized void updateMap(String str, int position) {
		Ship playerShip = (Ship) (gameMap.get(position));
		System.out.println(str);
		//updating the weapon cooldowns
		playerShip.updateWeaponsCooldown();
		try {
			switch (str) {
			case "fireWeapon1":

				gameMap.add(playerShip.fire(Ship.LASER_BLASTER_INDEX));
				break;
			case "fireWeapon2":
				gameMap.add(playerShip.fire(Ship.PLASMA_BLASTER_INDEX));
				break;
			case "fireWeapon3":
				gameMap.add(playerShip.fire(Ship.TORPEDO_WEAPON_INDEX));
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
				playerShip.rollLeft();
				break;
			case "rollRight":
				playerShip.rollRight();
				break;
			case "shieldReplenish":
				playerShip.increseShieldsLevel();
			    break;
			case "fuelReplenish":
				playerShip.increaseFuel();
				break;	
			case "laserReplenish":
				playerShip.increaseWeaponAmmoByIndex(Ship.LASER_BLASTER_INDEX);
				break;
			case "torpedoReplenish":
				playerShip.increaseWeaponAmmoByIndex(Ship.TORPEDO_WEAPON_INDEX);
				break;
			case "plasmaReplenish":
				playerShip.increaseWeaponAmmoByIndex(Ship.PLASMA_BLASTER_INDEX);
				break;
			default:
				throw new IllegalArgumentException("You done sent the wrong string yo");
			}
		} catch (Exception e) {
			System.out.println("error in updateMap()");
		}
	}
	
	public synchronized void updateMap() {
		gameMap.update();
	}
}
