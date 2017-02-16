package UI;

import Views.EngineerView;
import Views.PilotView;

import javax.swing.JFrame;

public class TestGame {

	public static void main(String[] args) {
		PilotView pView = new PilotView("TestPlayer1");

		EngineerView eView = new EngineerView("TestPlayer2");

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(1900, 1080);
		f.add(eView);
		f.setVisible(true);
	}
}