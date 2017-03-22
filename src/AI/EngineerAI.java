package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.stream.Collectors;

import ClientNetworking.GameClient.GameClient;
import ClientNetworking.GameHost.ChatMessage;
import ClientNetworking.GameHost.MapContainer;
import GameLogic.*;
import Physics.Body;
import Views.ResourcesView;

/**
 * 
 * @author Svetlin,James,Jaren
 *
 */

public class EngineerAI implements Observer
{
	private GameClient gameClient;
	private String nickname;

	private final int SHIP_PROXIMITY_MESSAGE_DISTANCE = 500;
	// cooldown ticks ~30 = 1 sec
	private final int coolDown = 150;
	private int cooldownTimer = coolDown;

	public EngineerAI(GameClient gameClient, String nickname)
	{
		this.gameClient = gameClient;
		this.nickname = nickname;
	}

	@Override
	public void update(Observable o, Object arg)
	{
		// get the map
		Map gameMap = gameClient.getMap();

		// get the ship list from the map
		List<Ship> ships = gameMap.bodies().parallelStream().filter(b -> b instanceof Ship).map(Ship.class::cast)
				.collect(Collectors.toList());

		// get the current team's ship
		Ship playerShip = ships.parallelStream().filter(s -> s.getPilotName().equals(nickname))
				.collect(Collectors.toList()).get(0);

		// do the "ai" things
		aiCalculations(playerShip);

		// check if a ship is nearby
		if (cooldownTimer == 0)
		{
			List<Ship> nearby = ships.parallelStream()
					.filter(s2 -> gameMap.shortestPath(playerShip.getID(), s2.getID())
							.length() < SHIP_PROXIMITY_MESSAGE_DISTANCE && !(playerShip.equals(s2)))
					.collect(Collectors.toList());

			if (nearby.size() > 1)
			{
				gameClient.send(new ChatMessage("Engineer", "There are enemy ships nearby!"));
				cooldownTimer = coolDown;
			} else if (nearby.size() > 0)
			{
				// Figure out where the ships are
				gameClient.send(new ChatMessage("Engineer", "There is an enemy ship nearby"));
				cooldownTimer = coolDown;
			}
			// reset the cooldown

		} else
			cooldownTimer--;

	}

	public void aiCalculations(Ship s)
	{
		// this is running
		Random r = new Random();
		ArrayList<Double> resources = new ArrayList<Double>();
		int i = 0;
		double min = 1;
		resources.add(s.getResource(Resource.Type.SHIELDS).getFraction());
		resources.add(s.getResource(Resource.Type.ENGINES).getFraction());
		resources.add(s.getWeapon(Weapon.Type.LASER).getAmmoResource().getFraction());
		resources.add(s.getWeapon(Weapon.Type.PLASMA).getAmmoResource().getFraction());
		resources.add(s.getWeapon(Weapon.Type.TORPEDO).getAmmoResource().getFraction());

		for (int j = 0; j < resources.size(); j++)
		{
			if (resources.get(j) < min)
			{
				min = resources.get(j);
				i = j;
			}
		}
		// nerf
		if (r.nextInt(100) < 1)
		{
//			System.out.println("I am doing things");
			switch (i)
			{
				case 0:
					if (r.nextInt(100) > 100 * s.getResource(Resource.Type.SHIELDS).getFraction())
					{
						gameClient.send("shieldReplenish");
					}
					break;
				case 1:
					if (r.nextInt(100) > 100 * s.getResource(Resource.Type.ENGINES).getFraction())
					{
						gameClient.send("fuelReplenish");
					}
					break;
				case 2:
					if (r.nextInt(100) > 100 * s.getWeapon(Weapon.Type.LASER).getAmmoResource().getFraction())
					{
						gameClient.send("laserReplenish");
					}
					break;
				case 3:
					if (r.nextInt(100) > 100 * s.getWeapon(Weapon.Type.PLASMA).getAmmoResource().getFraction())
					{
						gameClient.send("plasmaReplenish");
					}
					break;
				case 4:
					if (r.nextInt(100) > 100 * s.getWeapon(Weapon.Type.TORPEDO).getAmmoResource().getFraction())
					{
						gameClient.send("torpedoReplenish");
					}
					break;
				default:
					break;
			}
		}
	}

}
