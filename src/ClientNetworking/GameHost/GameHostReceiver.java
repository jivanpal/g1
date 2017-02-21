package ClientNetworking.GameHost;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Random;

import GameLogic.*;
import Geometry.Vector;
import Physics.Body;
import ServerNetworking.ClientTable;


public class GameHostReceiver extends Thread
{
	private Map gameMap;
	private BufferedReader in;
	private ClientTable clientTable;
	private String playerPos;
	private int playerInt;
	public GameHostReceiver(InputStream reader, Map gM, ClientTable cT, String playerPos, String nickname)
	{		
		in = new BufferedReader(new InputStreamReader(reader));
		this.playerPos = playerPos;
		clientTable = cT;
		playerInt = Integer.parseInt(playerPos);
		gameMap = gM;
		
		// If the player added is the pilot, put a new ship on the
		// map in a sensible position.
		if (playerInt % 2 == 0)
		{
			Ship ship = (new Ship(nickname));
			ship.setPosition(new Vector(
					playerInt % 4 == 0 ? 0 : gameMap.getDimensions().getX() / 2,
							playerInt < 4 ? 0 : gameMap.getDimensions().getY() / 2,
					0
			));
			gameMap.add(ship);
		}
		Random r = new Random();
		for (int i = 0; i < 100; i++)
		{
			Asteroid a = new Asteroid();
			a.setPosition(new Vector(r.nextDouble()* gameMap.getDimensions().getX(), 
									r.nextDouble()* gameMap.getDimensions().getY(),
									r.nextDouble()* gameMap.getDimensions().getZ()));
			boolean overlaps = false;
			for(Body b : gameMap) {
				if (a.isTouching(b)) {
					overlaps=true;
					break;
				}
			}
			
			if(!overlaps) {
				a.setVelocity(new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble()).scale(10));
				gameMap.add(a);
				System.out.println("notsTUCK");
			} else {
				System.out.println("stuck");
				i--;
			}
		}
		
	}

	public void run()
	{
		boolean running = true;
		while (running)
		{
			try
			{
				String str = in.readLine();
				Ship playerShip = (Ship)(gameMap.get(playerInt));
				switch(str){
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
					case "exit":
						running = false;
					default:
						throw new IllegalArgumentException("You done sent the wrong string yo");
				}
					
			}

			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		
	}
}
