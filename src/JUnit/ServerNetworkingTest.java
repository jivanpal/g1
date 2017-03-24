package JUnit;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ClientNetworking.Client;
import ClientNetworking.IpGetter;
import ClientNetworking.GameClient.GameClient;
import ClientNetworking.GameHost.GameHost;
import GeneralNetworking.Action;
import GeneralNetworking.Lobby;
import GeneralNetworking.Player;

/**
 * this fuctions correctly only when ran on locally hosted server
 * @author Svetlin
 *
 */
public class ServerNetworkingTest {
	
	Client cp1 = new Client("player1");
	Client cp2 = new Client("player2");
	Lobby l  = new Lobby(cp1.name,IpGetter.getRealIp());

	@Before
	public void init(){
		System.out.println(cp1.name+ " "+ l.getPlayers()[0].nickname);
		cp1.send(l);
		cp2.updateList();

	}

	@Test
	public void test() {

		while(cp2.getLobbyList()==null || cp2.getLobbyList().getLobbies().length==0 || cp2.getLobbyList().getLobbies()[0]==null)
		{
			//System.out.println(cp2.getLobbyList().getLobbies().length);
			try
			{
				cp2.updateList();
				Thread.sleep(500);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(cp2.getLobbyList().getLobbies()[0].host);
		assertEquals(cp1.name,cp2.getLobbyList().getLobbies()[0].host);
		cp2.send(new Action(cp2.getLobbyList().getLobbies()[0].lobbyID,new Player(cp2.name,IpGetter.getRealIp(),false),Action.ADD));
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(cp2.name, cp1.getLobby().getPlayers()[1].nickname);
		
		
		cp1.send(new Action(cp1.getLobby().getID(),cp1.getLobby().getPlayers()[0],Action.START));
		GameHost ghost = new GameHost(cp1.getLobby());
		while(!cp1.getLobby().started || !cp2.getLobby().started)
		{
			try
			{
				Thread.sleep(200);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		GameClient gc1 = new GameClient(cp1.getLobby(),new Player(cp1.name,IpGetter.getRealIp(),true));
		GameClient gc2 = new GameClient(cp2.getLobby(),new Player(cp2.name,IpGetter.getRealIp(),false));
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(gc1.getMap().bodies().size()>0 && gc2.getMap().bodies().size()>0);
		
	}

}