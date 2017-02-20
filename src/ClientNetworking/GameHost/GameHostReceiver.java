package ClientNetworking.GameHost;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import GameLogic.*;
import ServerNetworking.ClientTable;


public class GameHostReceiver extends Thread
{
	private ObjectInputStream clientIN;
	private Map gameMap=null;
	private BufferedReader in;
	private ClientTable clientTable;
	private String playerPos;
	private int playerInt;
	public GameHostReceiver(ObjectInputStream reader, Map gM, ClientTable cT, String playerPos)
	{		
		in = new BufferedReader(new InputStreamReader(reader));
		gameMap = gM;
		this.playerPos = playerPos;
		clientTable = cT;
		playerInt = Integer.parseInt(playerPos);
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
