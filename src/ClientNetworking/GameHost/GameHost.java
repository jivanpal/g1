//Server Class
package ClientNetworking.GameHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import GeneralNetworking.Lobby;
import GeneralNetworking.Player;
import ServerNetworking.ClientTable;

public class GameHost extends Thread {
	private final int PORT = 1273;
	private final Lobby lobby;
	private ServerSocket serverSocket = null;
	private ArrayList<KeySequence> keySequences= new ArrayList<KeySequence>();
	
	
	public GameHost(Lobby l) {
		lobby = l;
		
		// Open a server socket:
		try {
			serverSocket = new ServerSocket(PORT, 8, InetAddress.getLocalHost());
		} catch (IOException e) {
			System.err.println("Couldn't listen on port " + PORT);
		}
		int minLength = 2;
		int maxLength = 7;
		int sequenceNumber = 10;
		for(int  i=0;i<4;i++)
		{
			keySequences.add(new KeySequence(minLength, maxLength, sequenceNumber));
		}	
	}

	public void run() {
		try {
		
			ClientTable clientTable = new ClientTable();
			MapContainer gameMap = new MapContainer();
			Player[] p = lobby.getPlayers();
			//add the ship if the team is not there anyway to avoid errors
			for(int i=0;i<lobby.getPlayers().length;i+=2)
				gameMap.addShip(i, p[i]==null? "":p[i].nickname, p[i+1] == null? "" : p[i+1].nickname);
			gameMap.generateTerrain();
			System.out.println("I HAVE STARTED THE SERVER");
			while (true) {
				// Listen to the socket, accepting connections from new clients:
				Socket socket = serverSocket.accept();
				System.out.println("SOMEONE JOINED");

				ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
				toClient.flush();

				ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
				String clientName = "";

				try 
				{
					clientName = (String) fromClient.readObject();
				}
				catch (ClassNotFoundException e) 
				{
					e.printStackTrace();
				}

				int position = lobby.getPlayerPosByName(clientName);
				toClient.writeObject(keySequences.get(position/2));
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
				}
				if (!gameShouldStart) 
				{
					System.out.println("I CLOSED THE SOCKET FOR UNAUTHORISED PLAYER: "+ clientName);
					socket.close();
				} 
				else 
				{
					clientTable.add(clientName);

					int tmId = position+(position%2==0?1:-1);
					String tmName ="";
					if(lobby.getPlayers()[tmId]!=null)
						tmName = lobby.getPlayers()[tmId].nickname;

					GameHostReceiver clientInput = new GameHostReceiver(fromClient, gameMap, clientTable, position,clientName, tmName);
					clientInput.start();

					GameHostSender clientOutput = new GameHostSender(toClient, clientTable, clientName);
					toClient.reset();
					clientOutput.start();
				}
				GameClock clock = new GameClock(clientTable, gameMap);
				clock.start();
			}
		} 
		catch (IOException e) 
		{
			System.err.println("IO error " + e.getMessage());
		}
	}
}
