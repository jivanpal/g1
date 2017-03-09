package AI;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import ClientNetworking.GameClient.GameClient;
import ClientNetworking.GameHost.MapContainer;
import GameLogic.Engines;
import GameLogic.Map;
import GameLogic.Shields;
import GameLogic.Ship;
import Views.ResourcesView;

public class EngineerAI implements Observer
{
	private GameClient gameClient;
	private String nickname;

	public EngineerAI(GameClient gameClient, String nickname)
	{
		this.gameClient = gameClient;
		this.nickname = nickname;
	}

	@Override
	public void update(Observable o, Object arg)
	{
		Map m = gameClient.getMap();

		for (int i = MapContainer.ASTEROID_NUMBER; i < m.size(); i++)
		{
			if (m.get(i) instanceof Ship)
			{
				Ship s = (Ship) m.get(i);

				if (s.getPilotName().equals(nickname))
				{
					aiCalculations(s);
					break;
				}
			}
		}

	}

	public void aiCalculations(Ship s)
	{
		Random r = new Random();
		switch (r.nextInt(30))
		{
			case 0:
				if (r.nextInt(100) > 100 * s.getShieldLevels() / Shields.DEFAULT_MAX_SHIELDS_LEVEL)
				{
					gameClient.send("shieldReplenish");
				}
				break;
			case 1:
				if (r.nextInt(100) > 100 * s.getFuelLevel() / Engines.DEFAULT_FUEL_MAX_LEVEL)
				{
					gameClient.send("fuelReplenish");
				}
				break;
			case 2:
			{
				if (r.nextInt(100) > 100 * s.getLaserBlasterAmmo() / s.getWeaponMaxAmmoByIndex(Ship.LASER_BLASTER_INDEX))
				{
					gameClient.send("laserReplenish");
				}
			}
				break;
			case 3:
			{
				if (r.nextInt(100) > 100 * s.getPlasmaBlasterAmmo() / s.getWeaponMaxAmmoByIndex(Ship.PLASMA_BLASTER_INDEX))
				{
					gameClient.send("plasmaReplenish");
				}
			}
				break;
			case 4:
			{
				if (r.nextInt(100) > 100 * s.getTorpedoWeaponAmmo() / s.getWeaponMaxAmmoByIndex(Ship.TORPEDO_WEAPON_INDEX))
				{
					gameClient.send("torpedoReplenish");
				}

			}
			break;
			default:
				break;
		}
	}

}
