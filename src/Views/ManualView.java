package Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import GameLogic.GameOptions;
/**
 * Class which displays the manual in the pilot view
 * @author Ivan Panchev
 *
 */
public class ManualView extends JPanel{
	private JButton previousPage;
	private JButton nextPage;
	private JButton closeView;
	private ManualInstructionsView instuctions;
	
	public ManualView(ArrayList<String> data, int size, int height){
		setLayout(new BorderLayout());

		previousPage = new JButton("<");
		styleButton(previousPage);
		
		nextPage = new JButton(">");
		styleButton(nextPage);
		
		previousPage.addActionListener(e -> goToPreviousPage());
		nextPage.addActionListener(e -> goToNextPage());
		
		add(previousPage, BorderLayout.WEST);
		add(nextPage, BorderLayout.EAST);
		
		instuctions = new ManualInstructionsView(data, size, height);

		add(instuctions,BorderLayout.CENTER);

		JPanel topBarPanel = new JPanel();
		topBarPanel.setLayout(new BoxLayout(topBarPanel, BoxLayout.X_AXIS));

		closeView = new JButton("X");
		closeView.addActionListener(e -> this.setVisible(false));

		topBarPanel.add(closeView);
		add(topBarPanel, BorderLayout.NORTH);
		
	}
	
	/**
	 * Applies the styling of the buttons used throughout the project 
	 */
	private void styleButton(JButton button){
		button.setFont(GameOptions.LARGE_BOLD_TEXT_FONT);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusable(false);
		button.setForeground(Color.BLACK);
	}
	
	/**
	 * Turns the manual to the next page
	 */
	public void goToNextPage() {
		instuctions.pageUp();
		update();
	}

	/**
	 * Turns the manual to the previous page
	 */
	public void goToPreviousPage() {
		instuctions.pageDown();
		update();
	}
	
	/**
	 * Repaint the manual
	 */
	private void update(){
		this.validate();
		this.repaint();
	}
}
