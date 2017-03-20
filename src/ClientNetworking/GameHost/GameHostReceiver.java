package ClientNetworking.GameHost;

import java.io.ObjectInputStream;
import java.util.ArrayList;

import GameLogic.Resource;
import GameLogic.Ship;
import GameLogic.Weapon;
import ServerNetworking.ClientTable;

public class GameHostReceiver extends Thread
{
	private ObjectInputStream clientIn;
	private ClientTable clientTable;
	private int position;
	public MapContainer gameMap;
	public String teamMate;
	private String playerName;
	private int shipID;
	private KeySequence keySequence;

	public GameHostReceiver(ObjectInputStream reader, MapContainer map, ClientTable cT, int playerPos, String nickname,
			String teammate, int ship, KeySequence k)
	{
		gameMap = map;
		position = playerPos;
		clientTable = cT;
		clientIn = reader;
		teamMate = teammate;
		playerName = nickname;
		shipID = ship;
		keySequence = k;
	}

	public void run()
	{
		boolean running = true;
		while (running)
		{
			try
			{
				Object obj = clientIn.readObject();
				if (obj instanceof String)
				{
					String str = (String) obj;
					gameMap.updateMap(str, playerName, shipID);
				}
				else
				{
					ChatMessage m = (ChatMessage) obj;
					clientTable.getQueue(playerName).offer(m);

					// Engineer with PilotAI teammate
					if (teamMate.equals("") && position % 2 == 1)
					{
						if (m.message.contains("instruction"))
						{
							String id = "";
							String[] split = m.message.split(" ");
							for (int i = 0; i < split.length; i++)
							{
								if (split[i].equals("instruction"))
								{
									id = split[i + 1];
								}
							}
							ArrayList<String> seq = keySequence.getAllKeys();
							for (int i = 0; i < seq.size(); i++)
							{
								if (seq.get(i).contains(id))
								{
									clientTable.getQueue(playerName)
											.offer(new ChatMessage("Pilot", seq.get(i).split(":")[1]));
								}
							}
						}
					}
					// Pilot with Engineer teammate
					else if (teamMate.equals("") && position % 2 == 0)
					{
						ChatMessage message = null;
						Ship s = (Ship) gameMap.gameMap.get(shipID);
						Resource r = null;
						if (m.nickname.equals(playerName))
						{
							switch (m.message.toLowerCase())
							{
							case "shield":
							case "shields":
								r = s.getResource(Resource.Type.SHIELDS);
								message = new ChatMessage("Engineer", resourceLowMediumHigh(r.get(), 0, r.getMax()));
								break;

							case "hull":
							case "health":
								r = s.getResource(Resource.Type.HEALTH);
								message = new ChatMessage("Engineer", resourceLowMediumHigh(r.get(), 0, r.getMax()));
								break;

							case "fuel":
							case "engine":
							case "engines":
								r = s.getResource(Resource.Type.ENGINES);
								message = new ChatMessage("Engineer", resourceLowMediumHigh(r.get(), 0, r.getMax()));
								break;

							case "laser":
							case "lasers":
								r = s.getWeapon(Weapon.Type.LASER).getAmmoResource();
								message = new ChatMessage("Engineer", resourceLowMediumHigh(r.get(), 0, r.getMax()));
								break;

							case "plasma":
								r = s.getWeapon(Weapon.Type.PLASMA).getAmmoResource();
								message = new ChatMessage("Engineer", resourceLowMediumHigh(r.get(), 0, r.getMax()));
								break;

							case "torpedo":
								r = s.getWeapon(Weapon.Type.TORPEDO).getAmmoResource();
								message = new ChatMessage("Engineer", resourceLowMediumHigh(r.get(), 0, r.getMax()));
								break;

							default:
								message = new ChatMessage("Engineer", "I don't understand what you just said!");
							}
							clientTable.getQueue(playerName).offer(message);
						}
					}
					else
					{
						clientTable.getQueue(teamMate).offer(m);
					}

				}
			}

			catch (Exception e)
			{
				running = false;
				e.printStackTrace();
			}

		}

	}

	/**
	 * Returns if a resource's value is low, medium or high
	 * 
	 * @param val
	 *            The current value
	 * @param min
	 *            The minimum possible value
	 * @param max
	 *            The maximum possible value
	 * @return Whether the resource is currently low, medium or high
	 */
	private String resourceLowMediumHigh(int val, int min, int max)
	{
		if (val < ((max + min) / 3))
		{
			return "low";
		}
		else if (val < 2 * ((max + min) / 3))
		{
			return "medium";
		}
		else
		{
			return "high";
		}
	}
}
