package ClientNetworking.GameHost;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import GameLogic.Map;


public class GameHostReceiver extends Thread
{
	private ObjectInputStream clientIN;
	private Map gameMap=null;
	private BufferedReader in;
	private int playerPos;
	public GameHostReceiver(ObjectInputStream reader, Map gM, int playerPos)
	{		
		in = new BufferedReader(new InputStreamReader(reader));
		gameMap = gM;
		this.playerPos = playerPos;
	}

	public void run()
	{
		boolean running = true;
		while (running)
		{
			try
			{
				String str = in.readLine();
				switch(str){
					case "fireWeapon1":
						
						break;
					case "fireWeapon2":
						break;
					case "fireWeapon3":
						break;
					case "accelerate":
						break;
					case "decelerate":
						break;
					case "pitchDown":
						break;
					case "pitchUp":
						break;
					case "rollLeft":
						break;
					case "rollRight":
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
