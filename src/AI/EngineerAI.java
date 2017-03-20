package AI;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import ClientNetworking.GameClient.GameClient;
import ClientNetworking.GameHost.ChatMessage;
import ClientNetworking.GameHost.MapContainer;
import GameLogic.*;
import Physics.Body;
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
		Ship s = null;
		int cooldownTimer = 150;
		for (int i =0; i < m.size(); i++)
		{
			if (m.get(i) instanceof Ship)
			{
				s = (Ship) m.get(i);
				if (s.getPilotName().equals(nickname))
				{
					aiCalculations(s);
					break;
				}
			}
		}
		if(cooldownTimer==0)
		{
			for(int i=0;i<m.size();i++)
			{
				Body b = m.get(i);
					//System.out.println();
					if(b instanceof Ship && !(((Ship)b).equals(s)) && m.shortestPath(b.getID(),s.getID()).length()<100)
						{
							
							gameClient.send(new ChatMessage("Engineer","Enemy spotted nearby"));
							cooldownTimer=150;
							break;
						}
			}
		}
		else
			cooldownTimer--;
		
	}

	public void aiCalculations(Ship s)
	{
		Random r = new Random();
		switch (r.nextInt(30))
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
