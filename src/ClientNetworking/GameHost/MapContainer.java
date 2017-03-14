package ClientNetworking.GameHost;

import java.util.Random;

import GameLogic.Asteroid;
import GameLogic.Map;
import GameLogic.Resource;
import GameLogic.Ship;
import GameLogic.Weapon;
import Geometry.*;
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
			System.out.println("added ship"+position/2);
		}
		return gameMap.size()-1;
	}

	public void generateTerrain() {
        Random r = new Random();
        for (int i = 0; i < ASTEROID_NUMBER; i++) {
            Asteroid a = new Asteroid(
                new Vector(
                    r.nextDouble() * gameMap.getDimensions().getX(),
                    r.nextDouble() * gameMap.getDimensions().getY(),
                    r.nextDouble() * gameMap.getDimensions().getZ()
                ),
                new Rotation(r.nextDouble()*Math.PI*2, r.nextDouble()*Math.PI, r.nextDouble()*Math.PI*2)
            );

            gameMap.add(a);

            boolean overlaps = true;
            while(overlaps) {
				overlaps = false;

				for(int bID : gameMap.bodyIDs()) {
					if(gameMap.overlaps(a.getID(), bID) && bID != a.getID()) {
						overlaps = true;
						generateNewAsteroidPosition(a);
						break;
					}
				}

				System.out.println("Help, I'm stuck in this loop!");
			}

/*            boolean overlaps = false;
            for (int bID : gameMap.bodyIDs()) {
                if (gameMap.overlaps(a.getID(), bID)) {
                    overlaps = true;
                    break;
                }
            }
            
            if (overlaps) {
                i--;
            } else {
                gameMap.add(a);
            }*/
        }
	}

	private void generateNewAsteroidPosition(Asteroid a) {
		Random r = new Random();
		a.setPosition(new Vector(
				r.nextDouble() * gameMap.getDimensions().getX(),
				r.nextDouble() * gameMap.getDimensions().getY(),
				r.nextDouble() * gameMap.getDimensions().getZ()
		));
		a.setOrientation(new Rotation(r.nextDouble()*Math.PI*2, r.nextDouble()*Math.PI, r.nextDouble()*Math.PI*2));
	}

	public synchronized void updateMap(String str, int position) {
		Ship playerShip = (Ship) (gameMap.get(position));

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
				playerShip.rollLeft();
				break;
			case "rollRight":
				playerShip.rollRight();
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
				playerShip.getWeapon(Weapon.Type.TORPEDO).increaseAmmo();;
				break;
			case "plasmaReplenish":
				playerShip.getWeapon(Weapon.Type.PLASMA).increaseAmmo();;
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
	public void addDummyBody()
	{
		gameMap.add(new Body());
	}
	public void delete(int ID) {
		gameMap.get(ID).destroy();
	}
}
