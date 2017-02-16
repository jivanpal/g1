package Graphics;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class GameEngine{
	
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		TestScreen screen = new TestScreen();
		frame.add(screen);
		frame.setUndecorated(true);
		frame.setSize(screenSize);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
