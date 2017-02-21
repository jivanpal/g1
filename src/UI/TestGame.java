package UI;

import ClientNetworking.GameClient.GameClient;
import GeneralNetworking.Lobby;
import Views.EngineerView;
import Views.PilotView;

import javax.swing.JFrame;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestGame {

	public static void main(String[] args) {
		try {
			PilotView pView = new PilotView("TestPlayer1", new GameClient(new Lobby("James", InetAddress.getLocalHost())));

			// EngineerView eView = new EngineerView("TestPlayer2", new GameClient(new Lobby("James", InetAddress.getLocalHost())));

			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setSize(1900, 1080);
			f.add(pView);
			f.setVisible(true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}


	}
}