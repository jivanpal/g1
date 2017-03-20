package AI;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.stream.Collectors;

import ClientNetworking.GameClient.GameClient;
import ClientNetworking.GameHost.ChatMessage;
import ClientNetworking.GameHost.MapContainer;
import GameLogic.*;
import Views.ResourcesView;

public class EngineerAI implements Observer
{
	private GameClient gameClient;
	private String nickname;

	private final int SHIP_PROXIMITY_MESSAGE_DISTANCE = 30;

	public EngineerAI(GameClient gameClient, String nickname)
	{
		this.gameClient = gameClient;
		this.nickname = nickname;
	}

	@Override
	public void update(Observable o, Object arg)
	{
		Map m = gameClient.getMap();

		List<Ship> ships = m.bodies().stream()
				.filter(b -> b instanceof Ship)
				.map(Ship.class::cast)
				.collect(Collectors.toList());

		ships.forEach(this::aiCalculations);

		for(Ship s1 : ships) {
			List<Ship> nearby = ships.stream()
					.filter(s2 -> m.shortestPath(s1.getID(), s2.getID()).length() < SHIP_PROXIMITY_MESSAGE_DISTANCE
							      && !s1.equals(s2))
					.collect(Collectors.toList());

			if (nearby.size() > 2) {
				gameClient.send(new ChatMessage(nickname, "There are enemy ships nearby!"));
			} else if (nearby.size() > 1){
				// Figure out where the ships are
				gameClient.send(new ChatMessage(nickname, "There is an enemy ship nearby"));
			}
		}
	}

	public void aiCalculations(Ship s)
	{
		Random r = new Random();
		switch (r.nextInt(30))
		{
		case 0:
			if  (r.nextInt(100) > 100 * s.getResource(Resource.Type.SHIELDS).getFraction())
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
