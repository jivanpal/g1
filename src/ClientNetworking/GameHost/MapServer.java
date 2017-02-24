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

public class MapServer extends Thread {
	private final int PORT = 1273;
	private final Lobby lobby;
	private ServerSocket serverSocket = null;

	public MapServer(Lobby l) {
		lobby = l;
		
		// Open a server socket:
		try {
			serverSocket = new ServerSocket(PORT, 8, InetAddress.getLocalHost());
		} catch (IOException e) {
			System.err.println("Couldn't listen on port " + PORT);
		}
	}

	public void run() {
		try {
			ArrayList<char[][]> keySequences = KeySequenceGen.Generate(10,5);
			
			ClientTable clientTable = new ClientTable();
			MapContainer gameMap = new MapContainer();
			System.out.println(serverSocket.getInetAddress());
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
					System.out.println("I CLOSED THE SOCKET XD");
					socket.close();
				} 
				else 
				{
					clientTable.add(String.valueOf(String.valueOf(position)));

					int mapEntry = gameMap.addShip(position, clientName, players[position+1] == null? "" : players[position+1].nickname);

					GameHostReceiver clientInput = new GameHostReceiver(fromClient, gameMap, clientTable, position,clientName, mapEntry);
					clientInput.start();

					GameHostSender clientOutput = new GameHostSender(toClient, clientTable, String.valueOf(position));
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
