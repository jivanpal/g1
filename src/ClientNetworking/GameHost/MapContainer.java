package ClientNetworking.GameHost;

import java.util.Random;

import GameLogic.Asteroid;
import GameLogic.Map;
import GameLogic.Ship;
import Geometry.Vector;
import Physics.Body;

public class MapContainer {
	
	public int ASTEROID_NUMBER = 100;
	public Map gameMap = new Map(10000, 10000, 10000);

	public MapContainer() {
		generateTerrain();
	}

	public int addShip(int position, String nickname) {
		if (position % 2 == 0) {
			Ship ship = (new Ship(nickname));
			ship.setPosition(new Vector(position % 4 == 0 ? 0 : gameMap.getDimensions().getX() / 2,
					position < 4 ? 0 : gameMap.getDimensions().getY() / 2, 0));
			gameMap.add(ship);
		}
		return gameMap.size()-1;
	}

	private void generateTerrain() {
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
				a.setVelocity(new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble()).scale(10));
				gameMap.add(a);
			} else {
				i--;
			}
		}
	}

	public synchronized void updateMap(String str, int position) {
		Ship playerShip = (Ship) (gameMap.get(position+ASTEROID_NUMBER));
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
			case "pitchDown":
				playerShip.pitchDown();
			case "pitchUp":
				playerShip.pitchUp();
				break;
			case "rollLeft":
				playerShip.rollLeft();
				break;
			case "rollRight":
				playerShip.rollRight();
				break;
			default:
				throw new IllegalArgumentException("You done sent the wrong string yo");
			}
		} catch (Exception e) {
			System.out.println("error in updateMap()");
		}
	}
}
