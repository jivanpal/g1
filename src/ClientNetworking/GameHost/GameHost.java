//Server Class
package ClientNetworking.GameHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import AI.TargetingBot;
import ClientNetworking.IpGetter;
import GameLogic.Ship;
import GeneralNetworking.Lobby;
import GeneralNetworking.Player;
import Physics.Body;
import ServerNetworking.ClientTable;
/**
 * The game server, ran locally on the host's machine
 * @author Svetlin
 */
public class GameHost extends Thread
{
	private final int PORT = 1273;
	private final Lobby lobby;
	private ServerSocket serverSocket = null;
	private ArrayList<KeySequence> keySequences = new ArrayList<KeySequence>();

	//Create the clientTable
	ClientTable clientTable;
	//create an empty map
	MapContainer gameMap;
	
	int[] shipIds;
	/**
	 * Constructor, used to generate all things we need before the thread starts running - including things that would slow down the 
	 * machine before it starts accepting connections  
	 * @param l the lobby
	 */
	public GameHost(Lobby l)
	{
		//initialise the map and 
		clientTable  = new ClientTable();
		gameMap = new MapContainer();
		
		lobby = l;

		// Open a server socket:
		try
		{
			serverSocket = new ServerSocket(PORT, 8, IpGetter.getRealIp());
		} catch (IOException e)
		{
			System.err.println("Couldn't listen on port " + PORT);
		}
		//Key sequence variables
		int minLength = 2;
		int maxLength = 7;
		int sequenceNumber = 10;
		//generate key sequences for all ships
		for (int i = 0; i < 4; i++)
		{
			keySequences.add(new KeySequence(minLength, maxLength, sequenceNumber));
		}
		
		Player[] p = lobby.getPlayers();
		// add the ships, add skip ID's for non existant ones and then delete them
		shipIds = new int[4];
		for (int i = 0; i < Lobby.LOBBY_SIZE; i += 2)
		{
			if (p[i]==null && p[i + 1]==null){
				System.out.println("Added dummy "+Body.nextID);
				Body.nextID++;
				shipIds[i/2]=-1;
			}
			else
			{
				System.out.println("Added ship "+Body.nextID);
				shipIds[i/2]=gameMap.addShip(i, p[i] == null ? "" : p[i].nickname, p[i + 1] == null ? "" : p[i + 1].nickname);
				if(p[i]==null)
				{
					gameMap.gameMap.addBot(new TargetingBot(gameMap.gameMap,shipIds[i/2],-1));
				}
				
			}
		}
		//testing
	//	int botId = gameMap.addShip(2, "", "");
	//	gameMap.gameMap.addBot(new TargetingBot(gameMap.gameMap,botId,0));
		gameMap.generateTerrain();
		System.out.println("I HAVE STARTED THE SERVER");
	}

	public void run()
	{
		try
		{
			while (true)
			{
				// Listen to the socket, accepting connections from new clients:
				Socket socket = serverSocket.accept();
				System.out.println("SOMEONE JOINED");

				//create streams
				ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
				toClient.flush();

				ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
				String clientName = "";
				
				//read the client's name
				try
				{
					clientName = (String) fromClient.readObject();
				} catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				
				//get the client's position
				int position = lobby.getPlayerPosByName(clientName);
				//send the key sequences to the client
				toClient.writeObject(keySequences.get(position / 2));
				//check if the player was in the lobby
				boolean gameShouldStart = false;
				Player[] players = lobby.getPlayers();
				int pos;
				for (pos = 0; pos < players.length; pos++)
				{
					if (players[pos] != null && players[pos].nickname.equals(clientName))
					{
						gameShouldStart = true;
						break;
					}
					if(players[pos] != null)
						System.out.println(players[pos].nickname);
				}
				if (!gameShouldStart)
				{
					System.out.println("I CLOSED THE SOCKET FOR UNAUTHORISED PLAYER: " + clientName);
					socket.close();
				} else
				{
					//add the player to the table of clients
					clientTable.add(clientName);
					
					//get the player's teammate's position
					int tmId = position + (position % 2 == 0 ? 1 : -1);
					//get the teammate's name 
					String tmName = "";
					if (lobby.getPlayers()[tmId] != null)
						tmName = lobby.getPlayers()[tmId].nickname;

					//start the receiver and sender threads for the client
					GameHostReceiver clientInput = new GameHostReceiver(fromClient, gameMap, clientTable, position,clientName, tmName, shipIds[position/2], keySequences.get(position/2));
					clientInput.start();

					GameHostSender clientOutput = new GameHostSender(toClient, clientTable, clientName);
					toClient.reset();
					clientOutput.start();
				}
				//start the game clock
				GameClock clock = new GameClock(clientTable, gameMap);
				clock.start();
			}
		} catch (IOException e)
		{
			System.err.println("IO error " + e.getMessage());
		}
	}
	public void killServer()
	{
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
